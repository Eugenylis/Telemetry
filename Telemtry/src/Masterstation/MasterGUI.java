package Masterstation;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Master GUI class is a display of the main GUI seen by the user of the program. 
 * The user can view the connection with the station. Add files and view plots with live data.
 * @author Eliza Gazda
 * @version 1.0
 */

public class MasterGUI extends Application {
	

	public Receiver receiver;
	
	// Pane
	private BorderPane borderPane;
	private Button btStation;
	private TextField txNameOfStation, txGPSNum;
	
	// Menu
	private MenuBar menuBar; // MenuBar
	private Menu menuHelp, menuFile, menuSettings, menuConnections, menuGroundStation; // Menus
	private MenuItem miSave, miOpen, miHelp, miPlotData, miConnect, miAddStation, miRemoveStation, miStationSettings, miDataSincFreq;
	//VBox for Status Display
	private VBox stationVBox, stationDetailsVBox;
	private Label lbTitle;
	private CheckBox cbSelectStation;
	
	// TabPane
	private TabPane tabPanePlots;
	private Tab Plot1, Plot2;
	//plotGridPane
	private GridPane plotGridPane;

	
	private Axis<Number> xAxis;
	private Axis<Number> yAxis;
	private LineChart<Number,Number> lineChart;
	
	/**
	 * @param args
	 * 
	 * Launches the GUI
	 */
	public static void main(String[] args) { launch(args);}
	
	/**
	 *  The method that contains all GUI details
	 */
	public MasterGUI(){
		
		// Create the BorderPane
		borderPane = new BorderPane();
		//menu
		menuBar = new MenuBar();
		menuHelp = new Menu("Help");
		menuFile = new Menu("File");
		menuSettings = new Menu("Settings");
		menuConnections = new Menu("Connection");
		menuGroundStation = new Menu("Settup Ground Stations");
		miAddStation = new MenuItem("Add Station");
		miRemoveStation = new MenuItem("Remove Station");
		miStationSettings = new MenuItem("Settings");
		miSave = new MenuItem("Save");
		miOpen = new MenuItem("Open");
		miHelp = new MenuItem("Help");
		miConnect = new MenuItem("Connect");
		miDataSincFreq = new MenuItem("Data Sinc Freguency");
		miPlotData = new MenuItem("Plot");
		menuFile.getItems().addAll(miSave, miOpen);
		menuHelp.getItems().addAll(miHelp);
		menuConnections.getItems().addAll(miConnect, miDataSincFreq);
		menuSettings.getItems().addAll(miPlotData);
		menuGroundStation.getItems().addAll(miAddStation, miRemoveStation, miStationSettings);
		menuBar.getMenus().addAll(menuFile, menuSettings, menuConnections, menuGroundStation, menuHelp);
		borderPane.setTop(menuBar);
		
		miDataSincFreq.setOnAction(arg0 -> DataSincFerq());
		miAddStation.setOnAction(arg0 -> showAddStation());
		miHelp.setOnAction(arg0 -> showHelp());
		miConnect.setOnAction(arg0 -> {
			try {
				Connect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		//Status Display
		stationVBox = new VBox();
		lbTitle = new Label("Stations"); 
		lbTitle.setStyle("-fx-padding: 8 30 10 50; -fx-background-color: #f8ecc2; -fx-border-style: solid; -fx-border-width: 3");
		lbTitle.setFont(Font.font("Ariel", FontWeight.BOLD, 25));
		lbTitle.setMaxWidth(200);
		stationVBox.getChildren().addAll(lbTitle);
		stationVBox.setBorder(new Border(new BorderStroke(Color.SKYBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5) )));
		stationVBox.setMinWidth(200);
		borderPane.setLeft(stationVBox);
	
		
		//tabs for plot display/ data/ 
		tabPanePlots = new TabPane();
		Plot1 = new Tab("Plot1");		
		Plot2 = new Tab("Plot2");
		tabPanePlots.getTabs().addAll(Plot1, Plot2);
		Plot1.setClosable(false);
		borderPane.setCenter(tabPanePlots);
		
		
		/****SAMPLE PLOT*********************************/
		//defining the axes
	    xAxis = new NumberAxis();
	    yAxis = new NumberAxis();
	    yAxis.setLabel("Velocity");
	    xAxis.setLabel("Time");
	    //creating the chart
	    lineChart = new LineChart<Number,Number>(xAxis,yAxis);
	    lineChart.setTitle("Velocity vs Time");
	    //defining a series
	    XYChart.Series series = new XYChart.Series();
        //populating the series with data
        series.getData().add(new XYChart.Data(1, 23));
        series.getData().add(new XYChart.Data(2, 14));
        series.getData().add(new XYChart.Data(3, 15));
        series.getData().add(new XYChart.Data(4, 24));
        series.getData().add(new XYChart.Data(5, 34));
        series.getData().add(new XYChart.Data(6, 36));
        series.getData().add(new XYChart.Data(7, 22));
        series.getData().add(new XYChart.Data(8, 45));
        series.getData().add(new XYChart.Data(9, 43));
        series.getData().add(new XYChart.Data(10, 17));
        series.getData().add(new XYChart.Data(11, 29));
        series.getData().add(new XYChart.Data(12, 25));
	        
        lineChart.getData().add(series);
      //Plots in tabPane Plot1
      		plotGridPane = new GridPane();
      		Plot1.setContent(plotGridPane);
      		plotGridPane.add(lineChart, 1, 1);
      		lineChart.setVisible(false);
	}
	
	
	/* 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 * 
	 * Displays the main window with plots and data
	 */
	public void start(Stage stage) throws Exception {
	
		// Set the scene and the stage
		Scene scene = new Scene(borderPane, 1000, 600);
		stage.setScene(scene);
		stage.setTitle("Telemetry Master Interface");
		stage.setResizable(false);
		
		
				
		// Display the GUI
		stage.show();
	}
	
	public void showAddStation(){
		
		Label lbNameOfStation = new Label("Name of Station:");
		Label lbGPSNum = new Label("GPS Number:");
		txNameOfStation = new TextField();
		txGPSNum = new TextField();
		Button btAddStation = new Button("Add");
		btAddStation.setOnAction(arg0 -> addStations());
		
		
		GridPane addStationPane = new GridPane();
		addStationPane.setHgap(10);
		addStationPane.setVgap(10);
		addStationPane.add(lbNameOfStation, 1, 1);
		addStationPane.add(txNameOfStation, 2, 1);
		addStationPane.add(lbGPSNum, 1, 2);
		addStationPane.add(txGPSNum, 2, 2);
		addStationPane.add(btAddStation, 1, 3);

		
		// Create and display said the aforementioned pane in a new stage
		Scene scene = new Scene(addStationPane, 550, 100);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("Add Station to View");
		stage.setResizable(false);
		stage.show();
	}
	
	public void addStations(){
		
		stationDetailsVBox = new VBox();
		HBox stationHBox = new HBox();
		HBox stationDetailsHBox = new HBox();
		stationHBox.setSpacing(30);
		stationDetailsVBox.setSpacing(10);
		stationDetailsVBox.setPadding(new Insets (5, 2, 5, 16));
		stationDetailsHBox.setSpacing(5);;
		Label lbStationDetails = new Label();
		btStation = new Button();
		btStation.setOnAction(arg0 -> btStationActions());
		Label lbGPS = new Label("GPS number:");
		cbSelectStation = new CheckBox();
		if (cbSelectStation.isSelected()){
			miRemoveStation.setOnAction(arg0 -> removeSelectedStation());
		}
		stationHBox.getChildren().addAll(cbSelectStation, btStation);
		stationDetailsHBox.getChildren().addAll(lbGPS, lbStationDetails);
		stationDetailsVBox.getChildren().addAll(stationHBox, stationDetailsHBox);
		btStation.setText(txNameOfStation.getText());
		lbStationDetails.setText(txGPSNum.getText());
		stationVBox.getChildren().addAll(stationDetailsVBox);
				
		
	}
	public void removeSelectedStation(){
		System.out.print("jhgf");
			VBox.clearConstraints(stationDetailsVBox);
	}
	
	public void btStationActions(){
		if (lineChart.isVisible() == true){
			lineChart.setVisible(false);
		}
		else {
			lineChart.setVisible(true);
		};
	}
	
	/**
	 * Displays DataSincFreq window
	 * @return 
	 */
	public void DataSincFerq(){
		
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
		final String aboutText = "blah blah blah";
	
		// Create the text label
		Label aboutLabel = new Label();
		aboutLabel.setWrapText(true);
		aboutLabel.setTextAlignment(TextAlignment.CENTER);
		aboutLabel.setFont(Font.font("Times New Roman", 14));
		aboutLabel.setText(aboutText);
	
		// Add the label to a StackPane
		StackPane pane = new StackPane();
		pane.getChildren().add(aboutLabel);
	
	
		// Create and display said the aforementioned pane in a new stage
		Scene scene = new Scene(pane, 550, 100);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("How to use this program");
		stage.setResizable(false);
		stage.show();
	}
	
	/**
	 * Displays Connections window
	 */
	private void showConnections(){
		
		GridPane connectionGrid = new GridPane();
		connectionGrid.setAlignment(Pos.CENTER);
		
		// Create the text label
		Label connectionStat = new Label("Connection Status");
		connectionStat.setTextAlignment(TextAlignment.LEFT);
		connectionStat.setFont(Font.font("Times New Roman", 14));
		connectionGrid.add(connectionStat, 1, 1);
		
	
		// Create and display said the aforementioned pane in a new stage
		Scene scene = new Scene(connectionGrid, 800, 500);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("Connections Settings");
		stage.setResizable(false);
		stage.show();
	}
	
	/**
	 * @throws IOException
	 * 
	 * Allows connection to the Ground Station
	 */
	public void Connect() throws IOException{
		
//		client = new Client(9040);
//		client.startComm();
//		client.writeToFile();
//		client.closeSocket();
		
	}
}
