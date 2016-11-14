//Some code pulled from https://examples.javacodegeeks.com/core-java/util/zip/zipoutputstream/java-zip-file-example/


package Groundstation;

import java.net.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.io.*;

/**
 * This class enables sending files to the Master system using TCP/IP through sockets.
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
            
            for (File f : files){
            	if (f.isFile())
            		zipFile(f,zipOutputStream);
            }

            zipOutputStream.close();
            System.out.println("Sending Files...");

            os.flush();
            os.close();
            System.out.println("File transfer complete");

            socket.close();
            
        } catch (IOException ex) {
            // Do exception handling
        }
        
		
		
//		this.socket = new Socket(this.IPaddress, this.portNum);
//		byte [] bytearray  = new byte [(int)transferFile.length()];
//		FileInputStream fin;
//		ZipOutputStream zipOutputStream;
//		try {
//			fin = new FileInputStream(transferFile);
//        } catch (FileNotFoundException ex) {
//            // Do exception handling
//        	System.out.println("File transfer failed...");
//        	return;
//        }
//		
//		BufferedInputStream bin = new BufferedInputStream(fin);
//
//        try {
//            bin.read(bytearray, 0, bytearray.length);
//            OutputStream os = socket.getOutputStream();
//            zipOutputStream = new ZipOutputStream(os);
//            ZipEntry zipEntry = new ZipEntry(transferFile.getName());
//            zipOutputStream.putNextEntry(zipEntry);
//            System.out.println("Sending Files...");
//            zipOutputStream.write(bytearray, 0, bytearray.length);
//            
//            // close ZipEntry to store the stream to the file
//            zipOutputStream.closeEntry();
//
//            zipOutputStream.close();
//            os.flush();
//            os.close();
//            fin.close();
//            bin.close();
//            System.out.println("File transfer complete");
//
//            // File sent, exit the main method
//            return;
//        } catch (IOException ex) {
//            // Do exception handling
//        }
//        socket.close();
//		System.out.println("Link disconnected");
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
            
            System.out.println("Zipping " + inputFile.getName());

            // close ZipEntry to store the stream to the file
            zipOutputStream.closeEntry();
            fileInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
		
	//This method safely closes the peer-to-peer connection	
	/**
	 * @throws IOException
	 */
//	public void close() throws IOException {
//		socket.close();
//		System.out.println("Link disconnected");
//	}
}




				