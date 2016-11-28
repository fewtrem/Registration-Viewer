import ij.IJ;


import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class NeuronTypeList {
	// Settable input references:
	private ArrayList<Neuron> neuronList;
	// is part of an editor window or not (for buttons)?
	private boolean editor;
	// This class JLists and buttons:
	private JList<String> ourJList;
	private JList<Neuron> exampleList;
	private ArrayList<JButton> buttonList;
	// List to store the neuronList position in the exampleList:
	// It is crucial that this is kept up to date with exampleList!
	//private ArrayList<Neuron> exampleListCorrespondingNeurons;
	private ArrayList<String> JListCorrespondingKeys;
	// Something to store the list of neurons
	private HashMap<String,Integer> mapOfNeurons;
	// The associated viewer (for updating it!)
	private NeuronViewer theViewer;
	// Unfortunate (for testing) overall program reference for setting disabled when editing and also for updating other viewers.
	private EdRegViewer_ theOverallProg;
	// GUI stuff:
	private JFrame dialog;
	private JList<String> chooseAValue;
	// for dialog box, neuron store ONLY:
	private Neuron neuronAct;
	// Store the last selected neuron so when updating it will go to this neuron if there was nothing selected before IF it is still there:
	private Neuron lastSelectedNeuron;
	private String lastSelectedJList;
	private JPanel newButtonPanel;

	// Tested, Reviewed 23/07/2016
	// get the names of the neurons initially and when a neuron is updated as this changes the counters..)
	public NeuronTypeList(ArrayList<Neuron> neuronListIn, boolean editor){
		// References:
		this.editor = editor;
		this.neuronList = neuronListIn;
		// Lists:
		mapOfNeurons = new HashMap<String,Integer>();
		//exampleListCorrespondingNeurons = new ArrayList<Neuron>();
		JListCorrespondingKeys = new ArrayList<String>();
		ourJList = new JList<String>();
		exampleList = new JList<Neuron>();
		// Properties of JLists:
		ourJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// Note, might still have nothing selected!
		exampleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// Note, might still have nothing selected!
		exampleList.setCellRenderer(new ListCellRenderer<Neuron>(){
			public Component getListCellRendererComponent(JList<? extends Neuron> list, Neuron neur, int index,
			        boolean isSelected, boolean cellHasFocus) {
				JLabel toRet = new JLabel(neur.toString());
				toRet.setOpaque(true);
				if (isSelected==false){
					if (neur.getXMLData(Neuron.APPROVED).equals(Neuron.TRUE)){
						toRet.setBackground(new Color(200,255,200));
					}
					else{
						toRet.setBackground(new Color(255,200,200));
					}
				}
				else{
					if (neur.getXMLData(Neuron.APPROVED).equals(Neuron.TRUE)){
						toRet.setBackground(new Color(100,255,100));
					}
					else{
						toRet.setBackground(new Color(255,100,100));
					}
				}
				return toRet;
			}
		});
		ourJList.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e){
				updateExampleList(true);
			}
		});
		exampleList.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e){
				updateViewer();
			}
		});
		generateButtons();
		updateNeurons();
	}
	
	// Tested, Reviewed 23/07/2016
	// Generate buttons to do stuff on the neurons and be placed in the viewer.
	// This is here as lots of stuff from the buttons happens on the lists and we don't want to lose empty neuron classes if the lists were emptied...
	// Considering changing it though.
	public JPanel generateButtons(){
		this.buttonList = new ArrayList<JButton>();
		newButtonPanel = new JPanel();
		if (editor==true){
			// Set approved button:
			JButton approveB = new JButton("Approve");
			approveB.addActionListener (new ActionListener(){
		    	public void actionPerformed(ActionEvent e){
		    		approveTheNeuron();
		    	}
		    });
			newButtonPanel.add(approveB);
			this.buttonList.add(approveB);
			JButton unapproveB = new JButton("Unapprove");
			unapproveB.addActionListener (new ActionListener(){
		    	public void actionPerformed(ActionEvent e){
		    		unapproveTheNeuron();
		    	}
		    });
			newButtonPanel.add(unapproveB);
			this.buttonList.add(unapproveB);
			// changeDBName button:
			JButton changeDBNameB = new JButton("Change class");
			changeDBNameB.addActionListener (new ActionListener(){
		    	public void actionPerformed(ActionEvent e){
		    		changeTheDBNameDialog();
		    	}
		    });
			newButtonPanel.add(changeDBNameB);
			this.buttonList.add(changeDBNameB);
			// set best in class button:
			JButton setBestInClassB = new JButton("Set best in class");
			setBestInClassB.addActionListener (new ActionListener(){
		    	public void actionPerformed(ActionEvent e){
		    		makeBestInClass();
		    	}
		    });
			newButtonPanel.add(setBestInClassB);
			this.buttonList.add(setBestInClassB);
			// set best in class button:
			JButton setNotBestInClassB = new JButton("Set NOT best in class");
			setNotBestInClassB.addActionListener (new ActionListener(){
		    	public void actionPerformed(ActionEvent e){
		    		makeNotBestInClass();
		    	}
		    });
			newButtonPanel.add(setNotBestInClassB);
			this.buttonList.add(setNotBestInClassB);
		}
		// open registered stack button:
		JButton openRegisteredStackB = new JButton("Open registered stack");
		openRegisteredStackB.addActionListener (new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		openRegStack();
	    	}
	    });
		newButtonPanel.add(openRegisteredStackB);
		this.buttonList.add(openRegisteredStackB);
		// open original stack button:
		JButton openOriginalStack = new JButton("Open original stack");
		openOriginalStack.addActionListener (new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		openOrigStack();
	    	}
	    });
		newButtonPanel.add(openOriginalStack);
		this.buttonList.add(openOriginalStack);
		return newButtonPanel;
	}
	
	// Tested, Reviewed 23/07/2016
	// Change the DB Name - super long dialog box...hmmm
	// but make sure that it is from the list and the selected neuron changes when it is selected...
	public void changeTheDBNameDialog(){
		if (exampleList.isSelectionEmpty()==true){
			IJ.log("Please select a neuron first!");
		}
		else{
			if (theOverallProg!=null){
				theOverallProg.setEnabledA(false);
			}
			Neuron neuronIN = exampleList.getSelectedValue();//exampleListCorrespondingNeurons.get(exampleList.getSelectedIndex());
			// list of possible new values:
			ArrayList<String> invalues = new ArrayList<String>();
			// Ensure an option is "unassigned":
			if (mapOfNeurons.containsKey(Neuron.UNASSIGNED)==false){
				invalues.add(Neuron.UNASSIGNED);
			}
			// recall mapOfNeurons is counter so keyset is name field:
			invalues.addAll(mapOfNeurons.keySet());
			// Jlist:
			chooseAValue = new JList<String>(invalues.toArray(new String[invalues.size()]));
			chooseAValue.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			// this should NOT happen, but....
			if (invalues.contains(neuronIN.getXMLData(Neuron.NAMEDB))==false){
				IJ.log("Error - neuron has type not in list(!?!)");
			}
			// match up values:
			else{
				int selectIndex = invalues.indexOf(neuronIN.getXMLData(Neuron.NAMEDB));
				chooseAValue.setSelectedIndex(selectIndex);
			}
			// full GUI
			dialog = new JFrame("Change neuron class");
			dialog.setLayout(new BoxLayout(dialog.getContentPane(),BoxLayout.Y_AXIS));
			JLabel toAddL = new JLabel("Please select the the neuron class for "+neuronIN.toString()+":");
			JLabel warning = new JLabel("WARNING: This neuron will lose any best in class designation on changing the class.");
			toAddL.setAlignmentX(Component.CENTER_ALIGNMENT);
			warning.setAlignmentX(Component.CENTER_ALIGNMENT);
			dialog.add(toAddL);
			dialog.add(warning);
			chooseAValue.setAlignmentX(Component.CENTER_ALIGNMENT);
			dialog.add(chooseAValue);
			JButton OKButton = new JButton("OK");
			neuronAct = neuronIN;
			OKButton.addActionListener (new ActionListener(){
		    	public void actionPerformed(ActionEvent e){
		    		changeTheDBName(neuronAct,(String)chooseAValue.getSelectedValue());
		    		dialog.setVisible(false);
		    		dialog.dispose();
					if (theOverallProg!=null){
						theOverallProg.setEnabledA(true);
					}
					updateAllViewers();
		    	}
		    });
			dialog.addWindowListener(new java.awt.event.WindowAdapter() {
			    @Override
			    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
			    	dialog.dispose();
					if (theOverallProg!=null){
						theOverallProg.setEnabled(true);
					}
			    }
			});
			OKButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			dialog.add(OKButton);
			dialog.pack();
			dialog.setVisible(true);
			updateAllViewers();
		}
	}
	
	// Tested, Reviewed 23/07/2016
	//the ability to choose the neuronDBName,
	public void changeTheDBName(Neuron neuronIN,String newValue){
		if (newValue != null){
			String oldValue = neuronIN.getXMLData(Neuron.NAMEDB);
			if (mapOfNeurons.containsKey(newValue)==false){
				addNeuronClass(newValue);
			}
			neuronIN.setXMLData(Neuron.NAMEDB,newValue);
			// remove Best in class designation:
			if (newValue.equals(oldValue)==false){
				neuronIN.setXMLData(Neuron.BESTINCLASS, Neuron.FALSE);
			}
			// update the lists:
			updateCounts();
			
			// Now set the selected value as the new one:
			for (int i=0;i<ourJList.getModel().getSize();i++){
				if (JListCorrespondingKeys.get(i).equals(newValue)){
					ourJList.setSelectedIndex(i);
				}
			}
			for (int i=0;i<exampleList.getModel().getSize();i++){
				if (exampleList.getModel().getElementAt(i).equals(neuronIN)){
				//if (exampleListCorrespondingNeurons.get(i).equals(neuronIN)){
					exampleList.setSelectedIndex(i);
				}
			}
			updateAllViewers();
		}	
	}
	
	// Tested, Reviewed 26/07/2016
	public void approveTheNeuron(){
		if (exampleList.isSelectionEmpty()==true){
			IJ.log("Please select a neuron first!");
		}
		else{
			Neuron neuronIN = exampleList.getSelectedValue();//exampleListCorrespondingNeurons.get(exampleList.getSelectedIndex());
			neuronIN.setXMLData(Neuron.APPROVED, Neuron.TRUE);
			updateExampleList(true);
			updateAllViewers();
		}

	}
	
	// Tested, Reviewed 26/07/2016
	public void unapproveTheNeuron(){
		if (exampleList.isSelectionEmpty()==true){
			IJ.log("Please select a neuron first!");
		}
		else{
			Neuron neuronIN = exampleList.getSelectedValue();//exampleListCorrespondingNeurons.get(exampleList.getSelectedIndex());
			neuronIN.setXMLData(Neuron.APPROVED, Neuron.FALSE);
			updateExampleList(true);
			updateAllViewers();
		}
	}
	
	// Tested, Reviewed 23/07/2016
	public void makeBestInClass(){
		if (exampleList.isSelectionEmpty()==true){
			IJ.log("Please select a neuron first!");
		}
		else{
			Neuron neuronIN = exampleList.getSelectedValue();//exampleListCorrespondingNeurons.get(exampleList.getSelectedIndex());
			for (Neuron thisNeuron : neuronList){
				if (thisNeuron.getXMLData(Neuron.NAMEDB).equals(neuronIN.getXMLData(Neuron.NAMEDB))){
					thisNeuron.setXMLData(Neuron.BESTINCLASS, Neuron.FALSE);
				}
			}
			neuronIN.setXMLData(Neuron.BESTINCLASS, Neuron.TRUE);
			updateExampleList(true);
			updateAllViewers();
		}

	}
	
	// Tested, Reviewed 23/07/2016
	public void makeNotBestInClass(){
		if (exampleList.isSelectionEmpty()==true){
			IJ.log("Please select a neuron first!");
		}
		else{
			Neuron neuronIN = exampleList.getSelectedValue();//exampleListCorrespondingNeurons.get(exampleList.getSelectedIndex());
			neuronIN.setXMLData(Neuron.BESTINCLASS, Neuron.FALSE);
			updateExampleList(true);
			updateAllViewers();
		}
	}
	
	// Tested, Reviewed 23/07/2016
	public void openRegStack(){
		if (exampleList.isSelectionEmpty()==true){
			IJ.log("Please select a neuron first!");
		}
		else{
			Neuron neuronIN = exampleList.getSelectedValue();//exampleListCorrespondingNeurons.get(exampleList.getSelectedIndex());
			theViewer.openRegViewer(neuronIN);
		}
	}
	
	// Tested, Reviewed 23/07/2016
	public void openOrigStack(){
		if (exampleList.isSelectionEmpty()==true){
			IJ.log("Please select a golly neuron first!");
		}
		else{
			Neuron neuronIN = exampleList.getSelectedValue();//exampleListCorrespondingNeurons.get(exampleList.getSelectedIndex());
			theViewer.openOrigStack(neuronIN);
		}
	}
	
	// Tested, Reviewed 23/07/2016
	// update the List of neurons from those in the NeuronList IN ORDER:
	// note neurons cannot be added / removed so order should be fixed by XML:
	public void updateNeurons(){
		for (int i=0;i<neuronList.size();i++){
			Neuron thisNeuron = neuronList.get(i);
			mapOfNeurons.put(thisNeuron.getXMLData(Neuron.NAMEDB), 0);
		}
		updateCounts();
	}
	
	// Tested, Reviewed 23/07/2016
	// update the counters of the neurons based on those in the database
	public void updateCounts(){
		// Note that it will not remove the old ones!  Intentional!
		for (String thisKey : mapOfNeurons.keySet()){
			mapOfNeurons.put(thisKey,0);
		}
		for (Neuron thisNeuron : neuronList){
			mapOfNeurons.put(thisNeuron.getXMLData(Neuron.NAMEDB),mapOfNeurons.get(thisNeuron.getXMLData(Neuron.NAMEDB))+1);
		}
		updateJList(false);
	}
	
	// Tested, Reviewed 23/07/2016
	// update the viewer to the currently selected neuron:
	public void updateViewer(){
		if (theViewer == null){
			IJ.log("No viewer initialised");
		}
		else{
			Neuron neuronIN = null;
			if (exampleList.isSelectionEmpty()==true){
				// grey out the buttons
				//newButtonPanel.setEnabled(false);
				
				for (JButton thisButton : buttonList){
					thisButton.setEnabled(false);
				}
			}
			else{
				neuronIN = exampleList.getSelectedValue();//exampleListCorrespondingNeurons.get(exampleList.getSelectedIndex());
				// grey in the buttons
				//newButtonPanel.setEnabled(true);
				
				for (JButton thisButton : buttonList){
					thisButton.setEnabled(true);
				}
			}
			theViewer.update(neuronIN);
			if (neuronIN!=null){
				lastSelectedJList = JListCorrespondingKeys.get(ourJList.getSelectedIndex());
				lastSelectedNeuron = neuronIN;
			}
		}
	}

	// Tested, Reviewed 23/07/2016
	// Add a neuron name to the list:
	public boolean addNeuronClass(String userInput){
		boolean addOK = true;
		// check it does not already exist:
		if (mapOfNeurons.containsKey(userInput)==false){
			mapOfNeurons.put(userInput,0);
			IJ.log("Added neuron class \""+userInput+"\"");
		}
		else{
			IJ.log("Neuron class \""+userInput+"\" already exists!");
			addOK = false;
		}
		updateJList(true);
		updateAllViewers();
		return addOK;
	}
	
	// Tested, Reviewed 23/07/2016
	// Delete a neuron class:
	public boolean deleteNeuronClass(String userInput){
		boolean deleteOK = false;
		if (mapOfNeurons.containsKey(userInput)==true){
			if (mapOfNeurons.get(userInput)!=0){
				IJ.log("Please empty this class first as it can only be deleted when it is empty (let me know if this is annoying)");
			}
			else{
				mapOfNeurons.remove(userInput);
				deleteOK = true;
				updateJList(false);
				IJ.log("Deleted neuron class \""+userInput+"\"");
			}
		}
		else{
			IJ.log("Neuron class \""+userInput+"\" does not exist!");
		}
		return deleteOK;
	}
	
	// Tested, Reviewed 23/07/2016
	// Delete a neuron from the list:
	public boolean deleteNeuronClassFromList(){
		boolean deleteOK = false;
		if (ourJList.isSelectionEmpty()==true){
			IJ.log("Select a neuron class first");
		}
		else{
			deleteOK = deleteNeuronClass(JListCorrespondingKeys.get(ourJList.getSelectedIndex()));
		}
		return deleteOK;
	}
	
	// Tested, Reviewed 24/07/2016
	// Desired behaviour:
	// maintain class
	// don't go to any class if not asked to (SetSelected = false).
	// allow it to be set
	// update thisList and the associated example list:
	public void updateJList(boolean setSelected){
		// Get current selected or specific target:
		
		String curSelectedClassKey=null;
		boolean nonSelected = ourJList.isSelectionEmpty();
		// We want to make sure that if it is still there we re-select it otherwise not.
		if (nonSelected==false){
			curSelectedClassKey = JListCorrespondingKeys.get(ourJList.getSelectedIndex());
		}
		else{
			curSelectedClassKey = lastSelectedJList; // Might still be null if first one!
		}
		int newSelectedIndex=-1;
		String[] keySet = mapOfNeurons.keySet().toArray(new String[mapOfNeurons.keySet().size()]);
		// Now let's generate the arraylist to use for the new values in the order that matches mapOfNeurons...
		this.JListCorrespondingKeys = new ArrayList<String>();
		ArrayList<String> toList = new ArrayList<String>();
		for (int i = 0;i<keySet.length;i++){
			String thisNeuronClassName = keySet[i];
			JListCorrespondingKeys.add(thisNeuronClassName);
			toList.add("["+Integer.toString(mapOfNeurons.get(thisNeuronClassName))+"] "+thisNeuronClassName);
			if (thisNeuronClassName.equals(curSelectedClassKey)){
				newSelectedIndex = i;
			}
		}
		// Set the data to the list:
		ourJList.setListData(toList.toArray(new String[toList.size()]));
		// reselect the previously selected value if appropriate:
		if (setSelected==true){
			if (newSelectedIndex != -1){
				try{
					ourJList.setSelectedIndex(newSelectedIndex);
				}
				catch(Exception e){
					IJ.log("Index not found for Class list, cannot set selected");
				}
			}
		}
		else{
			ourJList.clearSelection();
		}
		updateExampleList(setSelected);
	}
	
	// Tested, Reviewed 23/07/2016
	// Desired behaviour:
	// maintain neuron if new list has neuron in it.
	// go to last neuron if nothing was previously selected and it is visible,
	// don't to any neuron if not asked to (SetSelected=false)
	// allow it to be set if we change the class, for example, which will update the JList()...
	public void updateExampleList(boolean setSelected){
		// NOTE: getSelectedIndex returns -1 if nothing selected!
		// case none is selected?
		Neuron oldSelectedNeuron=null;
		boolean nonSelected = exampleList.isSelectionEmpty();
		if (nonSelected==false){
			oldSelectedNeuron = exampleList.getSelectedValue();//exampleListCorrespondingNeurons.get(exampleList.getSelectedIndex());
		}
		else{
			oldSelectedNeuron = lastSelectedNeuron; // Might still be null if first one!
		}
		//IJ.log("old Selected Neuron:"+oldSelectedNeuron.toString());
		int newSelectedIndex=-1;
		//exampleListCorrespondingNeurons.clear();
		// Build up the new list (if applicable):
		//sort by score, best in class designation etc.
		ArrayList<Neuron> toList = new ArrayList<Neuron>();
		ArrayList<Neuron> neuronListToSort = new ArrayList<Neuron>();
		if (ourJList.isSelectionEmpty()==false){
			for (Neuron thisNeuron : neuronList){
				if (thisNeuron.getXMLData(Neuron.NAMEDB).equals(JListCorrespondingKeys.get(ourJList.getSelectedIndex()))){
					neuronListToSort.add(thisNeuron);
					
				}
			}
			// sort the list:
			
			Collections.sort(neuronListToSort,new Comparator<Neuron>(){
			       public int compare(Neuron n2, Neuron n1)
			        {
			            return  n1.getScoreForList().compareTo(n2.getScoreForList());
			        }
				});
			int i = 0;
			for (Neuron thisNeuron : neuronListToSort){
				toList.add(thisNeuron);
				//exampleListCorrespondingNeurons.add(thisNeuron);
				// select it if the old selected neuron is still in the list:
				if (thisNeuron.equals(oldSelectedNeuron)){
					newSelectedIndex = i;
				}
				i++;
			}
		}
		// Set the selected indices:
		if (setSelected==true){
			exampleList.setListData(toList.toArray(new Neuron[toList.size()]));
			if (newSelectedIndex != -1){
				//IJ.log("new Selected index:"+newSelectedIndex);
				exampleList.setSelectedIndex(newSelectedIndex);
			}
			else if(exampleList.getModel().getSize()>0){
				exampleList.setSelectedIndex(0);
			}
		}
	}
	

	public void setEnabled(boolean enableOrNot){
		//ourJList.setEnabled(enableOrNot);
		//exampleList.setEnabled(enableOrNot);
		for (JButton but : buttonList){
			but.setEnabled(enableOrNot);
		}
	}
	
	// Tested, Reviewed 23/07/2016
	// update the other viewers in the program... slow, perhaps.
	public void updateAllViewers(){
		if (theOverallProg!=null){
			for (SuppViewer thisList : theOverallProg.suppFrames){
				thisList.ntl.updateNeurons();
				thisList.ntl.updateJList(true);
			}
		}
	}
	
	// Tested, Reviewed 23/07/2016
	public void setRegViewer(EdRegViewer_ inProg){
		theOverallProg = inProg;
	}
	
	// Tested, Reviewed 23/07/2016
	public JList<String> getMainList(){
		return ourJList;
	}
	
	// Tested, Reviewed 23/07/2016
	public JList<Neuron> getExampleList(){
		return exampleList;
	}
	
	// Tested, Reviewed 23/07/2016
	public void setViewer(NeuronViewer theViewer){
		this.theViewer = theViewer;
	}
}
	

	