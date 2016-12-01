package Masterstation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
   
	//class for receiving incoming data
	public static String dataLocation;
	public static ArrayList<Station> stationArrayList = new ArrayList<Station>();
	public static int stationCount = 0;
	
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
		
		dataLocation = (new File(".")).getAbsoluteFile().getParentFile().getPath() + "\\Extras\\data"; ////////////////////This sets the data save location to be the project directory under lib/data/////////////////ELIZA WILL HAVE THE USER GIVE THIS.
		stationArrayList.add(new Station(portNum, name, dataLocation));
		//increment the counter
		stationCount++;
	}
	
	/**
	 * Method to return current counter for the array of stations
	 * @return current station count
	 */
	public static int getStationCount(){
		return stationCount;
	}
	
	
	/**
	 * Method to check if the text typed in a box is correct (4-integer number)
	 * Checks if string is not equal to 4
	 * Checks each character in a string for being an integer
	 * Checks if the string is empty
	 * Checks if port number is equal to the port of previous station
	 * 
	 * @param str - a string to check
	 * @param radix - radix, range
	 * @return verify that string is a 4-digit integer number
	 */
	public static boolean isPortCorrect(String portString, int radix) {
		
		//variable to return
		boolean result = true;
		//integer value for the port to check for, initialized at some number (0 in this case, but any number would work)
		int portToCheck = 0;
		//previous port number, initialized at some number (0 in this case, but any number would work)
		int previousPortNum = 0;
		
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
		
		//if more than one stations exist, assign previous port number to a variable for checking
		if (stationCount>0){
		previousPortNum = stationArrayList.get(stationArrayList.size()-1).receiver.getPortNum();
		}
		
		//check if two stations have the same port number
		if(portToCheck == previousPortNum){ result = false;	}
	
		return result;
	}
}