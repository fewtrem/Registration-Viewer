package RegViewer;
import ij.IJ;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import org.w3c.dom.Element;
import org.w3c.dom.Document;

public class NeuronViewerListTest {
	JFrame testProg;
    String newNeuronName = "Lou papier neuron";
    String newNeuronName2 = "bog roll neuron";
    String newNeuronName3 = "toilet neuron";
    String newNeuronName4 = "mr blobby neuron";
    NeuronViewer nv;
    NeuronTypeList testNTL;
	ArrayList<Neuron> neuronList;
	public static void main(String[] args){
		NeuronViewerListTest go = new NeuronViewerListTest();
		System.out.println(go.hashCode());
	}
	public NeuronViewerListTest(){
		try{
			HashMap <String,String> replacerTest = new HashMap <String,String> ();
			replacerTest.put("**folder**","folder");
			replacerTest.put("**unid**","unid");
			replacerTest.put("**uid**","uid");
			String testSourceFolder="/media/s1144899/JaneliaBlue/Senders/Test2/";
			ArrayList<HashMap<String,String>> filesys = new ArrayList<HashMap<String,String>>();
			HashMap<String,String> testFileSys = new HashMap<String,String>();
			testFileSys.put("name", "regyproj");
			testFileSys.put("filename", "**unid**RegProjY.tif");
			testFileSys.put("type", "neuron");
			testFileSys.put("tabName", "neuron");
			testFileSys.put("type", "neuron");
			filesys.add(testFileSys);
			HashMap<String,String> testFileSys66 = new HashMap<String,String>();
			testFileSys66.put("name", "tab1");
			testFileSys66.put("tabNumber", "3");
			testFileSys66.put("tabName", "original");
			testFileSys66.put("type", "tab");
			filesys.add(testFileSys66);
			HashMap<String,String> testFileSys2 = new HashMap<String,String>();
			testFileSys2.put("name", "origzproj");
			testFileSys2.put("filename", "**folder**OrigProjZ.tif");
			testFileSys2.put("type", "original");
			//testFileSys2.put("tabName", "original");
			testFileSys2.put("xpos", "5");
			testFileSys2.put("ypos", "5");
			testFileSys2.put("xdim", "100");
			testFileSys2.put("ydim", "40");
			filesys.add(testFileSys2);
			HashMap<String,String> testFileSys3 = new HashMap<String,String>();
			testFileSys3.put("name", "gonna");
			testFileSys3.put("textValue", "**folder** Let's view it!OrigProjZ.tif");
			testFileSys3.put("type", "text");
			filesys.add(testFileSys3);
			HashMap<String,String> testFileSys4 = new HashMap<String,String>();
			testFileSys4.put("name", "notes");
			testFileSys4.put("text", "");
			testFileSys4.put("type", "textfield");
			testFileSys4.put("xpos", "5");
			testFileSys4.put("ypos", "5");
			testFileSys4.put("xdim", "100");
			testFileSys4.put("ydim", "40");
			filesys.add(testFileSys4);
			HashMap<String,String> testFileSysA = new HashMap<String,String>();
			testFileSysA.put("regxproj", "**unid**RegProjX.tif");
			testFileSysA.put("regyproj", "**unid**RegProjY.tif");
			testFileSysA.put("regzproj", "**unid**RegProjZ.tif");
			testFileSysA.put("scorexproj", "**unid**ScoreProjX.tif");
			testFileSysA.put("scoreyproj", "**unid**ScoreProjY.tif");
			testFileSysA.put("scorezproj", "**unid**ScoreProjZ.tif");
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
			testElement.setAttribute("nameDB","lou papier neuron");
			
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
			testElement2.setAttribute("nameDB","lou papier's exceedingly brilliant but very long named neuron has a really really long name!");
			
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
			
			ArrayList<Neuron> alphaNeuronList = new ArrayList<Neuron>();
			alphaNeuronList.add(new Neuron(testElement,1,testSourceFolder,testFileSysA,replacerTest));
			alphaNeuronList.add(new Neuron(testElement2,2,testSourceFolder,testFileSysA,replacerTest));
			alphaNeuronList.add(new Neuron(testElement3,3,testSourceFolder,testFileSysA,replacerTest));
			alphaNeuronList.add(new Neuron(testElement4,4,testSourceFolder,testFileSysA,replacerTest));
			nv = new NeuronViewer(testSourceFolder,filesys,replacerTest,null,false,"/home/s1144899/SSH/keno/");
			nv.getOrigImages(alphaNeuronList.get(0));
			nv.getOrigImages(alphaNeuronList.get(1));
			nv.getOrigImages(alphaNeuronList.get(2));
			nv.getOrigImages(alphaNeuronList.get(3));
			nv.makeGUI();
			testNTL = new NeuronTypeList(alphaNeuronList,false);
			testProg = new JFrame("Test Prog");
			testProg.setLayout(new BorderLayout());
		    JPanel pane = new JPanel();
		    pane.setLayout(new BorderLayout());
		    //pane.set(new Dimension(100,100));
		    testNTL.getMainList().setFixedCellWidth(200);
		    testNTL.getExampleList().setFixedCellWidth(200);
		    JScrollPane scrollPaneML = new JScrollPane(testNTL.getMainList());
		    //scrollPaneML.setPreferredSize(new Dimension(100,100));
		    JScrollPane scrollPaneEL = new JScrollPane(testNTL.getExampleList());
		    //scrollPaneEL.setPreferredSize(new Dimension(100,100));
		    pane.add(scrollPaneML,BorderLayout.CENTER);
		    pane.add(scrollPaneEL,BorderLayout.SOUTH);
		    testProg.add(pane,BorderLayout.WEST);
		    testNTL.setViewer(nv);
			testProg.add(nv.getJPanel(),BorderLayout.CENTER);
			testProg.setVisible(true);
			neuronList = new ArrayList<Neuron>();
			neuronList.add(new Neuron(testElement,0,testSourceFolder,testFileSysA,replacerTest));
			testProg.setMinimumSize(new Dimension(500,500));
			testProg.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		}
		catch(Exception e){
			System.out.println("Error");
			e.printStackTrace();
		}
		
	}
	void update(){
		IJ.log("Updating...");
		nv.update(neuronList.get(0));
	}
}
