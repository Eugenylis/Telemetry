package Groundstation;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;

import Masterstation.MS_Manager;

//test

public class GS_GUI {

	private JFrame frame;
	private JTextField textFieldIP;
	private JLabel lblNewLabel_1;
	private JLabel lblStep;
	private JLabel lblNewLabel_2;
	private JTextField textFieldSocket;
	private JLabel lblNewLabel_3;
	private JButton btnNewButton_2;

	String filename;
	String IPaddress;
	int socketNum;

	/**
	 * Launch the application.
	 */
	//	public static void main(String[] args) {
	//		EventQueue.invokeLater(new Runnable() {
	//			public void run() {
	//				try {
	//					GS_GUI window = new GS_GUI();
	//					window.frame.setVisible(true);
	//				} catch (Exception e) {
	//					e.printStackTrace();
	//				}
	//			}
	//		});
	//	}

	/**
	 * Create the application.
	 */
	public GS_GUI() {
		initialize();
		this.frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1180, 594);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		textFieldIP = new JTextField();
		textFieldIP.setBounds(405, 28, 540, 49);
		frame.getContentPane().add(textFieldIP);
		textFieldIP.setColumns(10);

//		JButton btnNewButton = new JButton("OK");
//		btnNewButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				IPaddress = textFieldIP.getText();
//				//				 try{
//				//					 num1=textFieldIP.getText();
//				//					 
//				//				 } catch(Exception e){
//				//					 
//				//					 JOptionPane.showMessageDialog(null, "Please Enter Valid Number");
//				//				 }
//			}
//		});
//		btnNewButton.setBounds(971, 28, 151, 49);
//		frame.getContentPane().add(btnNewButton);

		JLabel lblNewLabel = new JLabel("Enter IP address");
		lblNewLabel.setBounds(149, 36, 196, 33);
		frame.getContentPane().add(lblNewLabel);

		lblNewLabel_1 = new JLabel("Step 1");
		lblNewLabel_1.setBounds(26, 36, 115, 33);
		frame.getContentPane().add(lblNewLabel_1);

		lblStep = new JLabel("Step 2");
		lblStep.setBounds(26, 102, 115, 33);
		frame.getContentPane().add(lblStep);

		lblNewLabel_2 = new JLabel("Enter port number ");
		lblNewLabel_2.setBounds(149, 102, 241, 33);
		frame.getContentPane().add(lblNewLabel_2);

		textFieldSocket = new JTextField();
		textFieldSocket.setBounds(405, 105, 540, 49);
		frame.getContentPane().add(textFieldSocket);
		textFieldSocket.setColumns(10);

//		btnNewButton_1 = new JButton("OK");
//		btnNewButton_1.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				try{
//					socketNum=Integer.parseInt(textFieldSocket.getText());
//
//				} catch(Exception e){
//
//					JOptionPane.showMessageDialog(null, "Please Enter Valid Number");
//				}
//			}
//		});
//		btnNewButton_1.setBounds(971, 105, 151, 45);
//		frame.getContentPane().add(btnNewButton_1);

		lblNewLabel_3 = new JLabel("Step 3");
		lblNewLabel_3.setBounds(26, 177, 115, 33);
		frame.getContentPane().add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("Choose Directory to listen to");
		lblNewLabel_4.setBounds(148, 177, 345, 33);
		frame.getContentPane().add(lblNewLabel_4);

		JTextPane textPane = new JTextPane();
		textPane.setBounds(405, 182, 540, 49);
		frame.getContentPane().add(textPane);

		JLabel lblNewLabel_5 = new JLabel("Step 4");
		lblNewLabel_5.setBounds(26, 252, 115, 33);
		frame.getContentPane().add(lblNewLabel_5);

		btnNewButton_2 = new JButton("Open");
		
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser openFile = new JFileChooser();
				openFile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				openFile.showOpenDialog(null);
				File f=openFile.getSelectedFile();
				filename=f.getAbsolutePath();
				textPane.setText(filename);
			}
			
		});
		btnNewButton_2.setBounds(971, 178, 151, 41);
		frame.getContentPane().add(btnNewButton_2);

		JButton btnRun = new JButton("RUN");
		btnRun.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if(GS_Manager.isPortCorrect(textFieldSocket.getText(),10)){
					IPaddress = textFieldIP.getText();
					try{
						socketNum=Integer.parseInt(textFieldSocket.getText());
	
					} catch(Exception e2){
	
						JOptionPane.showMessageDialog(null, "Please Enter Valid Socket Number");
					}
					try{
						//System.out.println("Within GUI " + filename + " " + IPaddress + " " + socketNum);
						GS_Manager.setSettings(filename, IPaddress, socketNum);
	
					} catch(Exception e3){
	
						System.out.print("Broke1");
					}
				} // end of if
			} // end of actionPerformed
		});
		btnRun.setBounds(149, 248, 171, 41);
		frame.getContentPane().add(btnRun);
	}
}
