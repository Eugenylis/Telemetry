package Masterstation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DataHandler {
	
	String dataLocation;
	String plotDataLocation;
	String savedDataExt = ".txt";



	
	public DataHandler(String dataSaveLocation) {
		
		this.dataLocation = dataSaveLocation; //assumed to not have an ending //
		this.plotDataLocation = dataLocation + "//plotData";
	}
	
	public void addNewData(InputStream inputStream) throws IOException {
		
	        // create a buffer to improve copy performance later.
	        byte[] buffer = new byte[2048];
	        int len;

	        // open the zip file stream
	        ZipInputStream stream = new ZipInputStream(inputStream);

	        try
	        {

	            // now iterate through each item in the stream. The get next
	            // entry call will return a ZipEntry for each file in the
	            // stream
	            ZipEntry entry;
	            while((entry = stream.getNextEntry())!=null)
	            {
	                String s = String.format("Entry: %s len %d added %TD",
	                                entry.getName(), entry.getSize(),
	                                new Date(entry.getTime()));
	                System.out.println(s);

	                // Once we get the entry from the stream, the stream is
	                // positioned to read the raw data, and we keep
	                // reading until read returns 0 or less.
	                //File dataFile = null;
	                String outpath = dataLocation + "/" + entry.getName();
	                FileOutputStream output = null;
	                //FileOutputStream testPut = null;
	                try
	                {
	                    output = new FileOutputStream(outpath);
	                    //testPut = new FileOutputStream(dataFile);
						

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
							//testPut.close();
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
		String[] filteredData = null; ///////////////////////////////////////////////////////////////////////////////////
		String[] linePart;
		int i;
		
		FileInputStream fis = new FileInputStream(filePath);
		 
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	 
		linePart = br.readLine().split(",");
		
		filteredData[0] = linePart[0];
		filteredData[1] = linePart[1];
		filteredData[2] = linePart[2];
	 
		br.close();
		
		return filteredData;
	}
	
	private void addPlotdata(String[] dataArray) throws IOException{
		
		String fileDataWillBeAddedTo = plotDataLocation + "//" + dataArray[0] + savedDataExt;
		BufferedWriter writer;
		
		if(Files.exists(Paths.get(fileDataWillBeAddedTo))){
			writer = Files.newBufferedWriter(Paths.get(fileDataWillBeAddedTo));
			writer.write(dataArray[1] + "," + dataArray[2] + "\n");
		}else{
			writer = Files.newBufferedWriter(Paths.get(fileDataWillBeAddedTo));
			writer.write(dataArray[1] + "," + dataArray[2] + "\n");
		}
		writer.close();
		
	}
	
	
	
}
	
	
	
	
	
	
	
	