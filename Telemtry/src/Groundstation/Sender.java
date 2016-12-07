package Groundstation;

import java.net.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.io.*;

/**
 * This class enables sending files to the Master system using TCP/IP through sockets.
 * 
 * https://examples.javacodegeeks.com/core-java/util/zip/zipoutputstream/java-zip-file-example/
 * @author Erik Parker
 * @version 1.0
 *
 */
public class Sender {
	
	String IPaddress;
	int portNum;
	
	private Socket socket;
	
	
	
	
	/**
	 * @param IPaddress The IP of the Master side
	 * @param portNum The socket number on the master side
	 * @throws IOException Something when wrong
	 */
	public Sender(String IPaddress, int portNum) throws IOException {
		
		//System.out.println("With in sender " + IPaddress + " " + portNum);
		//this.socket = new Socket(IPaddress, portNum);
		this.IPaddress = IPaddress;
		this.portNum = portNum;
		//this.socket = new Socket("155.31.132.40", 8748);
		//InputStream is = socket.getInputStream();
	}
	
	//This method handles all things required to send a file is supplied the file location
	/**
	 * @param fileLocation The path of the file to be transfered
	 * @throws IOException
	 */
	public void send(ArrayList<File> files) {
		
        try {
            this.socket = new Socket(this.IPaddress, this.portNum);
            OutputStream os = socket.getOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(os);
            
            System.out.println("Zipping files...");
            for (File f : files){
            	if (f.isFile())
            		zipFile(f,zipOutputStream);
            }

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
	
	public static void zipFile(File inputFile, ZipOutputStream zipOutputStream) {

        try {
            // A ZipEntry represents a file entry in the zip archive
            // We name the ZipEntry after the original file's name
            ZipEntry zipEntry = new ZipEntry(inputFile.getName());
            zipOutputStream.putNextEntry(zipEntry);

            FileInputStream fileInputStream = new FileInputStream(inputFile);
            byte [] bytearray  = new byte [(int)inputFile.length()];
            int bytesRead;

            // Read the input file and write the read bytes to the zip stream
            bytesRead = fileInputStream.read(bytearray);
            zipOutputStream.write(bytearray, 0, bytesRead);
            
            //System.out.println("Zipping " + inputFile.getName());

            // close ZipEntry to store the stream to the file
            zipOutputStream.closeEntry();
            fileInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
	
	
	public void disconnect(){
		try {
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}




				