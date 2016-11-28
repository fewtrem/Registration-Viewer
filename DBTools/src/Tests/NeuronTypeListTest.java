
import ij.IJ;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import org.w3c.dom.Element;
import org.w3c.dom.Document;

public class NeuronTypeListTest {
	JFrame testProg;
    String newNeuronName = "Lou papier neuron";
    String newNeuronName2 = "bog roll neuron";
    String newNeuronName3 = "toilet neuron";
    String newNeuronName4 = "mr blobby neuron";
    NeuronTypeList testNTL;
	ArrayList<Neuron> neuronList;
	public static void main(String[] args){
		NeuronTypeListTest go = new NeuronTypeListTest();
		System.out.println(go.hashCode());
	}
	public NeuronTypeListTest(){
		try{
			HashMap <String,String> replacerTest = new HashMap <String,String> ();
			replacerTest.put("**folder**","folder");
			replacerTest.put("**unid**","unid");
			replacerTest.put("**uid**","uid");
			String testSourceFolder="/media/s1144899/JaneliaBlue/Senders/Test2/";
			HashMap<String,String> testFileSys = new HashMap<String,String>();
			testFileSys.put("origxproj", "**folder**OrigProjX.tif");
			testFileSys.put("origyproj", "**folder**OrigProjY.tif");
			testFileSys.put("origzproj", "**folder**OrigProjZ.tif");
			testFileSys.put("regxproj", "**unid**RegProjX.tif");
			testFileSys.put("regyproj", "**unid**RegProjY.tif");
			testFileSys.put("regzproj", "**unid**RegProjZ.tif");
			testFileSys.put("scorexproj", "**unid**ScoreProjX.tif");
			testFileSys.put("scoreyproj", "**unid**ScoreProjY.tif");
			testFileSys.put("scorezproj", "**unid**ScoreProjZ.tif");
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document testDocument = docBuilder.newDocument();
			
			Element testElement = testDocument.createElement("neuron");
			testElement.setAttribute("folder","5000002");
			testElement.setAttribute("stackFolderSource","/home/s1144899/SSH/kenoResults/Scored/5000002");
			testElement.setAttribute("origFile","13A03_57C10wtFlp_A_Br.tif");
			testElement.setAttribute("origFolder","/home/s1144899/SSH/keno/Lines/GMR_01-20/13A03/Confocal_63x_2011-11-02_13A03_Br/stack_TIFF");
			testElement.setAttribute("gmrLine","13A03");
			testElement.setAttribute("neuronKey","1");
			testElement.setAttribute("singleScore","0.4331");
			testElement.setAttribute("cOM","617.0,355.0,268.0");
			testElement.setAttribute("channel","G");
			testElement.setAttribute("vol", "244413");
			testElement.setAttribute("inVol","229151");
			testElement.setAttribute("uid","5000002-G");
			testElement.setAttribute("unid","5000002-G-1");
			
			Element testElement2 = testDocument.createElement("neuron");
			testElement2.setAttribute("folder","5000002");
			testElement2.setAttribute("stackFolderSource","/home/s1144899/SSH/kenoResults/Scored/5000002");
			testElement2.setAttribute("origFile","13A03_57C10wtFlp_A_Br.tif");
			testElement2.setAttribute("origFolder","/home/s1144899/SSH/keno/Lines/GMR_01-20/13A03/Confocal_63x_2011-11-02_13A03_Br/stack_TIFF");
			testElement2.setAttribute("gmrLine","13A03");
			testElement2.setAttribute("neuronKey","2");
			testElement2.setAttribute("singleScore","0.531");
			testElement2.setAttribute("cOM","613.0,085.0,68.0");
			testElement2.setAttribute("channel","G");
			testElement2.setAttribute("vol", "244413");
			testElement2.setAttribute("inVol","229151");
			testElement2.setAttribute("uid","5000002-G");
			testElement2.setAttribute("unid","5000002-G-1");
			
			Element testElement3 = testDocument.createElement("neuron");
			testElement3.setAttribute("folder","5000001");
			testElement3.setAttribute("stackFolderSource","/home/s1144899/SSH/kenoResults/Scored/5000002");
			testElement3.setAttribute("origFile","13A03_57C10wtFlp_A_Br.tif");
			testElement3.setAttribute("origFolder","/home/s1144899/SSH/keno/Lines/GMR_01-20/13A03/Confocal_63x_2011-11-02_13A03_Br/stack_TIFF");
			testElement3.setAttribute("gmrLine","13A03");
			testElement3.setAttribute("neuronKey","1");
			testElement3.setAttribute("singleScore","0.4331");
			testElement3.setAttribute("cOM","617.0,355.0,268.0");
			testElement3.setAttribute("channel","G");
			testElement3.setAttribute("vol", "244413");
			testElement3.setAttribute("inVol","229151");
			testElement3.setAttribute("uid","5000001-G");
			testElement3.setAttribute("unid","5000001-G-1");
			
			Element testElement4 = testDocument.createElement("neuron");
			testElement4.setAttribute("folder","5000001");
			testElement4.setAttribute("stackFolderSource","/home/s1144899/SSH/kenoResults/Scored/5000002");
			testElement4.setAttribute("origFile","13A03_57C10wtFlp_A_Br.tif");
			testElement4.setAttribute("origFolder","/home/s1144899/SSH/keno/Lines/GMR_01-20/13A03/Confocal_63x_2011-11-02_13A03_Br/stack_TIFF");
			testElement4.setAttribute("gmrLine","13A03");
			testElement4.setAttribute("neuronKey","2");
			testElement4.setAttribute("singleScore","0.4331");
			testElement4.setAttribute("cOM","617.0,355.0,268.0");
			testElement4.setAttribute("channel","G");
			testElement4.setAttribute("vol", "244413");
			testElement4.setAttribute("inVol","229151");
			testElement4.setAttribute("uid","5000001-G");
			testElement4.setAttribute("unid","5000001-G-2");

			// Do the test code:
		    neuronList = new ArrayList<Neuron>();
		    neuronList.add(new Neuron(testElement,1,testSourceFolder,testFileSys,replacerTest));
		    neuronList.add(new Neuron(testElement2,2,testSourceFolder,testFileSys,replacerTest));
		    neuronList.add(new Neuron(testElement3,3,testSourceFolder,testFileSys,replacerTest));
		    neuronList.add(new Neuron(testElement4,4,testSourceFolder,testFileSys,replacerTest));
		    testNTL = new NeuronTypeList(neuronList,true);
		    
		    JPanel pane = new JPanel();
		    pane.add(testNTL.getMainList());
		    pane.add(testNTL.getExampleList());

		    testNTL.addNeuronClass(newNeuronName);
		    testNTL.addNeuronClass(newNeuronName2);
		    testNTL.addNeuronClass(newNeuronName3);
		    
		    testNTL.addNeuronClass(newNeuronName);
		    testNTL.updateCounts();
		    testNTL.deleteNeuronClass(newNeuronName3);
		    testNTL.deleteNeuronClass(newNeuronName4);
		    testNTL.makeBestInClass();
		    
		    Button upN = new Button("updateNeurons");
		    pane.add(upN);
		    upN.addActionListener (new ActionListener(){
		    	public void actionPerformed(ActionEvent e){
		    		updateNeurons();
		    	}
		    });
		    
		    String[] inputArray = {"A","B","C"}; 
		    JList<String> HowardsList = new JList<String>();
			HowardsList.setListData(inputArray);
			pane.add(HowardsList);
			
			
			
			testProg = new JFrame("Test Prog");
			JScrollPane scrollpane = new JScrollPane(pane);
			testProg.add(scrollpane);
			testProg.setMinimumSize(new Dimension(400,400));
		}
		catch(Exception e){
			System.out.println("Error");
			e.printStackTrace();
		}
		testProg.setVisible(true);
	}
	void updateNeurons(){
		IJ.log("Updating...");
		testNTL.changeTheDBName(neuronList.get(1), "GROVEShark");
		testNTL.changeTheDBName(neuronList.get(0), newNeuronName);
		//testNTL.makeBestInClass(neuronList.get(1));
	}
}
