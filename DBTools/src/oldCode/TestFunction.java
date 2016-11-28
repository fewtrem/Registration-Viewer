package oldCode;
import java.io.File;


public class TestFunction {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sourceFolderIn = "/afs/inf.ed.ac.uk/user/s11/s1144899/PhD/Python Projects/DBTools/src";
		String folder = "A";
		String file = "rabbit.jpg";
		File thisFilePath = new File(sourceFolderIn);
		thisFilePath = new File(thisFilePath,folder);
		thisFilePath = new File(thisFilePath,file);
		System.out.println(thisFilePath.toString());
	}

}
