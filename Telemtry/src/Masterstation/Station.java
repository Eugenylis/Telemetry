package Masterstation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javafx.embed.swing.SwingNode;

public class Station {
	
	public Receiver receiver;
	public String stationName = " ";
	final ArrayList<SwingNode> swingNodeList = new ArrayList<SwingNode>();
	public int plotUpdateFreqMS; //in miliseconds
	
	
	
	/**
     * Constructor which creates socket with specified port number and specified name
     */
    public Station(int portNumber, String name, String saveLocation) {
    	
    	setStationName(name);
    	receiver = new Receiver(portNumber, name, saveLocation);
    	plotUpdateFreqMS = 1000; //Find a better place to put this
    }
    
    
    /**
     * Method to set the station name
     * @param name of the station
     */
    public void setStationName(String newName){
    	stationName = stationName.replace(stationName, newName);
    }
    
    /**
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * @param name of data type (temperature, pressure, etc...) which is the first line for the packet received.
     */
    public SwingNode getNewSwingNodePlot(String sensorTypes, int listIndex) {
        
    	System.out.println(sensorTypes + "__________________---------------------~~~~~~~~~~~~````````````````");
    	String properTitle = Character.toUpperCase(sensorTypes.charAt(0)) + sensorTypes.substring(1);
    	
    	SwingNode swingNode = new SwingNode();
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	XYSeries series = new XYSeries(properTitle);
            	XYSeriesCollection dataset = new XYSeriesCollection(series);
        		JFreeChart chart = ChartFactory.createScatterPlot(properTitle + " plot", properTitle, "Altitude (m)", dataset);
        		ChartPanel plot= new ChartPanel(chart);        		
        		swingNode.setContent(plot);
        		
        		//adds to list
        		//swingNodeList.add(listIndex, swingNode); //Good for future when needing to know the location of the element in the list
        		swingNodeList.add(swingNode); ////////////LOOK AT MEEEEEEEEEEEEEE
        		
        		// create a new thread that listens for incoming text and populates the graph
        		Thread thread = new Thread(){     			
        			
        			@Override public void run() {
        				int totalCount = 0;
        				while(true){
        				
	        				File temp = new File(receiver.dataHandler.plotDataLocation + "\\" + sensorTypes + ".txt");
        					double number[] = new double[2];
        					String[] data;// = new String[2];
	        				try (BufferedReader br = new BufferedReader(new FileReader(temp))) {	
	        				     
	        					String line;
	        					
	        					int tempCount = 0;
	        					while ((line = br.readLine()) != null) {		
	        						
	        						tempCount++;
	        						if (totalCount < tempCount){
		        						totalCount++;//count number of line 
		        			        	
		        			        	data = line.split(",");
		        			        	
		        			        	number[0] = Double.parseDouble(data[0]);			
										number[1] = Double.parseDouble(data[1]);
										series.add(number[0], number[1]);
										plot.repaint();
	        						}  
	        			        }
	        					Thread.sleep(plotUpdateFreqMS);
	
	        				}catch(Exception e){
	        					e.printStackTrace();
	        				}					
        				}
        			}					
        			//end while
        		};
        		thread.start();
        		
        		
            }
        });
		return swingNode;
	}
}
