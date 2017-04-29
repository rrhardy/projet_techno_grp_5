package Viewer;
import javax.json.JsonObject;
import javax.swing.JFrame;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

public class GPSMapView extends GViewAbstract{
	private JXMapViewer jmv;
	private DefaultWaypoint point;
	
	public GPSMapView(JFrame frame, int id, Viewer v){
		super(frame,id,v);
		System.out.println("Parametrage de jxmapviewer");
		this.jmv = new JXMapViewer();
		// Create a TileFactoryInfo for OpenStreetMap
		TileFactoryInfo info = new OSMTileFactoryInfo();
		DefaultTileFactory tileFactory = new DefaultTileFactory(info);
		System.out.println(tileFactory.toString());
		jmv.setTileFactory(tileFactory);
		tileFactory.setThreadPoolSize(8);
		// Set the focus
		GeoPosition frankfurt = new GeoPosition(50.11, 8.68);
		this.point = new DefaultWaypoint(frankfurt);
		jmv.setZoom(7);
		jmv.setAddressLocation(frankfurt);
		System.out.println("Configuré");
		System.out.println("MAJ du Frame principal");
		this.frame.setVisible(false);
		this.frame.getContentPane().removeAll();
		
		frame.add(this.jmv);
		this.frame.setSize(800,600);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setVisible(true);
		System.out.println("OK");
		//this.jmv = new JMapViewer();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Execution thread principal avec " + v.getLastMessage(this.id));
		while(true){
			JsonObject resp = v.getLastMessage(this.id).getJsonObject("contents");
			try{
				Thread.sleep(1000);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			if(resp != null){
				double lat = resp.getJsonNumber("lat").doubleValue();
				double lng = resp.getJsonNumber("lng").doubleValue();
				//System.out.println(lat+"   "+lng);
				synchronized(point){
					point.setPosition(new GeoPosition(lat,lng));
					this.jmv.repaint();
				}
			}
			
		}
	}
}
