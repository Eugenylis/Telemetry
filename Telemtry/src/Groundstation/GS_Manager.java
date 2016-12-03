package Groundstation;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.EventQueue;
import java.io.*;

/**
 * GS_Manager manages the file sender, GUI, and the directory watcher.
 * 
 * @author Erik Parker
 * @version 1.0
 *
 */
public abstract class GS_Manager{
    static public Sender zipSender;
    static public GS_GUI GUIwindow;
    static public ZipFileTimer Timer;
    static public int datSendFreqMiliseconds;
    
	/**
	 * @param args Standard variable
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		datSendFreqMiliseconds = 2000;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIwindow = new GS_GUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * @param listenerDir The location of the directory being watched for changes.
	 * @param IPaddress The IP address of the Master side.
	 * @param socketNum The socket number of the Master station to transfer files to.
	 * @throws IOException
	 */
	public static void setSettings(String listenerDir, String IPaddress, int socketNum) throws IOException{
		Path dir = Paths.get(listenerDir);
		zipSender = new Sender(IPaddress, socketNum);
		Timer = new ZipFileTimer(datSendFreqMiliseconds);
		new WatchDir(dir, false).processEvents();	
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
		
		//variable to return
		boolean result = true;
		
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
		
		return result;
	}
}