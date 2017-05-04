package Viewer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.json.JsonObject;
import javax.swing.JFrame;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

import com.sun.java.swing.Painter;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class GPSMapView extends GViewAbstract{
	private JXMapViewer jmv;
	private DefaultWaypoint point;
	
	public GPSMapView(JFrame frame, int id, Viewer v){
		super(frame,id,v);
		System.out.println("Parametrage de jxmapviewer");
		this.jmv = new JXMapViewer();
		this.point = new DefaultWaypoint();
		// Create a TileFactoryInfo for OpenStreetMap
		TileFactoryInfo info = new OSMTileFactoryInfo();
		DefaultTileFactory tileFactory = new DefaultTileFactory(info);
		//System.out.println(tileFactory.toString());
		jmv.setTileFactory(tileFactory);
		tileFactory.setThreadPoolSize(8);
		// Set the focus
		GeoPosition frankfurt = new GeoPosition(50.11, 8.68);
		this.point = new DefaultWaypoint(frankfurt);
		jmv.setZoom(17);
		jmv.setAddressLocation(frankfurt);
		// Create a waypoint painter that takes all the waypoints
				
		System.out.println("Configure");
		System.out.println("MAJ du Frame principal");
		this.frame.setVisible(false);
		this.frame.getContentPane().removeAll();
		this.frame.setContentPane(this.jmv);
		this.frame.getContentPane().validate();
		this.frame.repaint();
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
				System.out.println("LAT: "+lat+" LNG: "+lng);
				//System.out.println(lat+"   "+lng);
				point.setPosition(new GeoPosition(lat,lng));
				this.jmv.repaint();
			}
			
		}
	}
}
