package Masterstation;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Receiver is a class that allows to receive files sent from another computer (ground station)
 * It creates a thread that allows parallel running with another programs
 * It also allows to receive multiple files and store each at a different name
 * All communication is done through the use of sockets like server socket with specified port number
 * 
 * @author Yevgeniy Lischuk
 * @version 2.0 of the Receiver
 */


public class Receiver extends Thread {

	//Data handler to process all received data
	public DataHandler dataHandler;
	//Socket for communication
	protected Socket socket;
	//ServerSocket to accept incoming connections
    protected ServerSocket serverSocket;
	//value for controlling while loop, if more data incoming = true
    protected boolean moreData = true;
    //counter to count how many times file was received
    protected static int counter = 0;
    //port number
    private int portNum;


    //no-argument constructor
    public Receiver() throws IOException {
    }

    
    /*
     * Constructor which creates socket with specified port number, specified name and location to save the data
     */
    public Receiver(int portNumber, String name, String saveLocation) {
    
        //specify directory to save files
        dataHandler = new DataHandler(saveLocation, name);
        
        //set the port number
        setPortNum(portNumber);
        
        //create server socket object with specified port number
        try {
			serverSocket = new ServerSocket(portNum);
		} catch (IOException e) {
			e.printStackTrace();
		} 
        
        //create socket for communication
        socket = new Socket();
    }

    
    /**
	 * Method to implement the thread
	 * Accepts incoming connections
	 * Uses dataHandler to process the received data
	 * Allows to continuously run and receive files
	 * @override run method in Thread class
	 * @throws IOException Something when wrong
	 */
    public void run() {
    
        while (moreData) {
            try {      
            	//allow to accept incoming connections
            	socket = serverSocket.accept();
            	//print out number for accepted connection
        		System.out.println("Accepted connection : " + socket);

                //get input stream from a socket
    			InputStream inputStream = socket.getInputStream();
    			//get data from input stream and process it with data handler
    			dataHandler.addNewData(inputStream);
    			
            } catch (IOException e) {
                //stop the loop
                moreData = false;
                //close the socket
                try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
                
            } 
            
        } // end of while(moreData)
    } // end of run()

    
    /**
     * Method to set the port number to a new value
     * @param portNum number of the port
     */
    public void setPortNum(int portNum){
    	this.portNum = portNum;
    }
     

    /**
     * Method to get the port number
     * @return port number for the specified object
     */
    public int getPortNum(){
    	return this.portNum;
    }
    
    
    /**
     * Method to open connection to start communication
     */
    public void connect(){
    	//execute run() method in Receiver thread
    	if(!isAlive()){
    		start();
    	}
    }
    
    
    /**
     * Method to close the socket to stop communication
     * Closes socket, so server socket port closes as well
     */
    public void disconnect(){
    	try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
   
} // end of class