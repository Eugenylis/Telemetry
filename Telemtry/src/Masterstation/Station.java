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

/**
 * Station is a class, which encompasses all features for a particular Ground Station used in Master Station when it is added in GUI
 * Features include: using receiver to start communication and received data,
 * Using SwingNode to plot newly received data
 * SwingNode is a swing-based plotter, which allows to plot the data and update the plots in real-time
 * Real-time plotting is done using threads
 * Also plotting update rate can be delayed by specified amount of milliseconds
 * 
 * @author Erik Parker, Victor Wong
 * @version 2.0 
 */
public class Station {
	
	//create a receiver for the station to receive incoming connection and files
	public Receiver receiver;
	//Array list of plots using SwingNode
	final ArrayList<SwingNode> swingNodeList = new ArrayList<SwingNode>();
	//name of the station
	public String stationName = "";
	//plot update frequency in milliseconds
	public int plotUpdateFreqMS;
	
	
	/**
     * Constructor which creates station
     * Set station name
     * Creates a new receiver object to receive incoming connection and files
     * @param portNumber - 4-digit port number required for communication
     * @param name - station name as a string
     * @param saveLocation - location to save received files
     */

    public Station(int portNumber, String name, String saveLocation) {
    	//set name of the station by changing the string
    	setStationName(name);
    	//create new receiver 
    	receiver = new Receiver(portNumber, name, saveLocation);
    	//set frequency for updating plots
    	plotUpdateFreqMS = 1000; 
    }
    
    
    /**
     * Method to set the station name
     * Replaces existing string name with a new one
     * @param name of the station 
     */
    public void setStationName(String newName){
    	stationName = stationName.replace(stationName, newName);
    }
    
    
    /**
     * Method to plot the data, receive by the master station
     * Uses threads to run continuously
     * Reads data from received file
     * Plots the data in real-time by updating the plots
     * @param sensorTypes type of the sensor data to plot, which is the first line for received data packet. Ex: altitude, GPS location, pressure, etc.
     * @param listIndex index number of the list
     */
    public SwingNode getNewSwingNodePlot(String sensorTypes, int listIndex) {
        
    	System.out.println(sensorTypes + "__________________---------------------~~~~~~~~~~~~````````````````");
    	//set title of the plot and sensor types
    	String properTitle = Character.toUpperCase(sensorTypes.charAt(0)) + sensorTypes.substring(1);
    	//create new swingNone object 
    	SwingNode swingNode = new SwingNode();
    	
    	
    	SwingUtilities.invokeLater(new Runnable() {   		
    		
            @Override
            public void run() {
            	//create plot and plot tittle
            	XYSeries series = new XYSeries(properTitle);
            	//create array for data
            	XYSeriesCollection dataset = new XYSeriesCollection(series);
            	//Create plot
        		JFreeChart chart = ChartFactory.createScatterPlot(properTitle + " plot", properTitle, "Altitude (m)", dataset);
        		//add chart to pane
        		ChartPanel plot= new ChartPanel(chart);  
        		//add content to the plot
        		swingNode.setContent(plot);
        		
        		//adds to list
        		//swingNodeList.add(listIndex, swingNode); //Good for future when needing to know the location of the element in the list
        		swingNodeList.add(swingNode); 
        		
        		// create a new thread that listens for incoming text and populates the graph
        		Thread thread = new Thread(){     			
        			//run the thread
        			@Override 
        			public void run() {
        				int totalCount = 0;
        				
        				while(true){
        					//create a new file object to get the data received by the master station
	        				File temp = new File(receiver.dataHandler.plotDataLocation + "\\" + sensorTypes + ".txt");
        					double number[] = new double[2];
        					String[] data;// = new String[2];        					
	        				try (BufferedReader br = new BufferedReader(new FileReader(temp))) {
	        					String line;
	        					int tempCount = 0;
	        					
	        					//read each line in a data fle
	        					while ((line = br.readLine()) != null) {		
	        						tempCount++;
	        						
	        						if (totalCount < tempCount){
	        							//count number of lines
		        						totalCount++;
		        			        	data = line.split(",");
		        			        	number[0] = Double.parseDouble(data[0]);			
										number[1] = Double.parseDouble(data[1]);
										//add data to array
										series.add(number[0], number[1]);
										// update plot
										plot.repaint();
	        						}  
	        			        }
	        					//stop the thread for specified amount of milliseconds
	        					Thread.sleep(plotUpdateFreqMS);
	
	        				}catch(Exception e){
	        					e.printStackTrace();
	        				}
        				} // end of while(true)
        			}	// end run()				
        		}; // end of thread for graphs
        		
        		// start a new thread immediately
        		thread.start();
            } //end upper run()
        });
		return swingNode;
	} // end of SwingNode
    
    
    /**
     * Method to start executing receiver thread
     * Implements connect() method start running the thread
     */
    public void connect(){
		receiver.connect();
	}
    
    
    /**
     * Method to close the socket to stop communication
     * Implements disconnect() function in the receiver to close the socket
     */
    public void disconnect(){
    	//closes station socket
    	receiver.disconnect(); 
    	
    }
    
    
    /**
     * Method to check if receiver thread is alive
     */
    public boolean isConnected(){
    	return receiver.isAlive();
    }
    
} // end of class
