package Viewer;

public abstract class GViewAbstract implements GView{
	protected int id;
	protected Viewer v;
	
	public GViewAbstract(int id, Viewer v){
		this.id = id;
		this.v = v;
		//this.jmv = new JMapViewer();
	}
}
