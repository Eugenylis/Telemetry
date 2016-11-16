package Masterstation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DataHandler {
	
	String dataLocation;
	String plotDataLocation;



	
	public DataHandler(String dataSaveLocation) {
		
		this.dataLocation = dataSaveLocation;
		this.plotDataLocation = dataLocation + "//plotData";
	}
	
	public void addNewData(InputStream inputStream) throws IOException {
		
	        // create a buffer to improve copy performance later.
	        byte[] buffer = new byte[2048];

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
	                File dataFile = null;
	                String outpath = plotDataLocation + "/" + entry.getName();
	                FileOutputStream output = null;
	                FileOutputStream testPut = null;
	                try
	                {
	                    output = new FileOutputStream(outpath);
	                    testPut = new FileOutputStream(dataFile);
	                    int len;
						len = stream.read(buffer);

	                    while (len != 0)
	                    {
	                        output.write(buffer, 0, len);
	                        testPut.write(buffer, 0, len);
	                        len = stream.read(buffer);
	                    }
	                }
	                finally
	                {
	                    // we must always close the output file
	                    if(output!=null) {
							output.close();
							testPut.close();
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//	public static void main(String[] args) {
//
//	        // The name of the file to open.
//	        String fileName = "Temp.txt";
//
//	        // This will reference one line at a time
//	        String line = null;
//
//	        try {
//	            // FileReader reads text files in the default encoding.
//	            FileReader fileReader = new FileReader(fileName);
//
//	            // Always wrap FileReader in BufferedReader.
//	            BufferedReader bufferedReader = new BufferedReader(fileReader);
//
//	            while((line = bufferedReader.readLine()) != null) {
//	                System.out.println(line);
//	            }   
//
//	            // Always close files.
//	            bufferedReader.close();         
//	        }
//	        
//	        catch(FileNotFoundException ex) {
//	            System.out.println(
//	                "Unable to open file '" + 
//	                fileName + "'");                
//	        }
//	        catch(IOException ex) {
//	            System.out.println(
//	                "Error reading file '" 
//	                + fileName + "'");                  
//	            
//	        }
//	    }
	
/*	public void ScanData() throws IOException {
		//Scans in data from the text document test.txt
			//Here are the File Objects
			double[] dataPacket = new double[5]; //define number array
			File inputFile = new File("test.txt");
			int i; //general counting
			
	    	
			if(inputFile.exists()) {
				
				//Reads in characters from text document
				BufferedReader br = new BufferedReader(new FileReader(inputFile));

				//this reads everything character by character
				String line;
				String[] numLine;
				
				while ((line = br.readLine()) != null)  {
					numLine = line.split(" ");
					
					//dataPacket defined on line 24
					dataPacket = new double[numLine.length];
					for(i = 0; i < numLine.length; i++) {
						dataPacket[i] = Double.parseDouble(numLine[i]);
					}
					//Saves parsed data line and new objects
					BalloonData packet = new BalloonData(dataPacket);
					Manager.packetArray.add(packet);
				}
				br.close();
			}
		}
	
	
	
	/*public DataHandler(double[] dataArray){

		Altitude = dataArray[0];
		Temp = dataArray[1];
		Pressure = dataArray[2];
		Density = dataArray[3];
		SoundSpeed = dataArray[4];
	
	}
	private double
	Altitude, //meter
	Temp, //celsius
	Pressure, //bar
	Density, //Relative density P/(P of ground) 
	SoundSpeed; //m per s
	



	public double getAltitude() {
		return 	Altitude;
	}

	public double getTemp() {
		return Temp;
	}

	public double getPressure() {
		return Pressure;
	}

	public double getDensity() {
		return Density;
	}

	
	public double getSoundSpeed() {
		return SoundSpeed;
	}

	public void setTime(double altitude) {
		Altitude = altitude;
	}

	public void setTemp(double temp) {
		Temp = temp;
	}

	public void setPressure(double pressure) {
		Pressure = pressure;
	}

	public void setDensity(double density) {
		Density = density;
	}

	
	public void setSoundSpeed(double speed) {
		SoundSpeed = speed;
	}*/
	
}//end DataHandler