package Groundstation;

import java.io.File;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * GS_GUI is the class used to create GUI for Ground Station
 * Creates GUI with text boxes for IP address, port number and data sync frequency
 * Contains buttons to send data and open file directory to listen for file additions
 * 
 * @author Victor Wong
 * @version 2.0
 * 
 */
public class GS_GUI extends Application {

	//main window
    private Stage window;
    //path to file
    private String pathName;
    //label for file directory
    private Label filedirectoryinput = new Label();	
    //IP address of the master station
	private String IPaddress;
	//port (socket) number
	private int socketNum;
	//data sync frequency
	private int syncFreq;

  
	/**
	 * Method create and display GUI for Ground Station  
	 */
    public void start(Stage primaryStage) throws Exception {
    	
    	//create primary stage
        window = primaryStage;
        window.setTitle("Ground station");
        
        //event handler stop executing everything when primary stage is closed
        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
	        	//close main window
	            window.close();
	            //exit the system
	  	        System.exit(0);
	          }
	      });  
        
        //GridPane with 10px padding around edge
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        //Step 1 label
        Label Step1 = new Label("Step 1");        
        GridPane.setConstraints(Step1, 0, 0);  
        
        //Label IPaddress        
        Label IPaddresslabel = new Label("Enter IP address");        
        GridPane.setConstraints(IPaddresslabel, 1, 0);     
       
        //IPaddress input
        TextField ipaddressInput = new TextField();
        ipaddressInput.setPromptText("155.31.132.40");
        GridPane.setConstraints(ipaddressInput, 2, 0);

        //Step 2 label
        Label Step2 = new Label("Step 2");        
        GridPane.setConstraints(Step2, 0, 1);  
        
        //Port Number Label
        Label portnumberlabel = new Label("Enter port number:");        
        GridPane.setConstraints(portnumberlabel, 1, 1);

        //Port Number Input
        TextField portInput = new TextField();     
        portInput.setPromptText("9030");
        GridPane.setConstraints(portInput, 2, 1);
        
        //Step 3 label
        Label Step3 = new Label("Step 3");        
        GridPane.setConstraints(Step3, 0, 2);  
        
        //Port Number Label
        Label timelabel = new Label("Enter time interval that file send in sec:");        
        GridPane.setConstraints(timelabel, 1, 2);

        //Port Number Input
        TextField timeInput = new TextField("1");        
        GridPane.setConstraints(timeInput, 2, 2);
        
        //Step 4 label
        Label Step4 = new Label("Step 4");        
        GridPane.setConstraints(Step4, 0, 3);
        
        //File directory Label
        Label filedirectorylabel = new Label("Choose Directory to listen to:");        
        GridPane.setConstraints(filedirectorylabel, 1, 3);

        //File directory Input text         
        filedirectoryinput.setFont(Font.font("Ariel", FontWeight.BOLD, 15));             
        GridPane.setConstraints(filedirectoryinput, 2, 3);
        
        //Open file directory Button
        Button OpenButton = new Button("Open");
        GridPane.setConstraints(OpenButton, 3, 3);  
        
        //Send Button
        Button SendButton = new Button("Start");
        GridPane.setConstraints(SendButton, 1, 4);         
        
        
        //Add everything to grid
        grid.getChildren().addAll(Step1, IPaddresslabel, ipaddressInput, Step2, portnumberlabel, 
        		portInput, Step3, timelabel, timeInput, Step4, filedirectorylabel, filedirectoryinput, OpenButton, SendButton);
        
        
        //event handler for the Open button
        //opens up a window to sear file directory
		OpenButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	//create a window to choose directory
        		DirectoryChooser openDirectoryChooser = new DirectoryChooser();        		
        		File selectedDirectory = openDirectoryChooser.showDialog(null);   
        		filedirectoryinput.setText(selectedDirectory.getAbsolutePath());   	    
        		pathName = selectedDirectory.getAbsolutePath().toString();
		}});
		
		
		//Event handler for the Send button
		//Sends data to the master station using socket
		SendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	
            	//check is port and IP numbers are correct
				if(GS_Manager.isPortCorrect(portInput.getText(),10) && GS_Manager.ipValid(ipaddressInput.getText())){
					//get text input from IP address text box
					IPaddress = ipaddressInput.getText();
					//get text input from syncFreq text box
					syncFreq = Integer.parseInt(timeInput.getText());
					//get text input from socket number text box
					socketNum = Integer.parseInt(portInput.getText());
	
					//start creating all objects needed for sending data in GS_Manager class
					try{
						GS_Manager.setSettings(pathName, IPaddress, socketNum,syncFreq);
	
					} catch(Exception e3){
						e3.printStackTrace();
					}
					
				//if port is not correct, display box with an error message	
				} else if(GS_Manager.isPortCorrect(portInput.getText(),10)){
					JOptionPane.showMessageDialog(null, "Your IP number format is incorrect, please enter valid IP number");
				//if IP address is not correct, display box with an error message	
				} else if(GS_Manager.ipValid(ipaddressInput.getText())){
					JOptionPane.showMessageDialog(null, "Your Port number format is incorrect, please enter valid Port number");
				}
				//if port and IP are not correct, display box with an error message	
				else{
					JOptionPane.showMessageDialog(null, "Your IP and Port number format is incorrect, please enter valid IP and Port number");
				}
		}});
		
		//set scene onto the main stage
        Scene scene = new Scene(grid, 600, 200);
        window.setScene(scene);
        window.show();
    }
}
