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
public class GS_Manager{
    static public Sender zipSender;
    static public GS_GUI GUIwindow;
    static public ZipFileTimer Timer;
    //static public String listenerDir;
    //static public String IPaddress;
    //static public int socketNum;
	
	/**
	 * @param args Standard variable
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GS_GUI GUIwindow = new GS_GUI();
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
		Timer = new ZipFileTimer();
		new WatchDir(dir, false).processEvents();
				
	}
}