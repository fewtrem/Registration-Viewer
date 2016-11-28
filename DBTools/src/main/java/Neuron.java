
import ij.*;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
public class Neuron {
	/*
	// From this program (i.e. Editable):
	String notes,nameDB,certainty,possibleOthers;
	// From Python:
	String gmrLine;
	String stackFolderSource;
	String folder;
	// From Python (Images) and worked out from the Hashmap:
	String origxproj,origyproj,origzproj;
`	String regxproj, regyproj, regzporj;
	String scorexproj,scoreyproj,scorezproj;
	// From JSON:
	String origFile,origFolder;
	// From Pickle:
	String neuronKey,singleScore,cOM,channel,vol,inVol;
	*/
	
	// Constants
	public static final String TRUE = "true";
	public static final String FALSE = "false";	
    static final String UNASSIGNED = "unassigned";
    // neuron field, required:
    static final String UNID = "unid";
    static final String UID = "uid";
    static final String SINGLESCORE = "singleScore";
    static final String CHANNEL = "channel";
    static final String GMRLINE = "gmrLine";
    static final String NEURONKEY = "neuronKey";
    static final String FOLDER = "folder";
    static final String APPROVED = "approved";
    static final String MYSCORE = "myScore";
    // optional:
    static final String ORIGFILE = "origFile";
    static final String ORIGFOLDER = "origFolder";
    static final String COM = "cOM";
    static final String VOL = "vol";
    static final String INVOL = "inVol";
    static final String STACKFOLDERSOURCE = "stackFolderSource";
    // editable, optional:
    static final String BESTINCLASS = "bestInClass";
    static final String NOTES = "notes";
    static final String NAMEDB = "nameDB";
    static final String CERTAINTY = "certainty";
    static final String POSSIBLEOTHERS = "possibleOthers";
	// Settable
	private String sourceFolder;
	private HashMap<String,String> theNeuronFiles;

	/*
	 * ImagePlus origxproj,origyproj,origzproj;
`	 * ImagePlus regxproj, regyproj, regzporj;
	 * ImagePlus scorexproj,scoreyproj,scorezproj;
	 */
	static final String[] REQUIREDKEYS = {FOLDER,UID,UNID,SINGLESCORE,CHANNEL,GMRLINE,NEURONKEY};
	static final String[] EDITABLEKEYS = {MYSCORE,APPROVED,NOTES,NAMEDB,CERTAINTY,POSSIBLEOTHERS,BESTINCLASS};
	
	// For ImageJ:
	// store the XML data in accessible format:
	private HashMap<String,String> XMLData;
	// store the images:
	private HashMap<String,ImagePlus> imageStore;
	// referene to replacers:
	private HashMap<String,String> replacer;
	// reference to element:
	private Element eElement;
	// the order neuron was created in (only for error outputs in case there is no name!)
	private Integer thisNeuronid;
	/**
	 * @param eElement The element that represents this neuron from XML
	 * @param thisNeuroni, the order neuron was created in (only for error outputs in case there is no name!)
	 * @param sourceFolder, String, the source folder containing the folders of images.
	 * @param theNeuronFiles HashMap<String,String>, the files needed to be loaded by this neuron <name id,filename>
	 * @param replacer HashMap<String,String> reference to replacer hashmap

	 */
	
	// Tested, Reviewed 23/07/2016
	public Neuron(Element eElement, int thisNeuroni, String sourceFolder,HashMap<String,String> theNeuronFiles,HashMap<String,String> replacer ) {
		this.eElement = eElement;
		this.replacer = replacer;
		this.thisNeuronid = thisNeuroni;
		this.sourceFolder = sourceFolder;
		this.theNeuronFiles = theNeuronFiles;
		this.XMLData = new HashMap<String,String>();
		this.imageStore = new HashMap<String,ImagePlus>();
		// Get the neuron attributes:
        NamedNodeMap nnm = eElement.getAttributes();
        if (nnm != null){
           for (int i=0; i<nnm.getLength(); i++){
              Node n = nnm.item(i);
              XMLData.put(n.getNodeName(),eElement.getAttribute(n.getNodeName()));
           }
        }
		for (String key : this.theNeuronFiles.keySet()){
			EdRegViewer_.openImage(key,this.theNeuronFiles.get(key),this,imageStore);
		}
	}
	
	//Tested, Reviewed 23/07/2016
	// Makes sure required keys are in this neuron's hash map (run before it is added to any validated lists)
	public boolean validateHashMap(){
		boolean isOkay = true;
		for (String key : REQUIREDKEYS){
			if (this.XMLData.containsKey(key)==false){
				isOkay = false;
				IJ.log("Missing XML data detected ["+key+"] for neuron at position "+Integer.toString(this.thisNeuronid));
			}
		}
		return isOkay;
	}
	
	//Tested, Reviewed 23/07/2016
	// Puts the XML data into the XML element, ready to be saved to disk.
	public void updateXMLElement(){
		for (String editableElement:EDITABLEKEYS){
			if (XMLData.containsKey(editableElement)==true){
				eElement.setAttribute(editableElement,XMLData.get(editableElement));
			}
		}
	}
	
	//Tested, Reviewed 23/07/2016
	// Returns a value from this neuron's XML data hash map.
	public String getXMLData(String key){
		String toReturn = null;
		if (XMLData.containsKey(key)==true){
			toReturn = XMLData.get(key);
		}
		else{
			IJ.log("Key \""+key+"\" does not exist for neuron numbered "+this.thisNeuronid);
		}
		if (toReturn == null){
			toReturn = UNASSIGNED; 
		}
		return toReturn;
	}
	
	//Tested, Reviewed 23/07/2016
	// Puts a new value into this Neuron's XMLData hash map.
	public boolean setXMLData(String key,String value){
		boolean done = false;
		for (String editableAttributeName:EDITABLEKEYS){
			if (editableAttributeName.equals(key)){
				XMLData.put(key,value);
				done = true;
			}
		}
		if (done==false){
			IJ.log("Cannot edit key "+key+" as protected / does not exist!");
		}
		return done;
	}
	
	//Tested, Reviewed 23/07/2016
	// Returns the score as a double for building ordered lists:
	public Double getScoreForList(){
		if (XMLData.containsKey(SINGLESCORE)==true){
			return Double.parseDouble(XMLData.get(SINGLESCORE));
		}
		else
			return 0.0;
	}
	
	//Tested, Reviewed 23/07/2016
	// Returns the string value for display in the lists.
	public String toString(){
		String addAtStart = "";
		String addAtEnd = "";
		if (XMLData.get(BESTINCLASS) == TRUE){
			addAtStart = " * ";
		}
		if (XMLData.get(SINGLESCORE).equals(UNASSIGNED)!=true){
			addAtEnd = " ("+XMLData.get(SINGLESCORE)+")";
		}
		return addAtStart+XMLData.get("unid")+addAtEnd;
	}
	
	//Tested, Reviewed 23/07/2016
	public HashMap<String,String> getReplacer(){
		return replacer;
	}
	
	//Tested, Reviewed 23/07/2016
	public String getSourceFolder(){
		return sourceFolder;
	}
	
	//Tested, Reviewed 23/07/2016
	public ImagePlus getImage(String imagename){
		return imageStore.get(imagename);
	}
	
	//Tested, Reviewed 23/07/2016
	public Element getElement(){
		return eElement;
	}
	
	
	
}