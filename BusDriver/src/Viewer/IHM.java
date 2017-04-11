package Viewer;

import java.awt.EventQueue;

import javax.json.JsonObject;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;

public class IHM extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
	private JButton btnList;
	private JTextField txtClasse;
	private JTextField txtNom;
	private JLabel lblNom;
	private JLabel lblClasse;
	private Device[] listRes;
	private Viewer v;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IHM frame = new IHM();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public IHM() {
		v = new Viewer("127.0.0.1");
		this.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		final JList<String> list = new JList<String>();
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				@SuppressWarnings("unchecked")
				JList<String> list = (JList<String>)evt.getSource();
				String res = new String();
		        if (evt.getClickCount() >= 2) {
		            // Double-click detected
		            res = list.getSelectedValue();
		            GView gvi = null;
		            System.out.println(res.split(" ")[0]);
		            JsonObject resMess = v.getLastMessage(Integer.parseInt(res.split(" ")[0]));
		            JOptionPane.showMessageDialog(null ,resMess);
		            String classe = res.split("      ")[1];
		            int id = Integer.parseInt(res.split("      ")[0]);
		            
					if(classe.compareTo("GPS") == 0)
		            	gvi = (GView)new GPSMapView(id,v);
					if(classe.compareTo("Accelerometer") == 0 || classe.compareTo("Gyroscope") == 0)
						gvi = (GView)new TriPointViewer(id,v);
					
					if(gvi != null)
						gvi.startView();
					else
						System.err.println("Aucun viewer pour la classe "+classe);
		        } 
		        
			}
		});
		list.setBounds(12, 12, 414, 140);
		getContentPane().add(list);
		
		btnList = new JButton("List");
		btnList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String classe = txtClasse.getText();
				String name = txtNom.getText();
				JsonObject[] res;
				if(classe.isEmpty() && name.isEmpty()){
					 res = v.getAllList();
					 System.out.println("all");
				}
				
				if(classe.compareTo("") == 0)
					res = v.getListByName(name);
				else
					res = v.getListByClass(classe);
				
				listRes = new Device[res.length];
				String[] disp = new String[res.length];
				for(int i=0 ; i<res.length ; i++)
					if(res[i] != null){
						disp[i] = res[i].getInt("sender_id",-1)+"      "+res[i].getString("sender_class")+"      "+res[i].getString("sender_name");
						listRes[i] = new Device(res[i].getInt("sender_id",-1),res[i].getString("sender_class"),res[i].getString("sender_name"));
					}
				
				list.setListData(disp);
				
			}
		});
		btnList.setBounds(90, 226, 117, 25);
		getContentPane().add(btnList);
		
		txtClasse = new JTextField();
		txtClasse.setBounds(90, 195, 114, 19);
		getContentPane().add(txtClasse);
		txtClasse.setColumns(10);
		
		txtNom = new JTextField();
		txtNom.setBounds(90, 164, 114, 19);
		getContentPane().add(txtNom);
		txtNom.setColumns(10);
		
		lblNom = new JLabel("Nom");
		lblNom.setBounds(22, 147, 71, 50);
		getContentPane().add(lblNom);
		
		lblClasse = new JLabel("Classe");
		lblClasse.setBounds(12, 179, 71, 50);
		getContentPane().add(lblClasse);
	}
}
