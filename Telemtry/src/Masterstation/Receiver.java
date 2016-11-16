package Masterstation;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
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

	//Socket for communication
	protected Socket socket;
	//ServerSocket to accept incoming connections
    protected ServerSocket serverSocket;
    
	//value for controlling while loop
    protected boolean moreData = true;
    
    //counter to count how many times file was received
    protected static int counter = 0;
    
    //port number
    private int portNum = 9040; //TODO ---------------------------------------delete and put into GUI
    //maximum file size to be received in bytes
    private int fileSize = 1022386;
    
    
    //no-argument constructor for the thread with specified object name
    public Receiver() throws IOException {
	this("Receiver");
    }

    
    /*
     * Constructor which creates socket with specified port number and specified name
     */
    public Receiver(String name) throws IOException {
        super(name);
 
        //set port number to server socket
        serverSocket = new ServerSocket(portNum); 
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
            	
            	int bytesRead = 0;
            	int currentTot = 0;
            	
            	//allow to accept incoming connections
            	socket = serverSocket.accept();
                
                System.out.println("test");

        		System.out.println("Accepted connection : " + socket);
                byte [] bytearray = new byte [fileSize];
    			
    			System.out.println("1");
    			InputStream inputStream = socket.getInputStream();
    			
    			System.out.println("2");
    			FileOutputStream fos = new FileOutputStream("test_" + counter + ".txt");
    			
    			System.out.println("3");
    			BufferedOutputStream bos = new BufferedOutputStream(fos);
    			
    			System.out.println("4");
    			bytesRead = inputStream.read(bytearray,0,bytearray.length); 
    			
    			System.out.println("5");
    			currentTot = bytesRead; 
    			
    			System.out.println("Writing to a file");
    			do{    				
    				System.out.println("6");
    				bytesRead = inputStream.read(bytearray, currentTot, (bytearray.length-currentTot)); 
    				
    				System.out.println("7");
    				if(bytesRead >= 0) currentTot += bytesRead; 
    				
    				System.out.println("8");
    			} while(bytesRead != -1);

    			System.out.println("9");
    			bos.write(bytearray, 0 , currentTot);
    			System.out.println("10");
    			bos.flush();
    			System.out.println("11");
    			bos.close();
    			
    			//close the socket
    			socket.close();

    			System.out.println("--------------------------------------Done");
    			counter++;
    			System.out.println("Counter is: " + counter);
    			
            } catch (IOException e) {
                e.printStackTrace();
                //stop the loop
                moreData = false;
            }
        }

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
    
   
} // end of class