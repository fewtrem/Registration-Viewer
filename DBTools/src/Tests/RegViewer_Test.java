package RegViewer;


public class RegViewer_Test {
	RegViewer_ regViewer;

	public RegViewer_Test(){
		regViewer = new RegViewer_();
		
		regViewer.run(null);
	}
	
	public static void main(String[] args) {
		RegViewer_Test newThis = new RegViewer_Test();
		System.out.println(newThis.hashCode());
	}

}
