package oldCode;
// DOM tools:
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException; 
import org.xml.sax.SAXParseException;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
public class ReadDom {
	static final String outputEncoding = "UTF-8";
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String filename = "/media/s1144899/JaneliaBlue/Senders/Test/index.xml";
		// TODO Auto-generated method stub
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		// Use the Error Handler:
		OutputStreamWriter errorWriter = new OutputStreamWriter(System.err,
                outputEncoding);
		db.setErrorHandler(new MyErrorHandler (new PrintWriter(errorWriter, true)));
		Document doc = db.parse(new File(filename));
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("entry");
		System.out.println(nList.getLength());
		for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            System.out.println("\nCurrent Element :" 
               + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNode;
               NamedNodeMap nnm = eElement.getAttributes();
               if (nnm != null)
               {
                  for (int i=0; i<nnm.getLength(); i++)
                  {
                     Node n = nnm.item(i);
                     System.out.println(" " + n.getNodeName() + " = " + n.getNodeValue());
                  }
               }
               NodeList big2 = nNode.getChildNodes();
               System.out.println(big2.getLength());
               for (int temp2 = 0; temp2 < big2.getLength(); temp2++) {
            	   Node nNode2 = big2.item(temp2);
            	   if (nNode2.getNodeName()=="folder"){
            		   System.out.println( "Folder: "+nNode2.getTextContent());
            	   }
               }
            }
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

