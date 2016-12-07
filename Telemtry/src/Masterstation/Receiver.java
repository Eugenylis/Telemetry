package Masterstation;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Receiver is a class that allows to receive files sent from another computer (Ground Station)
 * Creates a thread that allows parallel running with another programs
 * Allows to receive multiple files and store each at a different name
 * All communication is done through the use of sockets and server socket with specified port number
 * 
 * Based upon example from Oracle:
 * https://docs.oracle.com/javase/tutorial/networking/datagrams/clientServer.html
 * 
 * @author Yevgeniy Lischuk
 * @version 2.0
 * 
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
     *   
     * @param portNumber
     * @param name
     * @param saveLocation
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
	 * @throws IOException if something went wrong
	 */
    public void run() {
    
        while (getMoreData()) {
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
     * @param portNum
     */
    public void setPortNum(int portNum){
    	this.portNum = portNum;
    }
     
    
    /**
     * Method to set value used to control the loop inside the thread
     * moreData - boolean value to determine if any additional data is being received
     * @param data - true is more data incoming, false otherwise
     */
    public void setMoreData(boolean data){
    	this.moreData = data;
    }

    
    /**
     * Method to get the port number
     * @return port number for the specified object
     */
    public int getPortNum(){
    	return this.portNum;
    }
    
    
    /**
     * Method to return value used to control the loop inside the thread
     * @return moreData - boolean value to determine if any additional data is being received
     */
    public boolean getMoreData(){
    	return this.moreData;
    }
    
    
    /**
     * Method to open connection to start communication
     * Starts the Receiver thread if it haven't been started before
     */
    public void connect(){
    	//execute run() method in Receiver thread
    	if(!isAlive()){
    		start();
    	}
    }
    
    
    /**
     * Method to close the socket to stop communication
     * Ends thread by setting control variable to false
     * Closes socket, so server socket port closes as well
     */
    public void disconnect(){
    	try {
    		//set moreData to false
    		setMoreData(false);
    		//close the socket
			this.socket.close();
			//stops thread
			interrupt(); 
		} catch (IOException e) {
			e.printStackTrace();
			
			try {
				//in case thread won't stop, this will pause it forever
				wait(0);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
    }
    
   
} // end of class