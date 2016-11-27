package Masterstation;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Graph {	
	
static int Xcount=1;
static int Ycount=1; // counting number of line print
static double counter=100000;
static String file1="E:\\java\\matlab test\\test1.txt";
static String file2="E:\\java\\matlab test\\test2.txt";

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
				while(Ycount<counter){//loop
				
		
				File temp = new File(file1);
				File temp2= new File(file2);

				try (BufferedReader br = new BufferedReader(new FileReader(temp))) {	
				     
					try(BufferedReader br2 = new BufferedReader(new FileReader(temp2))){
					    String line ;
					    String line2;
					    int count=0;
					 while ((line = br.readLine()) != null && (line2 = br2.readLine()) != null) {		
			        	count++;//count number of line 
			        	System.out.printf("Fist count: %d \n",count);
			        	System.out.printf("Secount Xcount: %d \n",Xcount);
			        	System.out.printf("Secount Ycount: %d \n",Ycount);
			        	// if the number of new line is added then do it 
			            if( Xcount == count ) {	     			    
							        
						            if( Ycount == count ) {
						            double number = Double.parseDouble(line);
									System.out.println(number);				
									double number2 = Double.parseDouble(line2);
						            System.out.println(number2);	
									series.add(number, number2);
									window.repaint();
									Ycount++;
									Xcount++;
					                }
			            }    
			        }
					}catch(IOException e){
						e.printStackTrace();
					}
							}catch(IOException e){
								e.printStackTrace();
							}					
			            }
			        }					

			
		
			//end while
		};
		thread.start();
		
	} 
	else {}	
	
};
});	//end of button	
// show the window
	window.setVisible(true);
}
};



