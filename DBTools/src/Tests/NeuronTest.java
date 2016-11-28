import static org.junit.Assert.*;

import java.io.FileOutputStream;

import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Test;

import org.w3c.dom.Element;
import org.w3c.dom.Document;


public class NeuronTest {
	
	@Test
	public void test() {
		String xmlFile = "/media/s1144899/JaneliaBlue/Senders/Test2/Test.xml";
		try{
			HashMap <String,String> replacerTest = new HashMap <String,String> ();
			replacerTest.put("**folder**","folder");
			replacerTest.put("**unid**","unid");
			replacerTest.put("**uid**","uid");
			String testSourceFolder = "/media/s1144899/JaneliaBlue/Senders/Test2/";
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document testDocument = docBuilder.newDocument();
			Element rootElement = testDocument.createElement("root");
			testDocument.appendChild(rootElement);
			HashMap<String,String> testFileSys = new HashMap<String,String>();
			testFileSys.put("regxproj", "**unid**RegProjX.tif");
			testFileSys.put("regyproj", "**unid**RegProjY.tif");
			testFileSys.put("regzproj", "**unid**RegProjZ.tif");
			testFileSys.put("scorexproj", "**unid**ScoreProjX.tif");
			testFileSys.put("scoreyproj", "**unid**ScoreProjY.tif");
			testFileSys.put("scorezproj", "**unid**ScoreProjZ.tif");
			Element testElement = testDocument.createElement("neuron");
			testElement.setAttribute("folder","5000002");
			testElement.setAttribute("stackFolderSource","/home/s1144899/SSH/kenoResults/Scored/5000002");
			testElement.setAttribute("origFile","13A03_57C10wtFlp_A_Br.tif");
			testElement.setAttribute("origFolder","/home/s1144899/SSH/keno/Lines/GMR_01-20/13A03/Confocal_63x_2011-11-02_13A03_Br/stack_TIFF");
			testElement.setAttribute("gmrLine","13A03");
			testElement.setAttribute("neuronKey","1");
			testElement.setAttribute("singleScore","0.4331");
			testElement.setAttribute("cOcM","617.0,355.0,268.0");
			testElement.setAttribute("channel","G");
			testElement.setAttribute("vol", "244413");
			testElement.setAttribute("inVol","229151");
			testElement.setAttribute("uid","5000002-G");
			testElement.setAttribute("unid","5000002-G-1");
		    Neuron testNeuron = new Neuron(testElement,1,testSourceFolder,testFileSys,replacerTest);
		    testNeuron.setXMLData("folder", "40");
		    testNeuron.setXMLData("notes", "yo du\"des!");
		    testNeuron.setXMLData("wordup", "yo dud");
			testNeuron.updateXMLElement();
			assertEquals("5000002-G-1",testNeuron.toString());
			testNeuron.setXMLData("bestInClass", "true");
			System.out.println(testNeuron.toString());
			assertEquals(" * 5000002-G-1",testNeuron.toString());
			assertEquals("unassigned",testNeuron.getXMLData("ddd"));
			// Ground truth?
			Transformer tr = TransformerFactory.newInstance().newTransformer();
		    tr.setOutputProperty(OutputKeys.INDENT, "yes");
		    tr.setOutputProperty(OutputKeys.METHOD, "xml");
		    tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		    tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		    tr.transform(new DOMSource(testDocument),new StreamResult(new FileOutputStream(xmlFile)));
		}
		catch(Exception e){
			System.out.println("Error");
			e.printStackTrace();
		}
	}

}
