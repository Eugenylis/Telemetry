package Groundstation;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;

/**
 * GS_Manager is the class to manage and encompass all ground station classes
 * Starts GS_GUI
 * Manages file sender, file zipper and zip file timer
 * Starts and manager directory watcher thread
 * Checks if port number was given correctly
 * Checks if IP number was given correctly
 * 
 * @author Erik Parker, Victor Wong, Yevgeniy Lischuk
 * @version 2.0
 *
 */
public abstract class GS_Manager{
	//sender object to transmit files
    public static Sender zipSender; 
    //object to zip multiple files together based on data sending frequency
    public static ZipFileTimer zipTimer;
    //data sending frequency
    public static int datSendFreqMiliseconds;
    
    
	/**
	 * Main method, launches Ground Station GUI
	 */
	public static void main(String[] args) throws IOException {
		Application.launch(GS_GUI.class, args);
	}
	
	
	/**
	 * Method to create objects for sending data, based on setting set by the user in GS_GUI
	 * @param listenerDir The location of the directory being watched for changes
	 * @param IPaddress The IP address of the Master Station
	 * @param socketNum The socket number of the Master station to transfer files to
	 * @param freq timer of how often to send data packets
	 * @throws IOException
	 */
	public static void setSettings(String listenerDir, String IPaddress, int socketNum, int freq) throws IOException{
		//set the path to the directory to listen to 
		Path dir = Paths.get(listenerDir);
		
		//create new sender object based on IP an socket number
		zipSender = new Sender(IPaddress, socketNum);
		
		//initialize zip file timer
		datSendFreqMiliseconds = freq*1000;
		zipTimer = new ZipFileTimer(datSendFreqMiliseconds);
		
		//create new watcher (directory listener) and start the thread
		WatchDir watcher = new WatchDir(dir, false);
		Thread thread = new Thread(watcher);
		thread.start();
	}
	
	
	/**
	 * Method to check if the text typed in a box is correct (4-integer number)
	 * Checks if string is not equal to 4
	 * Checks each character in a string for being an integer
	 * Checks if the string is empty
	 * 
	 * @param str - a string to check
	 * @param radix - radix, range
	 * @return verify that string is a 4-digit integer number
	 */
	public static boolean isPortCorrect(String portString, int radix) {
		//variable to return if port is correct
		boolean correct = true;
		
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
		
		return correct;
	}
	
	
	/**
	 * Method to check if the text typed in a box is correct IP address
	 * Checks if IP address is in correct format, Example: 155.55.555.55
	 * @param ip - a IP address string to check
	 * @return false - if incorrect, true if correct
	 */
	public static boolean ipValid(String ip) {
	    Pattern p = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
	    Matcher m = p.matcher(ip);
	    return m.find();
	}
	
} //end of class