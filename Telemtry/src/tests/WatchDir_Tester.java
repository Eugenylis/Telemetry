package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class WatchDir_Tester {
	String fileParentPath = (new File("")).getAbsolutePath();
	Path dir = Paths.get(fileParentPath);
	String fileName = "test.txt";
	String filePath = fileParentPath + "\\" + fileName;
	File file;
	
	static public String foundFilePath;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		file = new File(filePath);
	      if(file.exists()){
	    	  file.delete(); // deletes file before test
	      }
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void test() {
		
		WatchDirTest watcher = null;
		try {
        	//create new watcher (directory listener) and start the thread. sleep to allow time to setup
			watcher = new WatchDirTest(dir, false);
			Thread thread = new Thread(watcher);
    		thread.start();
    		Thread.sleep(3000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {

		      file = new File(filePath); // creates file that should trigger the listener

		      if (file.createNewFile()){
		        System.out.println("File is created!");
		        
		      }else{
		        System.out.println("File already exists.");
		      }

	    	} catch (Exception e) {
	    		e.printStackTrace();
	    }

		try {
			Thread.sleep(3000); //sleep to allow time to setup
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		file.delete();
		
		assertEquals(foundFilePath, filePath.toString());
	}

}
