package Masterstation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DataHandler {


	public static void main(String[] args) {

	        // The name of the file to open.
	        String fileName = "Temp.txt";

	        // This will reference one line at a time
	        String line = null;

	        try {
	            // FileReader reads text files in the default encoding.
	            FileReader fileReader = new FileReader(fileName);

	            // Always wrap FileReader in BufferedReader.
	            BufferedReader bufferedReader = new BufferedReader(fileReader);

	            while((line = bufferedReader.readLine()) != null) {
	                System.out.println(line);
	            }   

	            // Always close files.
	            bufferedReader.close();         
	        }
	        
	        catch(FileNotFoundException ex) {
	            System.out.println(
	                "Unable to open file '" + 
	                fileName + "'");                
	        }
	        catch(IOException ex) {
	            System.out.println(
	                "Error reading file '" 
	                + fileName + "'");                  
	            
	        }
	    }
	
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