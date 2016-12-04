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

public class GS_GUI extends Application {

    Stage window;
    String filename;
    Label filedirectoryinput = new Label();	
	String IPaddress;
	int socketNum;
	int Freq;

  
	/**
	 * Method create Gui for ground station  
	 * 
	 * @return file location in string and IPaddress,socketNum,Freq in Int
	 */
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Ground station");

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

        //File directory Input            
        
        filedirectoryinput.setFont(Font.font("Ariel", FontWeight.BOLD, 15));             
        GridPane.setConstraints(filedirectoryinput, 2, 3);
        
        //Send Button
        Button OpenButton = new Button("Open");
        GridPane.setConstraints(OpenButton, 3, 3);  
        
        //Send Button
        Button SendButton = new Button("Send");
        GridPane.setConstraints(SendButton, 1, 4);            
        

        //Add everything to grid
        grid.getChildren().addAll(Step1, IPaddresslabel, ipaddressInput, Step2, portnumberlabel, 
        		portInput, Step3, timelabel, timeInput, Step4, filedirectorylabel, filedirectoryinput, OpenButton, SendButton);

		OpenButton.setOnAction(new EventHandler<ActionEvent>() {
			/**
			 * Method allow user to choose file directory for ground station to read
			 * 
			 * @return file location in string
			 */
            @Override public void handle(ActionEvent e) {            	
        		DirectoryChooser openDirectoryChooser = new DirectoryChooser();        		
        		File selectedDirectory = openDirectoryChooser.showDialog(null);   
        		filedirectoryinput.setText(selectedDirectory.getAbsolutePath());   	    
        		filename = selectedDirectory.getAbsolutePath().toString();

		}});
		
		SendButton.setOnAction(new EventHandler<ActionEvent>() {
			/**
			 * Method send inputed value to main
			 * 
			 * 
			 * 
			 * @return file location in string and IPaddress,socketNum,Freq in Int
			 */
            @Override public void handle(ActionEvent e) {
				if(GS_Manager.isPortCorrect(portInput.getText(),10)){
					IPaddress = ipaddressInput.getText();
					Freq=Integer.parseInt(timeInput.getText());
					try{
						socketNum=Integer.parseInt(portInput.getText());
	
					} catch(Exception e2){
	
						JOptionPane.showMessageDialog(null, "Please Enter Valid Socket Number");
					}
					try{
						//System.out.println("Within GUI " + filename + " " + IPaddress + " " + socketNum);
						GS_Manager.setSettings(filename, IPaddress, socketNum,Freq);
	
					} catch(Exception e3){
	
						System.out.print("Broke1");
					}
				} // end of if
			

		}});
        
        Scene scene = new Scene(grid, 600, 200);
        window.setScene(scene);
        window.show();
    }
}
