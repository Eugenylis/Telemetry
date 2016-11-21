package Masterstation;

import java.io.IOException;

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
	public static Receiver fileReceiverThread;
	
	
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
	public static void setSettings(int portNum) throws IOException{
		
		fileReceiverThread = new Receiver();
		fileReceiverThread.setPortNum(portNum);
				
	}
	
	
	/**
	 * Method to check if the text typed in a box is a 4-digit integer
	 * Checks if the string is empty
	 * Checks if string is not equal to 4
	 * Checks each character in a string for being an integer
	 * 
	 * @param str - a string to check
	 * @param radix - radix, range
	 * @return verify that string is a 4-digit integer number
	 */
	public static boolean isInteger(String str, int radix) {
		
		boolean verify = true;
		//check is string is empty
	    if(str.isEmpty()){ verify = false;}
	    
	    //check is the string is less than 4 characters long
	    if(str.length() != 4) {verify = false;}
	    
	    //check each charter for being a number
	    for(int i = 0; i < str.length(); i++) {
	        if(i == 0 && str.charAt(i) == '-') {
	            if(str.length() == 1 ) { verify = false;}
	        }
	        if(Character.digit(str.charAt(i),radix) < 0) {
	        	verify = false;
	        }
	    }
	    
		return verify;
	}
}