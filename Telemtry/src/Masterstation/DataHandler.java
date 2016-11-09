package Masterstation;

import java.io.BufferedReader;
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