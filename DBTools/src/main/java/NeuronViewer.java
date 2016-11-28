
import ij.IJ;
import ij.ImagePlus;
import ij.gui.ImageWindow;
import ij.gui.NewImage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;


public class NeuronViewer{
	// Constants:
	// Positions:
	public static final Integer DEFAULTPOSITION = 10000;
	public static final String POSITIONORDER = "positionOrder";
	public static final String WEIGHTX = "weigthx";
	public static final String GRIDX = "gridx";
	public static final String GRIDY = "gridy";
	public static final String GRIDWIDTH = "gridwidth";
	//Scroll Frame:
	public static final int FRAMEX = 500;
	public static final int FRAMEY = 500;	
	// Further attributes of images:
	public static final String TABNAME = "tabName";
	public static final String XPOS = "xpos";
	public static final String YPOS = "ypos";
	public static final String XDIM = "xdim";
	public static final String YDIM = "ydim";
	public static final String COLS = "cols";
	// Tab attributes:
	public static final String TYPE_TAB = "tab"; 
	public static final String DEFAULT = "Default";
	public static final String TABNUMBER = "tabNumber";
	public static final String TEXTVALUE = "textValue";
	public static final String TABTEXT = "displayName";
	public static final int DEFAULTINT = 100;
	public static final int MAXTABS = 10000;
	public static final double INNERBORDER = 5.0;
	
	// convenience:
	// the image-key to filename (pre-replacer) for images:
	private HashMap<String,String> origFileList;
	private HashMap<String,String> neuronFileList;
	// the image-key to tab-key list:
	private HashMap<String,String> origTabList;
	private HashMap<String,String> neuronTabList;
	// easy way to find the corresponding JLabels,textfields etc.:
	private HashMap<String,JLabel> origFileLabels;
	private HashMap<String,JLabel> neuronFileLabels;
	private HashMap<String,JLabel> textLabels;
	private HashMap<String,JTextField> textFields;
	// place to store the JLabel text prior to replacement with inputs from other neurons:
	private HashMap<String,String> textLabelsText;
	// A place to store the Original Images:
	private HashMap<String,HashMap<String,ImagePlus>> origImages;
	// list of tab-keys
	private ArrayList<String> tabs;
	// List of open Image windows (for ease of closing)
	private ArrayList<ImageWindow> openWindows;
	// Open stacks (for ease of closing)
	private ArrayList<ImagePlus> openStacks;
	
	//References:
	// fileSys:
	private ArrayList<HashMap<String,String>> fileSys;
	// replacers:
	private HashMap<String,String> replacementValues;
	// sourceFolder reference
	private String sourceFolder;
	// template reference:
	private ImagePlus templateIm;
	
	// GUI stuff:
	private JPanel thisPane;
	public Dimension maxDim;
	public JPanel newSubPane;
	private JTabbedPane tabbedPane;
	
	// current neuron being viewed:
	private Neuron curNeuron;
	// Edditable (main) version or not?
	private boolean editor;
	// Original pre-text for directory of original images:
	private String origPre;
	// Blank image for the image lables when nothing can be found / no neuron selected.
	ImagePlus blankImage;
	
	// Tested, Reviewed 23/07/2016
	public NeuronViewer(String sourceFolder,ArrayList<HashMap<String,String>> fileSys,HashMap<String,String> replacementValues,ImagePlus templateImg,boolean editor,String origPre){
		//References:
		this.editor = editor;
		this.origPre = origPre;
		this.templateIm = templateImg;
		this.fileSys = fileSys;
		this.replacementValues = replacementValues;
		this.sourceFolder =sourceFolder;
		if (sourceFolder==null){
			sourceFolder = "";
		}
		// Instantiate Lists:
		this.openWindows = new ArrayList<ImageWindow>();
		this.openStacks = new ArrayList<ImagePlus>();
		this.origImages = new HashMap<String,HashMap<String,ImagePlus>>();
		// Other:
		this.blankImage = NewImage.createByteImage("Blank Image", 10, 10, 1,NewImage.FILL_WHITE);
		curNeuron = null;
		// Get lists:
		this.origFileList = EdRegViewer_.findFileNamesOfType(fileSys,EdRegViewer_.TYPE_ORIG);
		this.neuronFileList = EdRegViewer_.findFileNamesOfType(fileSys,EdRegViewer_.TYPE_NEURON);
		this.tabs = getListOfTabs();
		// ScrollFrame max dim:
		if (maxDim == null){
			maxDim = new Dimension(FRAMEX,FRAMEY);
		}
		makeGUI();
	}
	
	// Get the tabs from the tab structure and those that are in the neuron image "tabnames"...
	// THEN order them as expected (hence the long function).
	//Tested, Reviewed 23/07/2016
	public ArrayList<String> getListOfTabs(){
		//image-key to tab-key lists:
		this.origTabList = new HashMap<String,String>();
		this.neuronTabList = new HashMap<String,String>();
		ArrayList<String> returnL = new ArrayList<String>();
		HashMap<String,Integer> firstListTabs = new HashMap<String,Integer>();
		TreeMap<Integer,String> tempM = new TreeMap<Integer,String>();
		// Get the tabs:
		// we don't want to add loads at the MAX!
		int i = 0;
		for (HashMap<String,String> fileInfo : fileSys){
			// look in images to get their tabs FIRST:
			if (fileInfo.get(EdRegViewer_.TYPE).equals(EdRegViewer_.TYPE_ORIG)||
					fileInfo.get(EdRegViewer_.TYPE).equals(EdRegViewer_.TYPE_NEURON)){
				String thisTab;
				// If neuron has a tab name then get it, otherwise is default:
				if (fileInfo.containsKey(TABNAME)==true){
					thisTab = fileInfo.get(TABNAME);
				}
				else{
					thisTab = DEFAULT;
				}
				// now put this neuron in the correct tab for later:
				if (fileInfo.get(EdRegViewer_.TYPE).equals(EdRegViewer_.TYPE_ORIG)){
					origTabList.put(fileInfo.get(EdRegViewer_.NAME), thisTab);
				}
				else if (fileInfo.get(EdRegViewer_.TYPE).equals(EdRegViewer_.TYPE_NEURON)){
					neuronTabList.put(fileInfo.get(EdRegViewer_.NAME), thisTab) ;
				}
				// add it to overall tab list if required:
				if (firstListTabs.containsKey(thisTab)==false){
					firstListTabs.put(thisTab,MAXTABS+i);
					i++;
				}
			}
		}
		// Now find the tab details:
		for (HashMap<String,String> fileInfo : fileSys){
			if (fileInfo.get(EdRegViewer_.TYPE).equals(TYPE_TAB)){
				String thisTabKey = fileInfo.get(EdRegViewer_.NAME);
				int thisTabNum;
				if (fileInfo.containsKey(TABNUMBER)==false){
					thisTabNum = MAXTABS+i;
					i++;
				}
				else{
					thisTabNum = Integer.parseInt(fileInfo.get(TABNUMBER));
				}
				// will overwrite it necessary:
				firstListTabs.put(thisTabKey,thisTabNum);
			}
		}
		// now put them into a treemap to order them:
		for (String tabName: firstListTabs.keySet()){
			tempM.put(firstListTabs.get(tabName),tabName);
		}
		for (Integer indexx : tempM.keySet()){
			returnL.add(tempM.get(indexx));
		}
		return returnL;
	}
	
	//Tested, Reviewed 23/07/2016
	// Actually make the GUI with the tabs and the info. pane.
	public void makeGUI(){
		origFileLabels = new HashMap<String,JLabel>();
		neuronFileLabels = new HashMap<String,JLabel>();
		thisPane = new JPanel();
		thisPane.setLayout(new BorderLayout());
		tabbedPane = new JTabbedPane();
		for (String thisTab : tabs){
			Dimension maxDims = new Dimension(100,100);
			JPanel pane = new JPanel();
			pane.setLayout(null);
			ImageIcon icon = new ImageIcon();
			for (String thisFile : origFileList.keySet()){
				if (origTabList.get(thisFile).equals(thisTab)){
					JLabel toAdd = generateImageLabel(EdRegViewer_.TYPE_ORIG,thisFile);
					origFileLabels.put(thisFile,toAdd);
					pane.add(toAdd);
					maxDims = updateMaxDims(maxDims,toAdd);
				}
			}
			for (String thisFile : neuronFileList.keySet()){
				if (neuronTabList.get(thisFile).equals(thisTab)){
					JLabel toAdd = generateImageLabel(EdRegViewer_.TYPE_NEURON,thisFile);
					neuronFileLabels.put(thisFile,toAdd);
					pane.add(toAdd);
					maxDims = updateMaxDims(maxDims,toAdd);
				}
			}
			pane.setPreferredSize(maxDims);
			// Convert to a scroll pane:
			JScrollPane scrollPane = new JScrollPane(pane);
			scrollPane.setPreferredSize(maxDim);
			tabbedPane.addTab(getTabName(thisTab),icon,scrollPane,"Click to view");
		}
		thisPane.add(tabbedPane,BorderLayout.CENTER);
		newSubPane = new JPanel();
		newSubPane.setLayout(new BorderLayout());
		JPanel subSubPane = new JPanel();
		subSubPane.setLayout(new GridBagLayout());
		//newSubPane.setBounds(0,405,500,100);
		// And the rest of the text:
		generateText(subSubPane);
		newSubPane.add(subSubPane,BorderLayout.SOUTH);
		thisPane.add(newSubPane,BorderLayout.SOUTH);
	}
	
	
	//Tested, Reviewed 23/07/2016
	// Generate the images for orig and neurons in the tabbed compartments:
	private JLabel generateImageLabel(String type,String name){
		JLabel retLabel = new JLabel("");
		boolean doneIt = false;
		for (HashMap<String,String> fileInfo : fileSys){
			if (fileInfo.get(EdRegViewer_.TYPE).equals(type) && fileInfo.get(EdRegViewer_.NAME).equals(name)){
				retLabel = new JLabel();
				doneIt = true;
				retLabel.setText("");
				retLabel.setBounds(getIntValue(XPOS,fileInfo),
						getIntValue(YPOS,fileInfo),
						getIntValue(XDIM,fileInfo),
						getIntValue(YDIM,fileInfo));
				retLabel.setIcon(new ImageIcon(blankImage.getImage().getScaledInstance(retLabel.getBounds().width,retLabel.getBounds().height,  java.awt.Image.SCALE_SMOOTH)));
				retLabel.putClientProperty(EdRegViewer_.NAME,name);
				retLabel.putClientProperty(EdRegViewer_.TYPE,type);
				retLabel.addMouseListener(new MouseAdapter(){
				    @Override
				    public void mouseClicked(MouseEvent e){
				    	// Lazy as can't be bothered to create extended Jlabel class....
				    	JLabel source = (JLabel) e.getSource(); 
				    	String name = (String) source.getClientProperty(EdRegViewer_.NAME);
				    	String type = (String) source.getClientProperty(EdRegViewer_.TYPE);
				        if(e.getClickCount()==2){
				            openDoubleClick(type,name);
				        }
				    }
				});
			}
		}
		if (doneIt==false){
			IJ.log("Could not find JLabel info. for "+name);
		}
		return retLabel;
	}
	
	
	// Tested, Reviewed 23/07/2016
	// Get integer values from fileSys.
	private int getIntValue(String request,HashMap<String,String> thisFileInfo){
		int retInt;
		if (thisFileInfo.containsKey(request)){
			retInt = Integer.parseInt(thisFileInfo.get(request));
		}
		else{
			retInt = DEFAULTINT;
		}
		return retInt;
	}
	
	// Tested, Reviewed 23/07/2016
	// Opens a double-clicked on image given the parameters stored in the JLabel
	// Relies on the Current Neuron being set (note that is done in the update)!
	public void openDoubleClick(String type,String name){
		if (curNeuron != null){
			ImagePlus thisIP = blankImage;
			if (type.equals(EdRegViewer_.TYPE_NEURON)){
				thisIP = curNeuron.getImage(name);
			}
			else if (type.equals(EdRegViewer_.TYPE_ORIG))
				thisIP = origImages.get(curNeuron.getXMLData(EdRegViewer_.FOLDER)).get(name);
			if (thisIP != null){
				ImageWindow newWindow = new ImageWindow(thisIP);
				newWindow.setVisible(true);
				openWindows.add(newWindow);
			}
		}
	}
	
	// Tested, Reviewed 23/07/2016
	// Returns the tab name for the tab-key from the file sys:
	private String getTabName(String tabName){
		String returnS = tabName;
		for (HashMap<String,String> fileInfo : fileSys){
			if (fileInfo.get(EdRegViewer_.TYPE).equals(TYPE_TAB)&&fileInfo.get(EdRegViewer_.NAME).equals(tabName)){
				if (fileInfo.containsKey(TABTEXT)){
					returnS =  fileInfo.get(TABTEXT);
				}
			}
		}
		return returnS;
	}
	
	// Tested, Reviewed 23/07/2016
	// updates the maximum widths etc. for the panel so it scrolls:
	public Dimension updateMaxDims(Dimension oldDim,JLabel input){
		Rectangle thisRect = input.getBounds();
		double oldXMax = oldDim.getWidth();
		double oldYMax = oldDim.getHeight();
		double newXMax = thisRect.getWidth()+thisRect.getX()+INNERBORDER;
		double newYMax = thisRect.getHeight()+thisRect.getY()+INNERBORDER;
		if (newXMax>oldXMax){
			oldXMax = newXMax;
		}
		if (newYMax>oldYMax){
			oldYMax = newYMax;
		}
		return new Dimension((int)oldXMax,(int)oldYMax);
	}

	//Tested, Reviewed 23/07/2016
	// generate textfields and textlabels
	private void generateText(JPanel pane){
		this.textLabels = new HashMap<String,JLabel>();
		this.textLabelsText = new HashMap<String,String>();
		this.textFields = new HashMap<String,JTextField>();
		ArrayList<HashMap<String,String>> fileInfoSorter = new ArrayList<HashMap<String,String>>();
		for (HashMap<String,String> fileInfo : fileSys){
			if (fileInfo.get(EdRegViewer_.TYPE).equals(EdRegViewer_.TYPE_TEXT)||fileInfo.get(EdRegViewer_.TYPE).equals(EdRegViewer_.TYPE_TEXTFIELD)){
				fileInfoSorter.add(fileInfo);
			}
		}
		// Sort them:
		Collections.sort(fileInfoSorter, new Comparator<HashMap<String,String>>() {
	        public int compare(HashMap<String,String> fruit2, HashMap<String,String> fruit1)
	        {

	            return  getPosition(fruit2).compareTo(getPosition(fruit1));
	        }
	    });
		for (HashMap<String,String> fileInfo : fileInfoSorter){
			if (fileInfo.get(EdRegViewer_.TYPE).equals(EdRegViewer_.TYPE_TEXT)){
				generateTextLabel(pane,fileInfo);
			}
			if (fileInfo.get(EdRegViewer_.TYPE).equals(EdRegViewer_.TYPE_TEXTFIELD)){
				generateTextField(pane,fileInfo);
			}
		}
	}
	
	//Tested, Reviewed 23/07/2016
	// Just return an Integer Object of the position if it exists:
	public static Integer getPosition(HashMap<String,String> mapIn){
		if (mapIn.containsKey(POSITIONORDER)){
			return Integer.parseInt(mapIn.get(POSITIONORDER));
		}
		else{
			return DEFAULTPOSITION;
		}
	}
	
	// Tested, Reviewed 23/07/2016
	// Get the text labels:
	private void generateTextLabel(JPanel pane,HashMap<String,String> fileInfo){
		String textToSave = "No text!";
		JLabel retLabel = new JLabel();
		retLabel.setBounds(getIntValue(XPOS,fileInfo),
				getIntValue(YPOS,fileInfo),
				getIntValue(XDIM,fileInfo),
				getIntValue(YDIM,fileInfo));
		retLabel.setText("Value not set");
		if (fileInfo.containsKey(TEXTVALUE)==true){
			textToSave = fileInfo.get(TEXTVALUE);
		}
		this.textLabelsText.put(fileInfo.get(EdRegViewer_.NAME),textToSave);
		this.textLabels.put(fileInfo.get(EdRegViewer_.NAME),retLabel);
		pane.add(retLabel,getGridBagConstraints(fileInfo));
	}
	
	// Tested, Reviewed 23/07/2016
	// Get the text fields:
	private void generateTextField(JPanel pane,HashMap<String,String> fileInfo){
		//JPanel sub3Panel = new JPanel();
		JTextField retTF = new JTextField();
		retTF.setBounds(getIntValue(XPOS,fileInfo),
				getIntValue(YPOS,fileInfo),
				getIntValue(XDIM,fileInfo),
				getIntValue(YDIM,fileInfo));
		retTF.setText("");
		retTF.setColumns(getIntValue(COLS,fileInfo));
		this.textFields.put(fileInfo.get(EdRegViewer_.NAME),retTF);
		if (editor==false){
			retTF.setEditable(false);
		}
		//sub3Panel.add(retTF);
		pane.add(retTF,getGridBagConstraints(fileInfo));
	}
	
	// Parse gridbag constraints:
	public static GridBagConstraints getGridBagConstraints(HashMap<String,String> fileInfo){
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		if (fileInfo.containsKey(WEIGHTX)){
			c.weightx = Double.parseDouble(fileInfo.get(WEIGHTX));
		}
		if (fileInfo.containsKey(GRIDX)){
			c.gridx = Integer.parseInt(fileInfo.get(GRIDX));
		}
		if (fileInfo.containsKey(GRIDY)){
			c.gridy = Integer.parseInt(fileInfo.get(GRIDY));
		}
		if (fileInfo.containsKey(GRIDWIDTH)){
			c.gridwidth = Integer.parseInt(fileInfo.get(GRIDWIDTH));
		}
		return c;
	}

	//Tested, Reviewed 23/07/2016
	// Update the Data for the closing neuron and load the new data...
	// Consider only calling this if the selected neuron actually changes or has data in it changed!
	public void update(Neuron newNeuron){
		closeCurrentNeuron();
		curNeuron = newNeuron;
		// go through all orig images:
		for (String thisKey : origFileLabels.keySet()){
			ImagePlus newImg = null;
			if (curNeuron!=null){
				newImg = origImages.get(newNeuron.getXMLData(Neuron.FOLDER)).get(thisKey);
			}
			updateImage(origFileLabels.get(thisKey),newImg);
		}
		// go through all neuron images:
		for (String thisKey : neuronFileLabels.keySet()){
			ImagePlus newImg = null;
			if (curNeuron!=null){
				newImg = newNeuron.getImage(thisKey);
			}
			updateImage(neuronFileLabels.get(thisKey),newImg);
		}
		// and update the text:
		for (String thisKey : textLabels.keySet()){
			textLabels.get(thisKey).setText(EdRegViewer_.doReplacement(textLabelsText.get(thisKey), replacementValues, newNeuron));
		}
		// and update the textFields:
		for (String thisKey : textFields.keySet()){
			String newText;
			if (curNeuron!=null){
				newText = newNeuron.getXMLData(thisKey);
			}
			else{
				newText = "";
			}
			textFields.get(thisKey).setText(newText);
		}
		newSubPane.repaint();
	}
	
	//Tested, Reviewed 23/07/2016
	// copy over the textfields as required.
	public void closeCurrentNeuron(){
		if (curNeuron!=null && editor==true){
			for (String thisKey : textFields.keySet()){
				curNeuron.setXMLData(thisKey,textFields.get(thisKey).getText());
			}
		}
	}
	
	//Tested, Reviewed 23/07/2016
	public void updateImage(JLabel labelToUpdate,ImagePlus newImage){
		if (newImage==null){
			newImage = blankImage;
		}
		labelToUpdate.setIcon(new ImageIcon(newImage.getImage().getScaledInstance(labelToUpdate.getBounds().width,labelToUpdate.getBounds().height ,  java.awt.Image.SCALE_SMOOTH)));
	}	
	
	//Tested, Reviewed 23/07/2016
	// Get the original images if they are needed to be got:
	// This is ran externally to save doing this again if a new viewer is instantiated!
	public void getOrigImages(Neuron inNeuron){
		IJ.log("Running on "+inNeuron.toString());
		String folder = inNeuron.getXMLData(EdRegViewer_.FOLDER);
		boolean continueThis = true;
		String thisFolder = "VOID";
		if (folder!=Neuron.UNASSIGNED){
			thisFolder = folder;
		}
		else{
			IJ.log("No folder reference!");
			continueThis = false;
		}
		if (continueThis==true){
			if (origImages.containsKey(thisFolder)==false){
				HashMap<String,ImagePlus> toSave = new HashMap<String,ImagePlus>();
				for (String key :origFileList.keySet()){
					EdRegViewer_.openImage(key,origFileList.get(key),inNeuron,toSave);
				}
				origImages.put(thisFolder,toSave);
			}
			
		}
	}
	
	//Tested, Reviewed 23/07/2016
	public void openRegViewer(Neuron neuronIN){
		openStacks.add(RegStackViewer.getRegStack(neuronIN, sourceFolder, templateIm,replacementValues));
	}
	//Tested, Reviewed 23/07/2016
	public void openOrigStack(Neuron neuronIN){
		File thisFilePath = new File(origPre,neuronIN.getXMLData(Neuron.ORIGFOLDER));
		thisFilePath = new File(thisFilePath,neuronIN.getXMLData(Neuron.ORIGFILE));
		ImagePlus neuronOrig = EdRegViewer_.loadImage(thisFilePath);
		neuronOrig.show();
		openStacks.add(neuronOrig);
	}
	//Tested, Reviewed 23/07/2016
	public JPanel getJPanel(){
		return thisPane;
	}
	//Tested, Reviewed 23/07/2016
	public JPanel getInfoPanel(){
		return newSubPane;
	}
	//Tested, Reviewed 23/07/2016
	public ArrayList<ImagePlus> getOpenStackList(){
		return openStacks;
	}
	//Tested, Reviewed 23/07/2016
	public ArrayList<ImageWindow> getOpenWindows(){
		return openWindows;
	}
	//Tested, Reviewed 23/07/2016
	public HashMap<String, HashMap<String, ImagePlus>> getOrigImageList(){
		return origImages;
	}
	//Tested, Reviewed 23/07/2016
	public void setOrigImageList(HashMap<String, HashMap<String, ImagePlus>> inImg){
		origImages = inImg;
	}
	
}
