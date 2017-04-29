package Viewer;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.json.JsonObject;
import javax.swing.JButton;
import javax.swing.JList;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;

public class IHM extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
	private JButton btnList;
	private JButton btnStartView;
	private JTextField txtClasse;
	private JTextField txtNom;
	private JLabel lblNom;
	private JLabel lblClasse;
	private Viewer v;
	private int focusId;
	private String focusClass;
	private JPanel panel1;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		if(args.length == 0){
			System.err.println("Usage: "+IHM.class + " <srv adr>");
			System.exit(-1);
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IHM frame = new IHM(args[0]);
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
	public IHM(String adr) {
		v = new Viewer(adr);
		this.focusId = -1;
		this.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 325);
		getContentPane().setLayout(null);
		this.createPanel();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int jdb = JOptionPane.YES_NO_OPTION;
    	jdb = JOptionPane.showConfirmDialog(null, "Start View","warning",jdb);
    	if(jdb == JOptionPane.NO_OPTION)
    		return ;
        
        Thread gvi = null;
		if(this.focusClass.compareTo("GPS") == 0)
        	gvi = new Thread(new GPSMapView(this,this.focusId,v));
		else if(this.focusClass.compareTo("Accelerometer") == 0 || this.focusClass.compareTo("Gyroscope") == 0)
			gvi = new Thread(new TriPointViewer(this,this.focusId,v));
		else
			System.err.println("Aucun viewer pour la classe "+this.focusClass);
		if(gvi != null)
			gvi.start();
	}
	
	public void refreshPanel(){
		this.removeAll();
		this.createPanel();
	}
	
	private void createPanel()
    {
		final JList<String> list = new JList<String>();
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() >= 2) {
		            // Double-click detected
					@SuppressWarnings("unchecked")
					JList<String> list = (JList<String>) evt.getSource();
					String res = list.getSelectedValue();
			        System.out.println(res.split(" ")[0]);
			        //JsonObject resMess = v.getLastMessage(Integer.parseInt(res.split(" ")[0]));
			        //JOptionPane.showMessageDialog(null ,resMess);
			        focusClass = res.split("      ")[1];
			        focusId = Integer.parseInt(res.split("      ")[0]);
					btnStartView.setEnabled(true);
		        	} 
		        
			}
		});
		list.setBounds(12, 12, 414, 140);
		getContentPane().add(list);
		
		btnStartView = new JButton("Start");
		btnStartView.setEnabled(false);
		btnStartView.addActionListener(this);
		btnStartView.setBounds(90, 255, 117, 25);
		getContentPane().add(btnStartView);
		btnList = new JButton("List");
		btnList.addMouseListener(new MouseAdapter() {
			private Device[] listRes;

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
