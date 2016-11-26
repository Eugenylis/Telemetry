package Masterstation;

//Some code from http://howtodoinjava.com/core-java/io/how-to-create-a-new-file-in-java/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DataHandler {
	
	String stationDataLocation;
	String plotDataLocation;
	String savedDataExt = ".txt";
	
	public DataHandler(String dataSaveLocation, String name) {
		this.stationDataLocation = dataSaveLocation + "\\" + name; //assumed to not have an ending \\
		this.plotDataLocation = stationDataLocation + "\\plotData";
		
		//Creates folder to store plot data if folder does not already exist
		File plotFolder = new File(plotDataLocation);
		if(!plotFolder.exists()){
			plotFolder.mkdir();
		}
	}
	
	public void addNewData(InputStream inputStream) throws IOException {
		
	        // create a buffer to store received data.
	        byte[] buffer = new byte[2048];
	        int len;

	        // open the zip file stream
	        ZipInputStream stream = new ZipInputStream(inputStream);

	        try
	        {

	            // Now iterate through each item in the stream. The getNextEntry 
	        	// call will return a ZipEntry for each file in the stream.
	        	// A ZipEntry is a file inside the zip folder/stream
	            ZipEntry entry;
	            while((entry = stream.getNextEntry())!=null)
	            {
//	            	String s = String.format("Entry: %s len %d added %TD",
//	                                entry.getName(), entry.getSize(),
//	                                new Date(entry.getTime()));
//	                System.out.println(s);

	                // Once we get the entry from the stream, the stream is
	                // positioned to read the raw data, and we keep
	                // reading until read returns 0 or less.
	                String outpath = stationDataLocation + "/" + entry.getName();
	                FileOutputStream output = null;
	                try
	                {
	                    output = new FileOutputStream(outpath);

	                    while ((len = stream.read(buffer)) > 0)
	                    {
	                        output.write(buffer, 0, len);
	                        //testPut.write(buffer, 0, len);
	                        //len = stream.read(buffer);
	                    }
	                    
	                }
	                finally
	                {
	                    // we must always close the output file
	                	if(output!=null) {
							output.close();
							addPlotdata(filterData(outpath));
							
	                    }
	                }
	            }
	        }
	        finally
	        {
	            // we must always close the zip file.
	            stream.close();
	        }
	}
	
	
	private String[] filterData(String filePath) throws IOException {
		String[] filteredData = new String[3];
		String[] linePart;
		FileInputStream fis = new FileInputStream(filePath);
		 
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String line = br.readLine();
		linePart = line.split(",");
		filteredData[0] = linePart[0];
		filteredData[1] = linePart[1];
		filteredData[2] = linePart[2];
	 
		br.close();
		return filteredData;
	}
	
	private void addPlotdata(String[] dataArray) throws IOException{
		
		String fileDataWillBeAddedTo = plotDataLocation + "\\" + dataArray[0] + savedDataExt;
		System.out.println(fileDataWillBeAddedTo);
		File file = new File(fileDataWillBeAddedTo);
		try {
			if(file.createNewFile()){
				//File has been created
			}else{
				//File already exists
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Write Content
		FileWriter writer = new FileWriter(file, true);
		writer.write(dataArray[1] + "," + dataArray[2] + "\n");
		writer.close();
		
	}
	
	
	
}
	
	
	
	
	
	
	
	