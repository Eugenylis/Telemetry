package Masterstation;

import java.io.IOException;

public class Station {
	
	public Receiver receiver;
	public String stationName = " ";
	
	
	
	/**
     * Constructor which creates socket with specified port number and specified name
     */
    public Station(int portNumber, String name, String saveLocation) {
    	
    	setStationName(name);
    	receiver = new Receiver(portNumber, name, saveLocation);
    }
    
    
    /**
     * Method to set the station name
     * @param name of the station
     */
    public void setStationName(String newName){
    	stationName = stationName.replace(stationName, newName);
    }
}
