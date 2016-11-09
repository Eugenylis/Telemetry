package Masterstation;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver extends Thread {

    protected DatagramSocket socket = null;
    protected boolean moreQuotes = true;

   
    ServerSocket serverSocket;
    Socket socket2;
    
    
    public static int counter = 0;
    
    public Receiver() throws IOException {
	this("Receiver");
    }

    public Receiver(String name) throws IOException {
        super(name);
        //socket = new DatagramSocket(4445);
        serverSocket = new ServerSocket(9040); 
        socket2 = new Socket();
    }

    public void run() {

        while (moreQuotes) {
            try {
              

                // THIS LINES ARE FOR RECEIVING REQUEST FROM THE GS
                // receive request
            	//byte[] buf = new byte[256];
            	//DatagramPacket packet = new DatagramPacket(buf, buf.length);
                //socket.receive(packet);

            	
            	
            	
            	// CODE BELOW IS COPIED FROM STABLE VERSION OF CLIENT
            	//Socket socket2 = new Socket();
                int filesize = 1022386;
            	int bytesRead = 0;
            	int currentTot = 0;
            	
            	socket2 = serverSocket.accept();
                
                System.out.println("test");

        		System.out.println("Accepted connection : " + socket2);
                byte [] bytearray = new byte [filesize];
    			
    			System.out.println("1");
    			InputStream inputStream = socket2.getInputStream();
    			
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
    			
    			socket2.close();

    			System.out.println("--------------------------------------Done");
    			counter++;
    			System.out.println("Counter is: " + counter);
            } catch (IOException e) {
                e.printStackTrace();
		moreQuotes = false;
            }
           
        }
       // socket.close();
    }

   
}