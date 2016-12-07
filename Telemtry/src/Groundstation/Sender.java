package Groundstation;

import java.net.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.io.*;

/**
 * This class enables sending files to the Master Station using TCP/IP data transfer through sockets
 * Allows to zip multiple files into packet
 * Sends packet to the Master station using bit output stream
 * 
 * Based upon:
 * https://examples.javacodegeeks.com/core-java/util/zip/zipoutputstream/java-zip-file-example/
 * 
 * @author Erik Parker
 * @version 2.0
 *
 */
public class Sender {
	
	//IP address
	protected String IPaddress;
	//port (socket) number
	protected int portNum;
	//socket object for TCP/IP communication
	private Socket socket;
	
	
	//no-argument constructor
	public Sender(){
	}
	
	
	/**
	 * Constructor that creases a sender object based upon provided IP address of the Master Station and port number
	 * @param IPaddress The IP of the Master Station (server)
	 * @param portNum The socket number on the master side
	 * @throws IOException is something when wrong
	 */
	public Sender(String IPaddress, int portNum) throws IOException {
		this.IPaddress = IPaddress;
		this.portNum = portNum;
	}
	
	
	
	/**
	 * Method to handle all things required to send a file to the Master Station
	 * Uses sockets to send streams of data
	 * @param fileLocation The path of the file to be transfered
	 * @throws IOException
	 */
	public void send(ArrayList<File> files) {
		
        try {
        	//initialize socket
            this.socket = new Socket(this.IPaddress, this.portNum);
            
            //create output streams for sending data
            OutputStream os = socket.getOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(os);
            
            //zip multiple files together
            System.out.println("Zipping files...");
            for (File f : files){
            	if (f.isFile())
            		zipFile(f,zipOutputStream);
            }

           //clean up output streams
            System.out.println("Sending Files...");
            zipOutputStream.close();
            os.flush();
            os.close();
            System.out.println("File transfer complete");
            
        } catch (IOException ex) {
        	System.out.println("Sending failed!!!");
        	System.out.println(ex);
        }
	}
	
	
	/**
	 * Method to zip several files into archive file
	 * @param inputFile file to be zipped
	 * @param zipOutputStream object for zipping file streams
	 * @throws IOException
	 */
	public static void zipFile(File inputFile, ZipOutputStream zipOutputStream) {

        try {
            // A ZipEntry represents a file entry in the zip archive
            // We name the ZipEntry after the original file's name
            ZipEntry zipEntry = new ZipEntry(inputFile.getName());
            zipOutputStream.putNextEntry(zipEntry);
            //create new file stream and separate it into bytes
            FileInputStream fileInputStream = new FileInputStream(inputFile);
            byte [] bytearray  = new byte [(int)inputFile.length()];
            int bytesRead;

            // Read the input file and write the read bytes to the zip stream
            bytesRead = fileInputStream.read(bytearray);
            zipOutputStream.write(bytearray, 0, bytesRead);
            
            // close ZipEntry to store the stream to the file
            zipOutputStream.closeEntry();
            fileInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
	
	
	/**
	 * Method to disconnect Ground Station from communication
	 * Closes socket used to send files
	 */
	public void disconnect(){
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
} //end of class