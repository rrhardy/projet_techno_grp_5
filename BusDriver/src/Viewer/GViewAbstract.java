package Viewer;

import javax.swing.JFrame;

public abstract class GViewAbstract implements GView{
	protected int id;
	protected Viewer v;
	protected JFrame frame;
	
	public GViewAbstract(JFrame frame, int id, Viewer v){
		this.id = id;
		this.v = v;
		this.frame = frame;
		//this.jmv = new JMapViewer();
	}
}
