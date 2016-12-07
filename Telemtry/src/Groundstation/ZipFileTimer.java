package Groundstation;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Erik Parker
 *
 */
public class ZipFileTimer {
	private ArrayList<File> files;
	private final ReentrantLock lock=new ReentrantLock(true);
	private long waitTimeMillis;
	
	/**
	 * 
	 */
	public ZipFileTimer(){
		files=new ArrayList<File>();
		waitTimeMillis=5000;
	}
	/**
	 * @param waitTimeMillis
	 */
	public ZipFileTimer(long waitTimeMillis){
		files=new ArrayList<File>();
		this.waitTimeMillis=waitTimeMillis;
	}
	
	/**
	 * @param filePath
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
	 * @return
	 */
	private ArrayList<File> getAndClearFiles(){
		ArrayList<File> tempFiles=null;
		
		try{
			lock.lock();
			tempFiles=this.files;
			this.files=new ArrayList<File>();
		}finally{
			lock.unlock();
		}
		
		return tempFiles;
	}



	class ZipRunnable implements Runnable{
		ArrayList<File> toZip=null;
		public void run(){
			try {
				Thread.sleep(waitTimeMillis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			toZip=getAndClearFiles();
			GS_Manager.zipSender.send(toZip);
		}
	}
	
	/**
	 * @param waitTime
	 */
	public void setwaitTimeMillis(long waitTime) {
		this.waitTimeMillis = waitTime;
	}
}
