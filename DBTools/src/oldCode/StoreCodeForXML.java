package oldCode;
import java.awt.TextField;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

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


public class StoreCodeForXML {
	static final String outputEncoding = "UTF-8";
	String xmlFile;
	TextField Owey;
	Document doc;
	Element curElement;
	String sourceFolder;
	String folderN;
	String fileN;
	String fileN2;

	public static void main(String[] args){
		StoreCodeForXML instance = new StoreCodeForXML();
		instance.run();
		
	}
	public void run() {
		try{
			OutputStreamWriter errorWriter = new OutputStreamWriter(System.err,
	                outputEncoding);
			//String pathToOpen = new OpenDialog("Select the xml document").getPath();
			String pathToOpen = "/afs/inf.ed.ac.uk/user/s11/s1144899/PhD/Python Projects/DBTools/src/index.xml";
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			File dbFile = new File(pathToOpen);
			xmlFile = pathToOpen;
			doc = db.parse(dbFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("entry");
			System.out.println(Integer.toString(nList.getLength()));
			sourceFolder = dbFile.getParent();
			for (int temp = 0; temp < nList.getLength(); temp++) {
	            Node nNode = nList.item(temp);
	            //IJ.log("\nCurrent Element :"
	            //   + nNode.getNodeName());
	            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	               Element eElement = (Element) nNode;
	               curElement = eElement;
	               NamedNodeMap nnm = eElement.getAttributes();
	               if (nnm != null)
	               {
	                  for (int i=0; i<nnm.getLength(); i++)
	                  {
	                     Node n = nnm.item(i);
	                     System.out.println("Entry " + n.getNodeName() + " = " + n.getNodeValue());
	                  }
	               }
	               NodeList big2 = nNode.getChildNodes();
	               for (int temp2 = 0; temp2 < big2.getLength(); temp2++) {
	            	   Node nNode2 = big2.item(temp2);
	            	   if (nNode2.getNodeName()=="folder"){
	            		   folderN = nNode2.getTextContent();
	            	   }
	            	   if (nNode2.getNodeName()=="file"){
	            		   fileN = nNode2.getTextContent();
	            	   }
	            	   if (nNode2.getNodeName()=="file2"){
	            		   fileN2 = nNode2.getTextContent();
	            	   }
	               }
	            }
			}
		}
		catch (Exception e){
			System.out.println("Error: "+e.getMessage());
		}
		saveDOM();
	}
	//SAVING
	public void saveDOM(){
	    try {
	        Transformer tr = TransformerFactory.newInstance().newTransformer();
	        tr.setOutputProperty(OutputKeys.INDENT, "yes");
	        tr.setOutputProperty(OutputKeys.METHOD, "xml");
	        tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	        curElement.setAttribute("dinosaur", "t-riex");
	        Node nodeToAdd = doc.createTextNode("ADDED");
	        Element subElement = doc.createElement("Gary");
	        subElement.appendChild(nodeToAdd);
	        curElement.appendChild(subElement);
	        // send DOM to file
	        tr.transform(new DOMSource(doc), 
	                             new StreamResult(new FileOutputStream(xmlFile)));
	
	    } catch (TransformerException te) {
	        System.out.println(te.getMessage());
	    } catch (IOException ioe) {
	        System.out.println(ioe.getMessage());
	    }
	}
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
}