import java.net.*;
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
		
		// TODO change this part
		File transferFile = new File (fileLocation);
		byte [] bytearray1  = new byte [(int)transferFile.length()];
		FileInputStream fin = new FileInputStream(transferFile); // I am looking into this
		BufferedInputStream bin = new BufferedInputStream(fin);
		bin.read(bytearray1,0,bytearray1.length);
		OutputStream os = socket.getOutputStream();
		System.out.println("Sending Files...");
		os.write(bytearray1,0,bytearray1.length);
		os.flush();
		fin.close();
		bin.close();
		System.out.println("File transfer complete");
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




				