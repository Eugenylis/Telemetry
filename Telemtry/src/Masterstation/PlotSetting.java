package Masterstation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PlotSetting extends Application {

    Stage window;
    Scene scene;
    Button button;
    ComboBox<String> XaxisComboBox;
    ComboBox<String> YaxisComboBox;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Plot setting");
        Button button = new Button("Plot");          
        

        final ComboBox<String> XaxisComboBox = new ComboBox<String>();      
        XaxisComboBox.getItems().add("GPS");      
        File temp = new File("E:\\java\\matlab test\\Name.txt");
		try (BufferedReader br = new BufferedReader(new FileReader(temp))) {
		    String text;
	        while((text= br.readLine()) != null) {		
	        	XaxisComboBox.getItems().add(text);
	        }

		}catch(IOException e){
			e.printStackTrace();
		}
		XaxisComboBox.setPromptText("Choose X Axis");      
				
        final ComboBox<String> YaxisComboBox = new ComboBox<String>();      
        YaxisComboBox.getItems().add( "GPS");        
        
        File temp1 = new File("E:\\java\\matlab test\\Name.txt");
		try (BufferedReader br = new BufferedReader(new FileReader(temp1))) {
		    String text1;
	        while((text1= br.readLine()) != null) {		
	        	YaxisComboBox.getItems().add(text1);
	        }

		}catch(IOException e){
			e.printStackTrace();
		}

		YaxisComboBox.setPromptText("Choose Y Axis");      
        					
		button.setOnAction( e -> UserSorter());
		
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(XaxisComboBox, YaxisComboBox, button);
        
        
        scene = new Scene(layout, 300, 250);
        window.setScene(scene);
        window.show();
    }
     

//DataHandler
     private void UserSorter(){
    	System.out.println(XaxisComboBox.getValue());
    	
//     	String userXchoice=XaxisComboBox.getValue();
//     	System.out.println(userXchoice);
//    	String userYchoice = YaxisComboBox.getValue();
//    	System.out.println(userYchoice); 
//    	String GPS = "GPS";
//    	String fileloc="E:\\java\\matlab test\\test2.txt";
//    	 if ( userXchoice.equals(GPS)) {
//    		  System.out.println("User choose Gps");
//    		}
//    		else {
//    			System.out.println(userXchoice);    
//    		} 
//    	 if ( userYchoice.equals(GPS)) {
//   		  System.out.println("User choose Gps");
//   		}
//   		else {
//   			System.out.println(userYchoice);    
//   		} 
     }

     }