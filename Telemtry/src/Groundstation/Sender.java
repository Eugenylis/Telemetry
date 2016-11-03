package Groundstation;
import java.net.*;
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
	private Socket socket;
	//made change
	/**
	 * @param IPaddress The IP of the Master side
	 * @param portNum The socket number on the master side
	 * @throws IOException Something when wrong
	 */
	public Sender(String IPaddress, int portNum) throws IOException {
		
		//System.out.println("With in sender " + IPaddress + " " + portNum);
		this.socket = new Socket(IPaddress, portNum);
		//this.socket = new Socket("155.31.132.40", 8748);
		//InputStream is = socket.getInputStream();
	}
	
	//This method handles all things required to send a file is supplied the file location
	/**
	 * @param fileLocation The path of the file to be transfered
	 * @throws IOException
	 */
	public void send(String fileLocation) throws IOException {
		
		File transferFile = new File (fileLocation);
		byte [] bytearray  = new byte [(int)transferFile.length()];
		FileInputStream fin = null;
///////////////////////
//        try {
//            fin = new FileInputStream(transferFile);
//        } catch (FileNotFoundException ex) {
//            // Do exception handling
//        }
//        BufferedInputStream bin = new BufferedInputStream(fin);
//
//        try {
//            bin.read(bytearray, 0, bytearray.length);
//            OutputStream os = socket.getOutputStream();
//            System.out.println("Sending Files...");
//            os.write(bytearray, 0, bytearray.length);
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
/////////////////////////////////////
		ZipEntry ze= new ZipEntry(fileLocation);

		try {
			fin = new FileInputStream(transferFile);
		} catch (FileNotFoundException ex) {
			// Do exception handling
		}

		try {
			fin.read(bytearray, 0, bytearray.length);
			OutputStream os = socket.getOutputStream();
			ZipOutputStream zos= new ZipOutputStream(os);
            zos.putNextEntry(ze);
            System.out.println("Sending Files...");
            zos.write(bytearray, 0, bytearray.length);
            os.flush();
            os.close();
            fin.close();
            zos.flush();
            zos.close();
            System.out.println("File transfer complete");

            return; // File sent, exit the main method
            
        } catch (IOException ex) {
            // Do exception handling
        }
        
        //begins writing a new zip file and sets the the position to the start of data
        
	}
		
	//This method safely closes the peer-to-peer connection	
	/**
	 * @throws IOException
	 */
	public void close() throws IOException {
		socket.close();
		System.out.println("Link disconnected");
	}
}




				