package RegViewer;
import ij.plugin.frame.PlugInFrame;

// The supplementary viewer class just groups a viwere and list class together for convenience.
// More such classes might have been useful in this program....
public class SuppViewer extends PlugInFrame{
	private static final long serialVersionUID = 1L;
	public NeuronViewer nv;
	public NeuronTypeList ntl;
	public SuppViewer(String inn, NeuronTypeList ntl, NeuronViewer nv){
		super(inn);
		this.nv = nv;
		this.ntl = ntl;
	}

}
