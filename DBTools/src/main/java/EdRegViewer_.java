
/*
    Author: David Wood, week of 18th July 2016.
    david.wood@ed.ac.uk
    davidwood.eu

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This plugin is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details. 
 */
//TODO: ensure update of selected neuron's displayed value and best in class fields;
//TODO: ensure XML unicode safe...
//TODO: Consider adding the menu option to change the original+registered stack pre-location. (and make sure this gets saved).
//TODO: Consider making an override function for the default files in the XML.
//TODO: Consider replacing getScaledInstance with faster thing.
//TODO: Stop buttons from disappearing if window too small...
//TODO: Possibly get the scroll window to stay in the same place on multiple windows.
//TODO: Check that the viewer info. grid dimensions update without having to resize the main window...
import ij.*;
import ij.gui.*;
import ij.io.OpenDialog;
import ij.io.Opener;
import ij.plugin.frame.PlugInFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


public class EdRegViewer_ extends PlugInFrame{
	// Constants
	public static final String VERSION = "1.0.0";
	public static final String PROGRAMNAME = "Registered Stack Viewer";
	public static final String EXTRATEXT = "ImageJ Pulgin";
	public static final String TYPE = "type";
	public static final String NAME = "name";
	public static final String FILENAME = "filename";
	public static final String TYPE_NEURON = "neuron";
	public static final String TYPE_ORIG = "original";
	public static final String TYPE_REPLACEMENTS = "replacement";
	public static final String TYPE_TEXT = "text";
	public static final String TYPE_TEXTFIELD = "textfield";
	public static final String TYPE_TEMPLATE = "templateFileName";
	public static final String TYPE_FILEPRE = "origPathPre";
	public static final String[] REQUIREDATTRIBUTES = {NAME,TYPE};
	private static final long serialVersionUID = 1L;
	public static final int[] CHANNELINTS = {16,8};
	public static final int[] CHANNELXS = {0xff0000,0x00ff00};
	public static final String DATASTRUCTURE = "datastructure";
	public static final String OUTPUTENCODING = "UTF-8";
	public static final String FOLDER = "folder";
	//the template image:
	private ImagePlus template;
	// The list of entries ordered as in list:
	private ArrayList<Neuron> entries;
	// The source folder
	private String sourceFolder;
	// The XML Document object
	private Document doc;
	// The XML file
	private String xmlFile;
	// A place to store the file system:
	private ArrayList<HashMap<String,String>> fileSys;
	// The main neuron viewer:
	private NeuronViewer theViewer;
	// Neuron file names:
	private HashMap<String,String> thisNeuronFiles;
	// replacement values for ___XXX___ in the neuron XMLs:
	private HashMap<String,String> replacementValues;
	// The NTL:
	private NeuronTypeList theNTL;
	// Dialog related fields:
	private JFrame dialog;
	private JTextField newName;
	// Supplementary viewer:
	public ArrayList<SuppViewer> suppFrames;
	// Location of original files:
	private String origFilePre;
	
	//Tested, Reviewed 23/07/2016
	public EdRegViewer_(){
		super(PROGRAMNAME+" - "+VERSION+" - "+EXTRATEXT);
		// Set up the lists:
		fileSys = new ArrayList<HashMap<String,String>>();
		entries = new ArrayList<Neuron>();
		suppFrames = new ArrayList<SuppViewer>();
		sourceFolder = "";
		template = null;
		doc = null;
		xmlFile = null;
		theViewer = null;
		thisNeuronFiles = null;
		replacementValues = null;
		theNTL = null;
		dialog = null;
		newName = null;
		origFilePre = "";
	}
	
	//Tested, 
	// Run the program as a plugin for ImageJ / FIJI:
	public void run(String arg) {
	   loadXML();
	   loadTemplate();
	   findOrigFilePre();
	   theViewer = new NeuronViewer(sourceFolder,fileSys,replacementValues,template,true,origFilePre);
	   for (Neuron addOrigImageN: entries){
		   theViewer.getOrigImages(addOrigImageN);
	   }
	   theNTL = new NeuronTypeList(entries,true);
	   theNTL.setRegViewer(this);
	   loadGUI(this,theViewer,theNTL);
	   addMenu();
	   return;
	}
	
	//Tested, Reviewed 23/07/2016
	// Load the XML from an external document that the user selects.
	public void loadXML(){
		OpenDialog whatIsFile = new OpenDialog("Select the xml document","");
		String dir = whatIsFile.getDirectory();
		String fN = whatIsFile.getFileName();
		if (dir!=null&&fN!=null){
			File toGet = new File(dir,fN);
			String pathToOpen = toGet.toString();//"/media/s1144899/JaneliaBlue/Senders/Test2/index.xml";
				IJ.log("Opening "+pathToOpen+"...");
				try{
					OutputStreamWriter errorWriter = new OutputStreamWriter(System.err,
							OUTPUTENCODING);
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					// Use the Error Handler:
					db.setErrorHandler(new MyErrorHandler (new PrintWriter(errorWriter, true)));
					File dbFile = new File(pathToOpen);
					sourceFolder = dbFile.getParent();
					xmlFile = pathToOpen;
					doc = db.parse(dbFile);
					doc.getDocumentElement().normalize();
				}
				catch (Exception e){
					IJ.log("Error with XML File: "+e.getMessage());
				}
				if (doc!=null){
					loadFileSystem(doc);
					loadNeurons(doc);
				}
		}
		else{
			IJ.log("No XML file selected");
			this.dispose();
		}
	}
	
	//Tested, Reviewed 23/07/2016
	// Load a file system with a format obeyed by files in folders for this XML file.
	// XML nodes are either "datastructure" i.e. for the whole project, or they are "neuron" i.e. for individual neurons.
	// All datastructure nodes need to have "name" and "type".
	// we expect many types of node types here (see constants).
	public void loadFileSystem(Document doc){
		// Get the datastructure nodes:
		NodeList dList = doc.getElementsByTagName(DATASTRUCTURE);
		for (int temp = 0; temp < dList.getLength(); temp++) {
			Node dNode = dList.item(temp);
			// Check is "Element Node"
			if (dNode.getNodeType() == Node.ELEMENT_NODE) {
				Element dElement = (Element) dNode;
				NamedNodeMap nnm = dElement.getAttributes();
				boolean success = true;
				HashMap<String,String> newHM = new HashMap<String,String>();
		        if (nnm != null){
		        	// go through the arrtibutes:
		            for (int i=0; i<nnm.getLength(); i++){
		              Node n = nnm.item(i);
		              newHM.put(n.getNodeName(),n.getNodeValue());
		            }
		            // Check it has all the required values...
		            for (String reqAt:REQUIREDATTRIBUTES){
		            	if (newHM.containsKey(reqAt)==false){
		            		success=false;
		            	}
		            }
		        }
		        else{
		        	success = false;
		        }
		        if (success==true){
		        	fileSys.add(newHM);
		        }
		        else{
		        	IJ.log("Failed to add datastructure element number "+(temp+1)+" (check required attributes are there)");
		        }
			}
		}
		replacementValues = getReplacementValues();
		thisNeuronFiles = findFileNamesOfType(fileSys,TYPE_NEURON);
	}
	
	//Tested, Reviewed 23/07/2016
	// get from the fileSystem the tags to replace which are organised as:
	// "___unid___":"unid"
	// where the first is the string to replace, the second the name of the attribute in the neuron to replace it with.
	public HashMap<String,String> getReplacementValues(){
		HashMap<String,String> toRet = new HashMap<String,String> ();
		for (HashMap<String,String> thisHM : fileSys){
			if (thisHM.get(EdRegViewer_.TYPE).equals(TYPE_REPLACEMENTS)){
				toRet = new HashMap<String,String>(thisHM);
			}
			// don't replace the words "type" and "name"!
			toRet.remove(EdRegViewer_.TYPE);
			toRet.remove(EdRegViewer_.NAME);
		}
		return toRet;
	}
	
	//Tested, Reviewed 23/07/2016
	// Creates neurons in the order returned from the Document:
	public void loadNeurons(Document doc){
		NodeList nList = doc.getElementsByTagName(TYPE_NEURON);
		IJ.log(Integer.toString(nList.getLength())+" neurons found");
		for (int thisNeuronNumber = 0; thisNeuronNumber < nList.getLength(); thisNeuronNumber++) {
            Node nNode = nList.item(thisNeuronNumber);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNode;
        	   Neuron curEntry = new Neuron(eElement,thisNeuronNumber+1,sourceFolder,thisNeuronFiles,replacementValues);
        	   if (curEntry.validateHashMap()==true){
        		   entries.add(curEntry);
        	   }
            }
        }
	}
	
	//Tested, Reviewed 23/07/2016
	// replaces input String with neuron attributes' values.
	public static String doReplacement(String inStr,HashMap<String,String> replacementValues,Neuron inNeuron){
		if (inStr!=null && replacementValues!=null){
			for (String key : replacementValues.keySet()){
				String newVal;
				if (inNeuron==null){
					newVal = "<<no neuron>>";
				}
				else{
					newVal = inNeuron.getXMLData(replacementValues.get(key));
				}
				inStr = inStr.replace(key, newVal);
			}
		}
		return inStr;
	}
	
	//Tested, Reviewed 23/07/2016
	// A function that opens NEURON related images!!
	public static void openImage(String nameIn, String thisFileName,Neuron nIn,HashMap<String,ImagePlus> output){
		thisFileName = EdRegViewer_.doReplacement(thisFileName,nIn.getReplacer(),nIn);
		File thisFileFolder = new File(new File(nIn.getSourceFolder()),nIn.getXMLData(FOLDER));
		File thisFilePath = new File(thisFileFolder,thisFileName);
		ImagePlus newImage = loadImage(thisFilePath);
		if (newImage!=null){
			output.put(nameIn,newImage);
		}
		if (output.containsKey(nameIn)==false){
			IJ.log("Blank image loaded instead...");
			ImagePlus blankImage = NewImage.createByteImage("Blank Image", 10, 10, 1,NewImage.FILL_RAMP);
			blankImage.getFileInfo().fileName = thisFileName;
			blankImage.getFileInfo().directory = thisFileFolder.toString();
			output.put(nameIn,blankImage);
		}
	}
	
	//Tested, Reviewed 23/07/2016
	// A function that loads an image.
	// Can return null!
	public static ImagePlus loadImage(File fileIn){
		ImagePlus toRet = null;
		try{
			Opener opener = new Opener();
			IJ.log("Opening "+fileIn.toString());
			toRet = opener.openImage(fileIn.toString());
		}
		catch(Exception e){
			IJ.log("Error opening image: "+fileIn.toString()+":\n"+e.getMessage());
		}
		return toRet;
	}
	
	//Tested, Reviewed 23/07/2016
	// load the template file in if it is available
	public void loadTemplate(){
		String templateImg = null;
		// go through the file system to get this name value:
		for (HashMap<String,String> thisHM : fileSys){
			if (thisHM.get(EdRegViewer_.TYPE).equals(TYPE_TEMPLATE)){
				templateImg = thisHM.get(NAME);
			}
		}
		if (templateImg!=null){
			File thisFilePath = new File(new File(sourceFolder),templateImg);
			this.template = loadImage(thisFilePath);
		}
		else{
			IJ.log("Could not find a template reference in XML file");
		}
	}
	
	//Tested, Reviewed 23/07/2016
	// Get the original file "Pre" path from the file system (XML storage): 
	public void findOrigFilePre(){
		String preStr = "";
		for (HashMap<String,String> thisHM : fileSys){
			if (thisHM.get(EdRegViewer_.TYPE).equals(TYPE_FILEPRE)){
				preStr = thisHM.get(NAME);
			}
		}
		if (preStr.equals("")==false){
			this.origFilePre = preStr;
		}
		else{
			IJ.log("Could not find a template reference in XML file");
		}
	}
	
	//Tested, Reviewed 23/07/2016
	// Get a list of filenames for a specified input type.
	public static HashMap<String,String> findFileNamesOfType(ArrayList<HashMap<String,String>> fileSys,String typeIn){
		HashMap<String,String> toReturn = new HashMap<String,String>();
		for (HashMap<String,String> thisHM : fileSys){
			if (thisHM.get(EdRegViewer_.TYPE).equals(typeIn)){
				if (thisHM.containsKey(EdRegViewer_.FILENAME)==true){
					toReturn.put(thisHM.get(EdRegViewer_.NAME),thisHM.get(EdRegViewer_.FILENAME));
				}
			}
		}
		return toReturn;
	}
	
	//Tested, Reviewed 23/07/2016
	public static void loadGUI(PlugInFrame frameIn,NeuronViewer viewIn,NeuronTypeList NTL){
		// Set up the GUI that incorporates the lists and the viewer:
		frameIn.setLayout(new BorderLayout());
	    JPanel pane = new JPanel();
	    pane.setLayout(new BorderLayout());
	    //pane.set(new Dimension(100,100));
	    NTL.getMainList().setFixedCellWidth(200);
	    NTL.getExampleList().setFixedCellWidth(200);
	    JScrollPane scrollPaneML = new JScrollPane(NTL.getMainList());
	    //scrollPaneML.setPreferredSize(new Dimension(100,100));
	    JScrollPane scrollPaneEL = new JScrollPane(NTL.getExampleList());
	    //scrollPaneEL.setPreferredSize(new Dimension(100,100));
	    pane.add(scrollPaneML,BorderLayout.CENTER);
	    pane.add(scrollPaneEL,BorderLayout.SOUTH);
	    frameIn.add(pane,BorderLayout.WEST);
	    NTL.setViewer(viewIn);
	    frameIn.add(viewIn.getJPanel(),BorderLayout.CENTER);
	    viewIn.getInfoPanel().add(NTL.generateButtons(),BorderLayout.NORTH);
	    frameIn.setMinimumSize(new Dimension(500,500));
	    frameIn.pack();
		GUI.center(frameIn);
		frameIn.setVisible(true);
	}
	
	//Tested, Reviewed 23/07/2016
	public void addMenu(){
		MenuBar menuBar = new MenuBar();
		Menu menu = new Menu("Menu");;
		menu.setShortcut(new MenuShortcut(KeyEvent.VK_M));
		menu.getAccessibleContext().setAccessibleDescription(
		        "The only menu in this program that has menu items");
		MenuItem menuItem = new MenuItem("Open a read-only Viewer");
		menuItem.getAccessibleContext().setAccessibleDescription("Opens a read-only version of this viewer for viewing neurons side-by-side");
		menuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				supplementaryViewer();
			}
		});
		menu.add(menuItem);
		menuItem = new MenuItem("Add neuron class");
		menuItem.getAccessibleContext().setAccessibleDescription("Add a new neuron class");
		menuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				addNeuronClassDialog();
			}
		});
		menu.add(menuItem);
		menuItem = new MenuItem("Delete neuron class");
		menuItem.getAccessibleContext().setAccessibleDescription("Delete the selected neuron class");
		menuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (theNTL!=null){
					theNTL.deleteNeuronClassFromList();
				}
				else{
					IJ.log("Initialise the Lists first!");
				}
			}
		});
		menu.add(menuItem);
		menuItem = new MenuItem("Save");
		menuItem.getAccessibleContext().setAccessibleDescription("Save to XML current changes");
		menuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				saveDOM(xmlFile);
			}
		});
		menu.add(menuItem);
		menuItem = new MenuItem("Close all other windows");
		menuItem.getAccessibleContext().setAccessibleDescription("Close all other windows except this one");
		menuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				closeAllOtherWindows();
			}
		});
		menu.add(menuItem);
		menuBar.add(menu);
		this.setMenuBar(menuBar);
	}
	
	//Tested, Reviewed 23/07/2016
	// Create a supplementary viewer as required:
	public void supplementaryViewer(){
		   NeuronViewer newViewer = new NeuronViewer(sourceFolder,fileSys,replacementValues,template,false,origFilePre);
		   // reuse the original image list!
		   newViewer.setOrigImageList(theViewer.getOrigImageList());
		   // reuse the entries neuron list:
		   NeuronTypeList newNTL = new NeuronTypeList(entries,false);
		   newNTL.setRegViewer(this);
		   SuppViewer newPIF = new SuppViewer("Supplementary Viewer "+(this.suppFrames.size()+1),newNTL,newViewer);
		   loadGUI(newPIF,newViewer,newNTL);
		   this.suppFrames.add(newPIF);
		   newPIF.addWindowListener(new java.awt.event.WindowAdapter() {
			    @Override
			    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
			    		suppFrames.remove((PlugInFrame)windowEvent.getSource());
			    	}
			});
	}

	//Tested, Reviewed 23/07/2016
	// Dialog to add a new neuron class (yes it is long!...)
	public void addNeuronClassDialog(){
		this.setEnabledA(false);
		if (theViewer != null){
			dialog = new JFrame("New neuron class");
			dialog.setLayout(new BoxLayout(dialog.getContentPane(),BoxLayout.Y_AXIS));
			JLabel toAddL = new JLabel("Please enter the name of the new neuron class:");
			toAddL.setAlignmentX(Component.CENTER_ALIGNMENT);
			dialog.add(toAddL);
			newName = new JTextField();
			newName.setColumns(30);
			newName.setMaximumSize(newName.getPreferredSize());
			newName.setAlignmentX(Component.CENTER_ALIGNMENT);
			dialog.add(newName);
			JButton OKButton = new JButton("OK");
			OKButton.addActionListener (new ActionListener(){
		    	public void actionPerformed(ActionEvent e){
		    		theNTL.addNeuronClass(newName.getText());
		    		dialog.setVisible(false);
		    		dialog.dispose();
		    		EdRegViewer_.this.setEnabledA(true);
		    	}
		    });
			dialog.addWindowListener(new java.awt.event.WindowAdapter() {
			    @Override
			    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
			    	dialog.dispose();
			    	EdRegViewer_.this.setEnabledA(true);
			    }
			});
			OKButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			dialog.add(OKButton);
			dialog.pack();
			dialog.setVisible(true);
		}
		else{
			IJ.log("Instantiate the viewer first!");
		}
	}
	
	//Tested, Reviewed 23/07/2016
	// Close all other windows including supplementary viewers:
	public void closeAllOtherWindows(){
		for (PlugInFrame pif : suppFrames){
			pif.dispose();
		}
		for (SuppViewer sv : suppFrames){
			for (ImagePlus ip : sv.nv.getOpenStackList()){
				ip.hide();
			}
			for (ImageWindow ip : sv.nv.getOpenWindows()){
				ip.dispose();
			}
		}
		for (ImagePlus ip : theViewer.getOpenStackList()){
			ip.hide();
		}
		for (ImageWindow ip :theViewer.getOpenWindows()){
			ip.dispose();
		}
	}
	
	//Tested, Reviewed 23/07/2016
	// Save back to XML:
	public void saveDOM(String xmlFile){
	    try {
	    	for (Neuron thisNeuron : entries){
	    		thisNeuron.updateXMLElement();
	    	}
	        Transformer tr = TransformerFactory.newInstance().newTransformer();
	        tr.setOutputProperty(OutputKeys.INDENT, "yes");
	        tr.setOutputProperty(OutputKeys.METHOD, "xml");
	        tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	        // send DOM to file
	        tr.transform(new DOMSource(doc), 
	                             new StreamResult(new FileOutputStream(xmlFile)));
	    } catch (TransformerException te) {
	        System.out.println(te.getMessage());
	        IJ.log("Could not save XML File");
	        JFrame SimpleFileChooser = new JFrame();
	    	JFileChooser chooser = new JFileChooser();
	        int option = chooser.showSaveDialog(SimpleFileChooser);
	        if (option == JFileChooser.APPROVE_OPTION) {
	        	saveDOM(chooser.getSelectedFile().toString());
	        }
	    } catch (IOException ioe) {
	        System.out.println(ioe.getMessage());
	        IJ.log("Could not find XML File");
	        JFrame SimpleFileChooser = new JFrame();
	    	JFileChooser chooser = new JFileChooser();
	        int option = chooser.showSaveDialog(SimpleFileChooser);
	        if (option == JFileChooser.APPROVE_OPTION) {
	        	saveDOM(chooser.getSelectedFile().toString());
	        }
	    }
	}
	
	//Reviewed 23/07/2016
	//ERROR HANDLING
	private static class MyErrorHandler implements ErrorHandler {
	    
	    private PrintWriter out;

	    MyErrorHandler(PrintWriter out) {
	        this.out = out;
	    }

	    private String getParseExceptionInfo(SAXParseException spe) {
	        String systemId = spe.getSystemId();
	        if (systemId == null) {
	            systemId = "null";
	        }

	        String info = "URI=" + systemId + " Line=" + spe.getLineNumber() +
	                      ": " + spe.getMessage();
	        return info;
	    }

	    public void warning(SAXParseException spe) throws SAXException {
	        out.println("Warning: " + getParseExceptionInfo(spe));
	    }
	        
	    public void error(SAXParseException spe) throws SAXException {
	        String message = "Error: " + getParseExceptionInfo(spe);
	        throw new SAXException(message);
	    }

	    public void fatalError(SAXParseException spe) throws SAXException {
	        String message = "Fatal Error: " + getParseExceptionInfo(spe);
	        throw new SAXException(message);
	    }
	}
	
	//Tested, Reviewed 23/07/2016
	// Ask if user really wants to close:
    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        if (JOptionPane.showConfirmDialog(this, 
            "Are you sure to close this plugin?", "Really Closing?", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
        	closePlugin();
        }
    }
    
    public void setEnabledA(boolean tfA){
    	this.setEnabled(tfA);
    	if (theNTL != null){
    		theNTL.setEnabled(tfA);
    	}
    }
    
	//Tested, Reviewed 23/07/2016
    // Ask for a save and close:
	public void closePlugin(){
		theViewer.closeCurrentNeuron();
		if (JOptionPane.showConfirmDialog(this, 
	            "Do you want to save?", "Save data?", 
	            JOptionPane.YES_NO_OPTION,
	            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
			saveDOM(xmlFile);
		}
		this.dispose();
	}
	
	//Tested, Reviewed 23/07/2016
	public void setSourceFolder(String sourceIn){
		sourceFolder = sourceIn;
	}
	
	//Tested, Reviewed 23/07/2016
	public String getSourceFolder(){
		return sourceFolder;
	}
}