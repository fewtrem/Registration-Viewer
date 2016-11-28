package RegViewer;
import ij.IJ;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import org.w3c.dom.Element;
import org.w3c.dom.Document;

public class NeuronViewerOnlyTest {
	JFrame testProg;
    String newNeuronName = "Lou papier neuron";
    String newNeuronName2 = "bog roll neuron";
    String newNeuronName3 = "toilet neuron";
    String newNeuronName4 = "mr blobby neuron";
    NeuronViewer nv;
    NeuronTypeList testNTL;
	ArrayList<Neuron> neuronList;
	public static void main(String[] args){
		NeuronViewerOnlyTest go = new NeuronViewerOnlyTest();
		System.out.println(go.hashCode());
	}
	public NeuronViewerOnlyTest(){
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
			HashMap<Integer,Neuron> alphaNeuronList = new HashMap<Integer,Neuron>();
			alphaNeuronList.put(1,new Neuron(testElement,1,testSourceFolder,testFileSysA,replacerTest));
			nv = new NeuronViewer(testSourceFolder,filesys,replacerTest,null,false,"/home/s1144899/SSH/keno/");
			nv.getOrigImages(alphaNeuronList.get(1));
			nv.makeGUI();
			testProg = new JFrame("Test Prog");
			//JScrollPane scrollpane = new JScrollPane();
			//scrollpane.add();
		    Button upN = new Button("updateNeurons");
		    upN.setBounds(200,200,100,10);
		    upN.addActionListener (new ActionListener(){
		    	public void actionPerformed(ActionEvent e){
		    		update();
		    	}
		    });
			testProg.add(nv.getJPanel());
			nv.getJPanel().add(upN);
			testProg.setMinimumSize(new Dimension(700,700));
			testProg.setVisible(true);
			neuronList = new ArrayList<Neuron>();
			neuronList.add(new Neuron(testElement,0,testSourceFolder,testFileSysA,replacerTest));
			
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
