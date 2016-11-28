import ij.io.PluginClassLoader;


public class loadPlugin {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClassLoader pcl = (ClassLoader)ij.IJ.getClassLoader();
		try{
			Class dere = pcl.loadClass("RegViewer_");
		}
		catch(Exception e){
			System.out.println("D"+e.toString());
		}
		

	}

}
