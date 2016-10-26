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

    static public Sender SimClient;
    static public GS_GUI GUIwindow;
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
		//this.listenerDir = listenerDir;
		//this.IPaddress = IPaddress;
		//this.socketNum = socketNum;
		
		Path dir = Paths.get(listenerDir);
		//System.out.println("Passed1");
		
		//System.out.println("Within setSetting " + listenerDir + " " + IPaddress + " " + socketNum);
		
		SimClient = new Sender(IPaddress, socketNum);
		//System.out.println("Passed2");
		//new WatchDir(dir, true).processEvents();
		new WatchDir(dir, false).processEvents();
		//System.out.println("Passed3");
	}
}