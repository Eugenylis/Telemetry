package Masterstation;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Master GUI class is a display of the main GUI seen by the user of the program. 
 * The user can view the connection status, add multiple stations, view plots with live data, save the data and plots.
 * 
 * @author Eliza Gazda
 * @version 1.0
 */

/**
 * @author GAZDAE
 *
 */
public class MasterGUI extends Application {
	
	/*  GUI ITEMS */
	// Pane
	private BorderPane borderPane;
	private Button btStation;
	private TextField txNameOfStation, txPortNum;
	
	// Menu
	private MenuBar menuBar; // MenuBar
	private Menu menuHelp, menuConnections, menuGroundStation; // Menus
	private MenuItem miHelp, miAddStation, miRemoveStation, miDataSincFreq, miChooseDirectory, miDisconnectAllStations; //MenuItems
	//VBox for Status Display
	private VBox stationVBox;
	private Label lbTitle, lbDirectory, lbSelectedDirectory;
	
	// TabPane
	private TabPane tabPanePlots;
	private Tab Plot;
	//plotGridPane
	private GridPane plotGridPane;
	
	
	/* COMMUNICATION ITEMS */
	//specified port number
	private int portNumber;
	/**
	 *  This method contains GUI main GUI pane, menu bar items, and VBox for stations
	 */
	public MasterGUI(){
		
		// Create the BorderPane
		borderPane = new BorderPane();
		//menu
		menuBar = new MenuBar();
		menuHelp = new Menu("Help");
		menuConnections = new Menu("Connection");
		menuGroundStation = new Menu("Setup Ground Stations");
		miAddStation = new MenuItem("Add Station");
		miChooseDirectory = new MenuItem("Choose Directory");
		miRemoveStation = new MenuItem("Remove Station");
		miHelp = new MenuItem("Help");
		miDataSincFreq = new MenuItem("Data Sinc Freguency");
		miDisconnectAllStations = new MenuItem("Disconnect All Stations");
		menuHelp.getItems().addAll(miHelp);
		menuConnections.getItems().addAll(miChooseDirectory, miDataSincFreq, miDisconnectAllStations);
		menuGroundStation.getItems().addAll(miAddStation, miRemoveStation);
		menuBar.getMenus().addAll(menuConnections, menuGroundStation, menuHelp);
		borderPane.setTop(menuBar);
		
		//menu items actions
		miDisconnectAllStations.setOnAction(arg0 -> MS_Manager.disconnectAllStations());
		miChooseDirectory.setOnAction(arg0 -> showDirectoryChooser());		
		miDataSincFreq.setOnAction(arg0 -> DataSincFerq());
		miAddStation.setOnAction(arg0 -> showAddStation());
		miHelp.setOnAction(arg0 -> showHelp());
		
		//show directory chosen
		lbDirectory = new Label();
		lbSelectedDirectory = new Label("File Directory:");
		lbSelectedDirectory.setFont(Font.font("Ariel", FontWeight.BOLD, 15));
		
		//VBox for station and details
		stationVBox = new VBox();
		lbTitle = new Label("Stations"); 
		lbTitle.setStyle("-fx-padding: 8 30 10 50; -fx-background-color: #f8ecc2; -fx-border-style: solid; -fx-border-width: 3");
		lbTitle.setFont(Font.font("Ariel", FontWeight.BOLD, 25));
		lbTitle.setMaxWidth(200);
		stationVBox.getChildren().addAll(lbSelectedDirectory, lbDirectory, lbTitle);
		stationVBox.setSpacing(10);
		stationVBox.setBorder(new Border(new BorderStroke(Color.SKYBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5) )));
		stationVBox.setMinWidth(200);
		borderPane.setLeft(stationVBox);
	
		
		//tabs for plot display/ data/ 
		tabPanePlots = new TabPane();			
	}		
		
	
	/* 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 * 
	 * Displays the main window with plots and data
	 */
	public void start(Stage stage) throws Exception {
	
		// Set the scene and the stage
		Scene scene = new Scene(borderPane, 1200, 675);
		stage.setScene(scene);
		stage.setTitle("Telemetry Master Interface");
		stage.setResizable(true);
						
		// Display the GUI
		stage.show();
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
	              System.out.println("Stage is closing");
	              MS_Manager.disconnectAllStations();
	  	        stage.close();

	          }
	      });        
	}
	
	
	/**
	 * Method that opens a window allowing the user to choose a directory for all data files.
	 */
	public void showDirectoryChooser(){
		//directory chooser created
		DirectoryChooser openDirectoryChooser = new DirectoryChooser();
		File selectedDirectory = openDirectoryChooser.showDialog(null);
		//directory path name is added to the MS_Manager
		lbDirectory.setText(selectedDirectory.getAbsolutePath());
		MS_Manager.dataLocation = selectedDirectory.getAbsolutePath().toString();
	}
	
	/**
	 * Method opens a window to display the add stations window where the user must input station name and station port number.
	 */
	public void showAddStation(){
		
		//if the user did not choose a directory this method will not open
		if (MS_Manager.dataLocation == null){
			//alert message for the chooser to pick a directory first
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			String s ="Please select a directory. Then try again.";
			alert.setContentText(s);
			alert.show();
			return;
		}
		
		//displayed features in the pop up window
		Label lbNameOfStation = new Label("Name of Station:");
		Label lbGPSNum = new Label("Port Number:");
		txNameOfStation = new TextField();
		txPortNum = new TextField();
		Button btAddStation = new Button("Add");
		btAddStation.setOnAction(arg0-> addStations()); 		//button adds stations to the main GUI view
		
		//pane for the features
		GridPane addStationPane = new GridPane();
		addStationPane.setHgap(10);
		addStationPane.setVgap(10);
		addStationPane.add(lbNameOfStation, 1, 1);
		addStationPane.add(txNameOfStation, 2, 1);
		addStationPane.add(lbGPSNum, 1, 2);
		addStationPane.add(txPortNum, 2, 2);
		addStationPane.add(btAddStation, 1, 3);
		
		// Create and display the pane in a new stage
		Scene scene = new Scene(addStationPane, 550, 100);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("Add Station to View");
		stage.setResizable(false);
		stage.show();
	}
	
	/**
	 * Method gives functionality to Add Station button, station is created on the main view in a column on left
	 */
	public void addStations(){
		
		//if user did not put in station name or port number a warning message appears
    	if (txNameOfStation.getText().isEmpty() || txPortNum.getText().isEmpty()){
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.setTitle("WAIT");
			String s ="Please enter a station name and port number!";
			alert.setContentText(s);
			alert.show();
			return;
    	}
    	
		//check if text typed in text box txProtNum is a 4-digit integer number
		if(MS_Manager.isPortAvailable(txPortNum.getText(),10)){
			
			//Get information given by the user
			portNumber = Integer.parseInt(txPortNum.getText());
			String stationName = txNameOfStation.getText();
			
			//create the column with station details
			VBox localStationDetailsVBox = new VBox();
			HBox stationHBox = new HBox();
			HBox stationDetailsHBox = new HBox();
			HBox stationButtonsHBox = new HBox();
			stationHBox.setSpacing(10);
			localStationDetailsVBox.setSpacing(10);
			localStationDetailsVBox.setPadding(new Insets (5, 2, 5, 16));
			stationDetailsHBox.setSpacing(5);
			stationButtonsHBox.setSpacing(5);
			Label lbStationDetails = new Label();
			btStation = new Button();
			btStation.setOnAction(arg0 -> btStationActions(stationName));
			Label lbPortNum = new Label("Port number:");
			CheckBox cbSelectStation = new CheckBox();
			Label lbStatus = new Label();
			
			//add a connect button to allow receiving data
			Button btConnect = new Button("Connect");
			btConnect.setStyle("-fx-background-color: mediumseagreen ");
			btConnect.setFont(Font.font("Ariel", FontWeight.BOLD, 12));
			//connection is made and status is showed when connection is made
			btConnect.setOnAction(new EventHandler<ActionEvent>() {
	            @Override public void handle(ActionEvent e) {
	            	MS_Manager.getStation(stationName).connect();
	            	if (MS_Manager.getStation(stationName).isConnected()){
	            		lbStatus.setText("Connected");
	            	}
			}});
			
			//button to allow disconnecting from the station
			Button btDisconnect = new Button("Disconnect");			
			btDisconnect.setStyle("-fx-background-color: crimson ");
			btDisconnect.setFont(Font.font("Ariel", FontWeight.BOLD, 12));
			btDisconnect.setOnAction(new EventHandler<ActionEvent>() {
	            @Override public void handle(ActionEvent e) {
	            	MS_Manager.getStation(stationName).disconnect();
	            	if (MS_Manager.getStation(stationName).isConnected() == false){
	            	lbStatus.setText("Disconnected");
	            	}
			}});
			
			//setup of the station details and features
			stationHBox.getChildren().add(0, cbSelectStation); //Forcing cbSelectStation to be in index 0 allows easy reading later.
			stationHBox.getChildren().add(1, btStation);
			stationHBox.getChildren().add(2, lbStatus);
			stationDetailsHBox.getChildren().addAll(lbPortNum, lbStationDetails);
			stationButtonsHBox.getChildren().addAll(btConnect, btDisconnect);
			localStationDetailsVBox.getChildren().add(0, stationHBox); //Forcing stationHBox  to be in index 0 allows easy reading later.
			localStationDetailsVBox.getChildren().add(1, stationDetailsHBox);
			localStationDetailsVBox.getChildren().add(2, stationButtonsHBox);
			btStation.setText(txNameOfStation.getText());
			lbStationDetails.setText(txPortNum.getText());
			stationVBox.getChildren().add(localStationDetailsVBox);
			

			//remove function
			miRemoveStation.setOnAction(arg0 -> removeSelectedStation());
			
			//create a receiver object with specified port number
			try {
				MS_Manager.createStation(portNumber, stationName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method allows the removal of a station when its corresponding check box is selected
	 */
	public void removeSelectedStation(){
			
		VBox station = new VBox();
		Iterator<Node> stationIterator = stationVBox.getChildren().iterator(); // Creates list like thing that allows it to me incremented 
		//through. Changes to it also changes the elements of stationVBox.

		while(stationIterator.hasNext()){
			// In the current setup, this while loop will loop through all elements of stationVBox. 
			// The first line in the try statement casts the element into a VBox, if the element is not a VBox then it will through an error
			// and be caught in the catch statement then continues through the loop. If the element is a VBox then it will continues to the next
			// line and assuming the above layout does not change (stationVBox(VBox) -> localStationDetailsVBox(VBox) -> stationHBox(HBox) -> cbSelectStation(CheckBox))
			try{
				station = (VBox) stationIterator.next();
				HBox hbox = (HBox) station.getChildren().get(0);
				CheckBox checkBox = (CheckBox) hbox.getChildren().get(0);
				if (checkBox.isSelected()){
					MS_Manager.removeStation(((Button)(hbox.getChildren().get(1))).getText()); //sends name of station which is on it's button
					stationIterator.remove(); //If checkBox is selected (as in it was checked), it is removed from the iterator (which is connected to the stationVBox) 
					//which removes it from stationVBox.
				}
				
			}catch (Exception e){

			}
		}
	}
	
	/**
	 * @param stationName : the name of each station set up by the user
	 * 
	 * This method allows each station button to open graph settings and setup the station plots
	 */
	public void btStationActions(String stationName){
		
		//check if station is connected and  show a message when not connected
		if (MS_Manager.getStation(stationName).isConnected() == false){
			//error message
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			String s ="Connection not established. Please try to connect again.";
			alert.setContentText(s);
			alert.show();
			return;
		}
		
		//labels and features in the plot settings window
		Label lbXAxis = new Label("X Axis");
		Label lbYAxis = new Label("Y Axis");
		Label lbPlot1 = new Label("Plot 1:");
		Label lbPlot2 = new Label("Plot 2:");
		Label lbPlot3 = new Label("Plot 3:");
		Label lbPlot4 = new Label("Plot 4:");
		
		ChoiceBox<String>[] cbPlotXAxis = new ChoiceBox[4];
		cbPlotXAxis[0] = new ChoiceBox<String>();	
		cbPlotXAxis[1] = new ChoiceBox<String>();
		cbPlotXAxis[2] = new ChoiceBox<String>();
		cbPlotXAxis[3] = new ChoiceBox<String>();
		
		ChoiceBox<String>[] cbPlotYAxis = new ChoiceBox[4];
		cbPlotYAxis[0] = new ChoiceBox<String>();		
		cbPlotYAxis[1] = new ChoiceBox<String>();
		cbPlotYAxis[2] = new ChoiceBox<String>();
		cbPlotYAxis[3] = new ChoiceBox<String>();

		// connect data and data type, to be viewed
		Station station;
        Iterator<Station> stationList = MS_Manager.stationArrayList.iterator();
        while(stationList.hasNext()){
        	station = stationList.next();
        	if(station.stationName.equals(stationName)){
        		cbPlotXAxis[0].getItems().addAll(station.receiver.dataHandler.sensorDataTypes);	
        		cbPlotXAxis[1].getItems().addAll(station.receiver.dataHandler.sensorDataTypes); 
        		cbPlotXAxis[2].getItems().addAll(station.receiver.dataHandler.sensorDataTypes); 
        		cbPlotXAxis[3].getItems().addAll(station.receiver.dataHandler.sensorDataTypes);
        		
        		break;
        	}
        }        
        
        cbPlotYAxis[0].getItems().add("Altitude"); 
		cbPlotYAxis[1].getItems().add("Altitude"); 
		cbPlotYAxis[2].getItems().add("Altitude"); 
		cbPlotYAxis[3].getItems().add("Altitude");
		Button btAddPlots = new Button("Add Plots");
		//add features to pane		
		GridPane addPlotGridPane = new GridPane();
		addPlotGridPane.setHgap(10);
		addPlotGridPane.setVgap(10);
		addPlotGridPane.add(lbXAxis, 2, 1);
		addPlotGridPane.add(lbYAxis, 3, 1);		
		addPlotGridPane.add(lbPlot1, 1, 2);
		addPlotGridPane.add(lbPlot2, 1, 3);
		addPlotGridPane.add(lbPlot3, 1, 4);
		addPlotGridPane.add(lbPlot4, 1, 5);
		addPlotGridPane.add(cbPlotXAxis[0], 2, 2);
		addPlotGridPane.add(cbPlotXAxis[1], 2, 3);
		addPlotGridPane.add(cbPlotXAxis[2], 2, 4);
		addPlotGridPane.add(cbPlotXAxis[3], 2, 5);
		addPlotGridPane.add(cbPlotYAxis[0], 3, 2);
		addPlotGridPane.add(cbPlotYAxis[1], 3, 3);
		addPlotGridPane.add(cbPlotYAxis[2], 3, 4);
		addPlotGridPane.add(cbPlotYAxis[3], 3, 5);
		addPlotGridPane.add(btAddPlots, 1, 6);		
		
		// Create and display said the aforementioned pane in a new stage
		Scene scene = new Scene(addPlotGridPane, 550, 300);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("Choose Plot Settings");
		stage.setResizable(false);
		stage.show();
		//plots are added to main GUI and stage is closed
		btAddPlots.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	addPlotsToGUI(stationName, cbPlotXAxis, cbPlotYAxis);
        		stage.close();

            }
        });
	}
	
	/**
	 * @param stationName : name of station chosen by the user
	 * @param cbPlotXAxis : choice box option for x axis in plots
	 * @param cbPlotYAxis : choice box option for x axis in plots
	 * 
	 * This method adds all plots for a specific station to the main GUI
	 */
	public void addPlotsToGUI(String stationName, ChoiceBox<String>[] cbPlotXAxis, ChoiceBox<String>[] cbPlotYAxis){		
		
		//plot tab with station name is created
		Plot = new Tab(stationName);		
		tabPanePlots.getTabs().addAll(Plot);
		Plot.setClosable(true);
		borderPane.setCenter(tabPanePlots);
        
		//Plots in tabPane Plot1
        plotGridPane = new GridPane();        		
		plotGridPane.setHgap(10);
        plotGridPane.setVgap(10);
        Plot.setContent(plotGridPane);
        
        //adding plots
        Station station;
        Iterator<Station> stationList = MS_Manager.stationArrayList.iterator();
        while(stationList.hasNext()){
        	if((station = stationList.next()).stationName.equals(stationName)){
        		//Plots in tabPane Plot1 (change the TilePane to some blank pane for the swing)
        		if(!(cbPlotXAxis[0].getValue()== null)){
		        	plotGridPane.add(station.getNewSwingNodePlot(cbPlotXAxis[0].getValue(), 0),1,1);  
		        }else{
		        	TilePane emptyplot= new TilePane();
		        	plotGridPane.add(emptyplot,1,1);
		        }
		        if(!(cbPlotXAxis[1].getValue()== null)){
		        	plotGridPane.add(station.getNewSwingNodePlot(cbPlotXAxis[1].getValue(), 1),1,2);  
		        }else{
		        	TilePane emptyplot= new TilePane();
		        	plotGridPane.add(emptyplot,1,2);
		        }
		        if(!(cbPlotXAxis[2].getValue()== null)){
		        	plotGridPane.add(station.getNewSwingNodePlot(cbPlotXAxis[2].getValue(), 2),2,1);  
		        }else{
		        	TilePane emptyplot= new TilePane();
		        	plotGridPane.add(emptyplot,2,1);
		        }
		        if(!(cbPlotXAxis[3].getValue()== null)){
		        	plotGridPane.add(station.getNewSwingNodePlot(cbPlotXAxis[3].getValue(), 3),2,2);  
		        }else{
		        	TilePane emptyplot= new TilePane();
		        	plotGridPane.add(emptyplot,2,2);
		        }
        		break;
        	}
        }
	}
	/**
	 * Data Frequency Window Displayed
	 */
	public void DataSincFerq(){
		//instructions
		final String descriptionText = "Input the value for the time equal to the amount of data rcieved from ground station:";
		// Create the text label
				Label aboutLabel = new Label();
				aboutLabel.setWrapText(true);
				aboutLabel.setTextAlignment(TextAlignment.CENTER);
				aboutLabel.setFont(Font.font("Times New Roman", 14));
				aboutLabel.setText(descriptionText);
				TextField tfInput = new TextField();
				tfInput.setMaxWidth(100);
		
		VBox pane = new VBox();
		pane.setPadding(new Insets (20, 2, 5, 20));
		pane.setSpacing(10);
		pane.getChildren().addAll(aboutLabel, tfInput);
		
		
		// Create and display said the aforementioned pane in a new stage
		Scene scene = new Scene(pane, 550, 100);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("Data Sinc Frequency");
		stage.setResizable(false);
		stage.show();
	}
	
	/**
	 * Displays Help window
	 */
	public void showHelp(){
		final String aboutText = "To use this program, follow these steps: click on 'Connection' -> 'Choose Directory' -> 'Select Directory', "
				+ "then: 'Setup Ground Stations' -> 'Add station' -> write name of your station and port number, then click 'Add', "
				+ "after that: click on 'Connect' to stat communication "
				+ "If the communication is established, click on the button which contains station name to see the plots ";
	
		// Create the text label
		Label aboutLabel = new Label();
		aboutLabel.setWrapText(true);
		aboutLabel.setTextAlignment(TextAlignment.LEFT);
		aboutLabel.setFont(Font.font("Times New Roman", 14));
		aboutLabel.setText(aboutText);
		aboutLabel.setMaxWidth(500);
	
		// Add the label to a StackPane
		StackPane pane = new StackPane();
		pane.getChildren().add(aboutLabel);
	
	
		// Create and display said the aforementioned pane in a new stage
		Scene scene = new Scene(pane, 600, 150);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("How to use this program");
		stage.setResizable(false);
		stage.show();
	}	
}