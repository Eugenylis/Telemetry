package Masterstation;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Graph {	
	
	static int x = 0;

	public static void main(String[] args) {
		
		// create and configure the window
		JFrame window = new JFrame();
		window.setTitle("Sensor Graph GUI");
		window.setSize(600, 400);
		window.setLayout(new BorderLayout());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// create a drop-down box and connect button, then place them at the top of the window
		JButton connectButton = new JButton("Connect");
		JPanel topPanel = new JPanel();
		topPanel.add(connectButton);
		window.add(topPanel, BorderLayout.NORTH);
		

		
		// create the line graph
		XYSeries series = new XYSeries("Light Sensor Readings");
		XYSeriesCollection dataset = new XYSeriesCollection(series);
		JFreeChart chart = ChartFactory.createXYLineChart("Light Sensor Readings", "Time (seconds)", "ADC Reading", dataset);
		window.add(new ChartPanel(chart), BorderLayout.CENTER);		
		connectButton.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent arg0) {
			if(connectButton.getText().equals("Connect")) {

					
					// create a new thread that listens for incoming text and populates the graph
					Thread thread = new Thread(){
						@Override public void run() {
						
							File temp = new File("E:\\java\\matlab test\\test1.txt");
//							Scanner scanner = null;
//							try {
//								scanner = new Scanner(temp);
//							} catch (FileNotFoundException e1) {
//								// TODO Auto-generated catch block
//								e1.printStackTrace();
//							}
							
							
							try (BufferedReader br = new BufferedReader(new FileReader(temp))) {
							    String line;
							    while ((line = br.readLine()) != null) {
							       // process the line.
									//System.out.println("Line data is: " +line );
									
									double number = Double.parseDouble(line);
									System.out.println(number );
									
									series.add(x++, number);
									window.repaint();
							    }
							}catch(IOException e){
								e.printStackTrace();
							}
							
//							while(scanner.hasNextLine()) {
//								try {
//									
//									String line = scanner.nextLine();
//									System.out.println("Line data is: " +line + " ");
//									
//									int number = Integer.parseInt(line);
//									System.out.println("Number is: " + number);
//									
//									series.add(x++, 1023 - number);
//									window.repaint();
//								} catch(Exception e) {}
//							}
							//scanner.close();
						}
					};
					thread.start();
			} else {}
			}
		});
		
		// show the window
		window.setVisible(true);
	}
	};


