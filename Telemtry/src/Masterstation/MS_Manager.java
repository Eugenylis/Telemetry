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
 * 
 * @author Yevgeniy Lischuk
 * @version 1.0
 *
 */
public abstract class MS_Manager{
   
	//location or path to the data
	public static String dataLocation;
	//array list of created receiver stations
	public static ArrayList<Station> stationArrayList = new ArrayList<Station>();
	//station counter
	public static int stationCount = 0;
	//array to store port number of stations
	public static ArrayList<Integer> previousPortNum = new ArrayList<Integer>();
	
	
	/**
	 * Main method to launch Master Station GUI
	 * @param args Standard variable
	 * @throws IOException
	 */
	public static void main(String[] args){
		//launch MS GUI
		Application.launch(MasterGUI.class, args);
	}
	
	
	/**
	 * Method to create a file receiver with specified port number
	 * @throws IOException
	 */
	public static void createStation(int portNum, String name) throws IOException{
		//dataLocation = (new File(".")).getAbsoluteFile().getParentFile().getPath() + "\\Extras\\data"; ////////////////////This sets the data save location to be the project directory under lib/data/////////////////ELIZA WILL HAVE THE USER GIVE THIS.
		stationArrayList.add(new Station(portNum, name, dataLocation));
		//increment the counter
		stationCount++;
	}
	
	
	/**
	 * Method to check if the text typed in a box is correct (4-integer number)
	 * Checks if string is not equal to 4
	 * Checks each character in a string for being an integer
	 * Checks if the string is empty
	 * Checks if port number is equal to the port number of any previous stations
	 * @param str - a string to check
	 * @param radix - radix, range
	 * @return verify that string is a 4-digit integer number
	 */
	public static boolean isPortCorrect(String portString, int radix) {
		//variable to return
		boolean result = true;
		//integer value for the port to check for, initialized at some number (0 in this case, but any number would work)
		int portToCheck = 0;
		
		//check is the string is less than 4 characters long
	    if(portString.length() != 4) { result = false;	}
	    
	    //check each charter in a string for being a number
	    for(int i = 0; i < portString.length(); i++) {
	        if(i == 0 && portString.charAt(i) == '-') {
	            if(portString.length() == 1 ) { result = false; }
	        }
	        if(Character.digit(portString.charAt(i),radix) < 0) {
	        	result = false;
	        }
	    }
		
		//check is string is empty
		if(portString.isEmpty()){ result = false; }
		
		//if no previous conditions were true, parse string to a integer
		if (result == true){ 
			portToCheck = Integer.parseInt(portString); 
		}
		
		//if more than one stations exist check port number
		if (stationCount>0){
			//add port number of the previous station and add it to the array of port numbers
			previousPortNum.add(  stationArrayList.get(stationArrayList.size()-1).receiver.getPortNum()  );
			//check if current port number is the same as port of any previous stations
			for (int j=0; j<previousPortNum.size(); j++){
				if(portToCheck == previousPortNum.get(j)){ 
					result = false;	
				}
			}
		}
		return result;
	}
	
	
	/**
	 * Method to remove a selected station
	 * Uses Iterator to find the station based on its name
	 * Removes station from the list
	 * Disconnects the station from communication (closes the socket)
	 * @param stationName - name of the station to remove from the list
	 */
	public static void removeStation(String stationName){
		//Creates list like the counter in MasterGUI to increment station array list
		Iterator<Station> stationIterator = stationArrayList.iterator();
		
		while(stationIterator.hasNext()){
			//get the next station
			Station station = stationIterator.next();
			if(station.stationName == stationName){ 
				//disconnect the station
				station.disconnect(); 
				//remove the station from class list of stations
				stationIterator.remove(); 
				break;
			}
		}
	}
	
	
	/**
	 * Method to remove all ground stations from the array list
	 * Uses Iterator to increment station array list
	 * Removes all stations from the list
	 * Disconnects all stations from communication (closes the socket)
	 */
	public static void disconnectAllStations(){
		Station station;
		
		//Creates list like the counter in MasterGUI to increment station array list
		Iterator<Station> stationIterator = stationArrayList.iterator(); 
			station = stationIterator.next();
			//disconnect the station
			station.disconnect();
			//remove the station from class list of stations
			stationIterator.remove();
	}
	

	/**
	 * Method to return current counter for the array of stations
	 * @return current station count
	 */
	public static int getStationCount(){
		return stationCount;
	}
	
} //end of MS_Manager
