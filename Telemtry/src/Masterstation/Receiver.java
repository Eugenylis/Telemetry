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
 * @author Eugene Lischuk
 * @version 2.0
 */


public class Receiver extends Thread {

	public DataHandler dataHandler;
	
	//Socket for communication
	protected Socket socket;
	//ServerSocket to accept incoming connections
    protected ServerSocket serverSocket;
    
	//value for controlling while loop
    protected boolean moreData = true;
    
    //counter to count how many times file was received
    protected static int counter = 0;
    
    //port number
    private int portNum;
    
    public String stationName = " ";

    //no-argument constructor for the thread with specified object name
    public Receiver() throws IOException {
    }

    
    /*
     * Constructor which creates socket with specified port number and specified name
     */
    public Receiver(int portNumber, String name, String saveLocation) {
    
        //specify directory to save files
        dataHandler = new DataHandler(saveLocation, name);
 
        setPortNum(portNumber);
        
        //set port number to server socket
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
	 * Creates new InputStream, FileOutputStream, BufferedOutputStream each time a file is sent
	 * Allows to continuously run and receive files
	 * @override run method in Thread class
	 * @throws IOException Something when wrong
	 */
    public void run() {
    
        while (moreData) {
            try {
            	           
            	System.out.println("Port Number " + portNum);
            	//allow to accept incoming connections
            	socket = serverSocket.accept();
                  
        		System.out.println("Accepted connection : " + socket);

    			
                //get input stream from a socket
    			InputStream inputStream = socket.getInputStream();
    			dataHandler.addNewData(inputStream);
    			
            } catch (IOException e) {
                //e.printStackTrace();
                //stop the loop
                moreData = false;
                try {
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
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
     * Method to close the socket to stop communication
     */
    public void closeSocket(){
    	try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
   
} // end of class