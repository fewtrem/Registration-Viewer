
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ColorProcessor;
import java.io.File;
import java.util.HashMap;


public class RegStackViewer {
	public static final int[] CHANNELINTS = {16,8};
	public static final int[] CHANNELXS = {0xff0000,0x00ff00};
	public static final String NRRDFILE = "reformatnrrd";
	public static final String CHANR = "R";
	public static final String CHANG = "G";
	/**
	 * @param args
	 */
	public static ImagePlus getRegStack(Neuron neuronToView,String sourceFolder,ImagePlus templateStack,HashMap<String,String> replacementValues){
		ImagePlus toReturn = null;
		if (templateStack!=null && neuronToView!=null){
			String newFilename = EdRegViewer_.doReplacement(neuronToView.getXMLData(NRRDFILE), replacementValues, neuronToView);
			File thisFilePath = new File(sourceFolder,neuronToView.getXMLData(EdRegViewer_.FOLDER));
			thisFilePath = new File(thisFilePath,newFilename);
			ImagePlus neuronNRRD = EdRegViewer_.loadImage(thisFilePath);
			if (neuronNRRD != null){
				ImageStack returnedStack = makeNewStack(templateStack,neuronNRRD,neuronToView.getXMLData(Neuron.CHANNEL));
				if (returnedStack!=null){
					toReturn = new ImagePlus();
					toReturn.setTitle(neuronToView.toString());
					toReturn.setStack(returnedStack);
					toReturn.show();
				}
			}
			else{
				IJ.log("No neuron file path defined");
			}
		}
		else{
			IJ.log("No template stack / neuron  found");
		}
		return toReturn;
	}
	
	private static ImageStack makeNewStack(ImagePlus tempIm,ImagePlus neuronNRRD,String channel){
		ImageStack returnPlus = null;
		if (tempIm.getWidth()==neuronNRRD.getWidth() && tempIm.getHeight()==neuronNRRD.getHeight() && tempIm.getStackSize()==neuronNRRD.getStackSize()){
			ImageStack tempStack = tempIm.getImageStack();
			ImageStack neuronStack = neuronNRRD.getImageStack();
			ImageStack theStack = new ImageStack(tempStack.getWidth(),tempStack.getHeight(),tempStack.getSize());
			for(int a = 0;a<tempStack.getSize();a++){
				ColorProcessor cp = new ColorProcessor(theStack.getWidth(), theStack.getHeight());
				byte[] tempPix = (byte[])tempStack.getPixels(a+1);
				byte[] neuronPix = (byte[])neuronStack.getPixels(a+1);
				//R only
				if (channel.equals(CHANR)==true){
					for (int i = 0; i < tempPix.length; ++i) {
						cp.set(i,((int)(neuronPix[i]& 0xFF)<<16)+(int)(tempPix[i]& 0xFF));
					}
				}
				//G only
				else if (channel.equals(CHANG)==true){
					for (int i = 0; i < tempPix.length; ++i) {
						cp.set(i,((int)(neuronPix[i]& 0xFF)<<8)+(int)(tempPix[i]& 0xFF));
					}
				}
				//Y:
				else{
					for (int i = 0; i < tempPix.length; ++i) {
						cp.set(i,((int)(neuronPix[i]& 0xFF)<<16)+((int)(neuronPix[i]& 0xFF)<<8)+(int)(tempPix[i]& 0xFF));
					}
				}
				theStack.setPixels(cp.getPixels(), a+1);
			}
			returnPlus = theStack;
		}
		else{
			IJ.log("There is a problem with the dimensions not matching:");
			IJ.log("Template x:"+tempIm.getWidth()+" vs. Neuron x:"+neuronNRRD.getWidth());
			IJ.log("Template y:"+tempIm.getHeight()+" vs. Neuron y:"+neuronNRRD.getHeight());
			IJ.log("Template z:"+tempIm.getStackSize()+" vs. Neuron z:"+neuronNRRD.getStackSize());
		}
		return returnPlus;
	}

}
