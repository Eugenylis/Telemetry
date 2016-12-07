package Groundstation;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class for handling all operations regarding zipping files
 * Zips files based on given frequency, given in milliseconds
 * Zips multiple files in one packet
 * Executes a thread for zipping files
 * @author Erik Parker
 *
 */
public class ZipFileTimer {
	
	//list of files needed to be archived
	private ArrayList<File> files;
	//lock so multiple threads can access one resource one at a time
	private final ReentrantLock lock = new ReentrantLock(true);
	//timer for zipping files
	private long waitTimeMillis;
	
	
	/**
	 * No argument constructor
	 * Initializes array list of files
	 * Sets frequency for zipping
	 */
	public ZipFileTimer(){
		files=new ArrayList<File>();
		waitTimeMillis=5000;
	}
	
	
	/**
	 * Constructor to create an zip file timer object given zipping frequency
	 * @param waitTimeMillis time to wait before zipping next data packet
	 */
	public ZipFileTimer(long waitTimeMillis){
		files=new ArrayList<File>();
		this.waitTimeMillis=waitTimeMillis;
	}
	
	
	/**
	 * Method to add specified file to zip
	 * @param filePath path to file
	 */
	public void addFile(String filePath){
		File file = new File(filePath);
		try{
			lock.lock();
			if (!files.contains(file)) {
				this.files.add(file);
				if(files.size()==1){
					Thread temp=new Thread(new ZipRunnable());
					temp.start();
				}
			}
			
		}
		finally{
			lock.unlock();
		}
	}

	
	/**
	 * @return tempFiles list on files
	 */
	private ArrayList<File> getAndClearFiles(){
		ArrayList<File> tempFiles=null;
		
		try{
			lock.lock();
			tempFiles = this.files;
			this.files=new ArrayList<File>();
		}finally{
			lock.unlock();
		}
		
		return tempFiles;
	}



	/**
	 * Class for executing thread for file zipper
	 * Contains run() method to implement the thread
	 * Sends archived files to Master Station
	 */
	class ZipRunnable implements Runnable{
		
		ArrayList<File> toZip=null;
		
		//run method to execute the thread
		public void run(){
			try {
				Thread.sleep(waitTimeMillis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//archive several files
			toZip=getAndClearFiles();
			//transmit archived file to Master Station
			GS_Manager.zipSender.send(toZip);
		}
	} // end of class
	
	
	/**
	 * Method to set file archiving frequency
	 * @param waitTime time to wait before zipping them
	 */
	public void setwaitTimeMillis(long waitTime) {
		this.waitTimeMillis = waitTime;
	}
	
} // end of class
