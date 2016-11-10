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
	
	static int secoundcount=1; // counting number of line print

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
	JFreeChart chart = ChartFactory.createScatterPlot("Light Sensor Readings", "Time (seconds)", "ADC Reading", dataset);
	window.add(new ChartPanel(chart), BorderLayout.CENTER);	
	
	connectButton.addActionListener(new ActionListener(){
		@Override public void actionPerformed(ActionEvent arg0) {
		if(connectButton.getText().equals("Connect")) {

			
			// create a new thread that listens for incoming text and populates the graph
			Thread thread = new Thread(){
				
				@Override public void run() {
					
			
					File temp = new File("E:\\java\\matlab test\\test1.txt");

					try (BufferedReader br = new BufferedReader(new FileReader(temp))) {
					    String line;
					   int count=0;	
				        while((line = br.readLine()) != null) {		
				        	count++;//count number of line 
				        	System.out.printf("Fist count: %d \n",count);
				        	System.out.printf("Secount count: %d \n",secoundcount);
				        	// if the number of new line is added then do it 
				            if( secoundcount == count ) {
				            double number = Double.parseDouble(line);
							System.out.println(number);									
							series.add(x++, number);
							window.repaint();	
							secoundcount++;
				            }
				        }

					}catch(IOException e){
						e.printStackTrace();
					}						

				}
				//end while
			};
			thread.start();
		} 
		else {}	
		
		}
	});		
	// show the window
	window.setVisible(true);
}
};


