package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Groundstation.Sender;
import Groundstation.WatchDir;
import Groundstation.ZipFileTimer;

public class WatchDir_Tester {
	String fileParentPath = Paths.get("").toString();
	Path dir = Paths.get(fileParentPath);
	String fileName = "test.txt";
	String filePath = fileParentPath + "\\" + fileName;
	File file;
	
	static String foundFilePath;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		try {

		      file = new File(filePath);

		      if (file.createNewFile()){
		        System.out.println("File is created!");
		      }else{
		        System.out.println("File already exists.");
		      }

	    	} catch (Exception e) {
		      e.printStackTrace();
		}
	}

	@After
	public void tearDown() throws Exception {
		file.delete();
	}

	@Test
	public void test() {
		
		try {
			new WatchDirTest(dir, false).processEvents();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		assertEquals(foundFilePath, fileParentPath);
	}

}
