package Masterstation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.application.Application;

/**
 * MS_Manager manages the file receiver and master station GUI
 * Launches master station GUI
 * Creates file receiver
 * Allows to set port number for file receiver
 * Sets each station into the array of stations
 * Allows to check if specified port number for the station is a valid number
 * Allows to remove specific ground station
 * Allows to remove all stations from the array at once
 * 
 * @author Yevgeniy Lischuk
 * @version 2.0
 *
 */
public abstract class MS_Manager{
   
	//location or path to the data
	public static String dataLocation = null;
	//array list of created receiver stations
	public static ArrayList<Station> stationArrayList = new ArrayList<Station>();
	//station counter
	public static int stationCount = 0;
	
	
	/**
	 * Main method to launch Master Station GUI
	 * @param args Standard variable
	 */
	public static void main(String[] args){
		//launch MS GUI
		Application.launch(MasterGUI.class, args);
	}
	
	
	/**
	 * Method to create a file receiver with specified port number
	 * @param portNum port number
	 * @param name name of station
	 * @throws IOException failure to create station
	 */
	public static void createStation(int portNum, String name) throws IOException{
		//dataLocation = (new File(".")).getAbsoluteFile().getParentFile().getPath() + "\\Extras\\data"; ////////////////////This sets the data save location to be the project directory under lib/data/////////////////ELIZA WILL HAVE THE USER GIVE THIS.
		stationArrayList.add(new Station(portNum, name, dataLocation));
		//increment the counter
		stationCount++;
	}
	
	
	/**
	 * Method to return current counter for the array of stations
	 * @return current station count
	 */
	public static int getStationCount(){
		return stationArrayList.size();
	}
	
	
	/**
	 * Method to check if the text typed in a box is correct (4-integer number)
	 * Checks if string is not equal to 4
	 * Checks each character in a string for being an integer
	 * Checks if the string is empty
	 * Checks if port number is equal to the port number of any previous stations
	 * 
	 * @param portString - a string to check
	 * @param radix - radix, range
	 * @return correct - verify that string is a 4-digit integer number
	 */
	public static boolean isPortAvailable(String portString, int radix) {
		
		//variable to return
		boolean correct = true;
		//integer value for the port to check for, initialized at some number (0 in this case, but any number would work)
		int portToCheck = 0;
		
		//check is the string is less than 4 characters long
	    if(portString.length() != 4) { correct = false;	}
	    
	    //check each charter in a string for being a number
	    for(int i = 0; i < portString.length(); i++) {
	        if(i == 0 && portString.charAt(i) == '-') {
	            if(portString.length() == 1 ) { correct = false; }
	        }
	        if(Character.digit(portString.charAt(i),radix) < 0) {
	        	correct = false;
	        }
	    }
		
		//check is string is empty
		if(portString.isEmpty()){ correct = false; }
		
		//if no previous conditions were true, parse string to a integer
		if (correct == true){ 
			portToCheck = Integer.parseInt(portString); 
		}
		
		//if more than one stations exist check port number
		if (stationArrayList.size() > 0){
			
			//iterates through the station list looking are the porn numbers to find a duplicates
			Iterator<Station> stationIterator = stationArrayList.iterator(); // Creates list like thing that allows it to me incremented
			while(stationIterator.hasNext()){
				Station station = stationIterator.next(); //gets next station
				if(station.receiver.getPortNum() == portToCheck){ 
					correct =  false; // returns 
					break;
				}
			}
		}
		return correct;
	}
	
	
	/**
	 * Method to get the specific station based on its name
	 * @param stationName - a string of the station name
	 * @return station which has specific name
	 */
	public static Station getStation(String stationName){
		Station station = null;
		//creates a list of stations using iterator
		Iterator<Station> stationIterator = stationArrayList.iterator();
		
		while(stationIterator.hasNext()){
			//gets next station
			station = stationIterator.next(); 
			if(station.stationName == stationName){ 
				break;
			}
		}
		return station;
	}
	
	
	/**
	 * Method to remove specific station
	 * Disconnects the station
	 * Removes the station from the station array list
	 * @param stationName name of the station
	 */
	public static void removeStation(String stationName){
		Station station = getStation(stationName);
		station.disconnect(); //disconnects station
		stationArrayList.remove(station); //removes station from class list of stations
	}
	
	
	/**
	 * Method to remove all ground stations from the array list
	 * Uses Iterator to increment station array list
	 * Removes all stations from the list
	 * Disconnects all stations from communication (closes the socket)
	 */
	public static void disconnectAllStations(){
		Station station;
		//creates a list of stations using iterator
		Iterator<Station> stationIterator = stationArrayList.iterator();
		
		while(stationIterator.hasNext()){
			station = stationIterator.next();
			//disconnect the station
			station.disconnect();
			//remove the station from class list of stations
			stationIterator.remove();
		}
	}
	
} //end of MS_Manager
