package Masterstation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.SwingUtilities;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
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
	private TextField txNameOfStation, txPortNum;
	
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
	private Tab Plot;
	//plotGridPane
	private GridPane plotGridPane;
	
	//for plot
	static int Xcount=1;
	static int Ycount=1; // counting number of line print
	static int counter=100;
	static String file1="E:\\java\\matlab test\\test1.txt";
	static String file2="E:\\java\\matlab test\\test2.txt";
	
	private Axis<Number> xAxis1, xAxis2, xAxis3, xAxis4;
	private Axis<Number> yAxis1, yAxis2, yAxis3, yAxis4;
	private LineChart<Number,Number> lineChart1, lineChart2, lineChart3, lineChart4;
	
	//for Communications
	int portNumber;
	
	
	/**
	 * @param args
	 * 
	 * Launches the GUI
	 */
	//public static void main(String[] args) { launch(args);}
	
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
		menuGroundStation = new Menu("Setup Ground Stations");
		miAddStation = new MenuItem("Add Station");
		miRemoveStation = new MenuItem("Remove Station");
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
		menuGroundStation.getItems().addAll(miAddStation, miRemoveStation);
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
		
		
		
		
		/****SAMPLE PLOT*********************************/
		//defining the axes
	    xAxis1 = new NumberAxis();
	    yAxis1 = new NumberAxis();
	    yAxis1.setLabel("Velocity");
	    xAxis1.setLabel("Time");
	    //creating the chart
	    lineChart1 = new LineChart<Number,Number>(xAxis1,yAxis1);
	    lineChart1.setTitle("Velocity vs Time");
	    //defining a series
	    XYChart.Series series1 = new XYChart.Series();
        //populating the series with data
        series1.getData().add(new XYChart.Data(1, 23));
        series1.getData().add(new XYChart.Data(2, 14));
        series1.getData().add(new XYChart.Data(3, 15));
        series1.getData().add(new XYChart.Data(4, 24));
        series1.getData().add(new XYChart.Data(5, 34));
        series1.getData().add(new XYChart.Data(6, 36));
        series1.getData().add(new XYChart.Data(7, 22));
        series1.getData().add(new XYChart.Data(8, 45));
        series1.getData().add(new XYChart.Data(9, 43));
        series1.getData().add(new XYChart.Data(10, 17));
        series1.getData().add(new XYChart.Data(11, 29));
        series1.getData().add(new XYChart.Data(12, 25));
	        
        lineChart1.getData().add(series1);
        
        /****SAMPLE PLOT*********************************/
		//defining the axes
	    xAxis2 = new NumberAxis();
	    yAxis2 = new NumberAxis();
	    yAxis2.setLabel("Velocity");
	    xAxis2.setLabel("Time");
	    //creating the chart
	    lineChart2 = new LineChart<Number,Number>(xAxis2,yAxis2);
	    lineChart2.setTitle("Velocity vs Time");
	    //defining a series
	    XYChart.Series series2 = new XYChart.Series();
        //populating the series with data
        series2.getData().add(new XYChart.Data(1, 23));
        series2.getData().add(new XYChart.Data(2, 14));
        series2.getData().add(new XYChart.Data(3, 15));
        series2.getData().add(new XYChart.Data(4, 24));
        series2.getData().add(new XYChart.Data(5, 34));
        series2.getData().add(new XYChart.Data(6, 36));
        series2.getData().add(new XYChart.Data(7, 22));
        series2.getData().add(new XYChart.Data(8, 45));
        series2.getData().add(new XYChart.Data(9, 43));
        series2.getData().add(new XYChart.Data(10, 17));
        series2.getData().add(new XYChart.Data(11, 29));
        series2.getData().add(new XYChart.Data(12, 25));
	        
        lineChart2.getData().add(series2);
        
        /****SAMPLE PLOT*********************************/
		//defining the axes
	    xAxis3 = new NumberAxis();
	    yAxis3 = new NumberAxis();
	    yAxis3.setLabel("Velocity");
	    xAxis3.setLabel("Time");
	    //creating the chart
	    lineChart3 = new LineChart<Number,Number>(xAxis3,yAxis3);
	    lineChart3.setTitle("Velocity vs Time");
	    //defining a series
	    XYChart.Series series3 = new XYChart.Series();
        //populating the series with data
        series3.getData().add(new XYChart.Data(1, 23));
        series3.getData().add(new XYChart.Data(2, 14));
        series3.getData().add(new XYChart.Data(3, 15));
        series3.getData().add(new XYChart.Data(4, 24));
        series3.getData().add(new XYChart.Data(5, 34));
        series3.getData().add(new XYChart.Data(6, 36));
        series3.getData().add(new XYChart.Data(7, 22));
        series3.getData().add(new XYChart.Data(8, 45));
        series3.getData().add(new XYChart.Data(9, 43));
        series3.getData().add(new XYChart.Data(10, 17));
        series3.getData().add(new XYChart.Data(11, 29));
        series3.getData().add(new XYChart.Data(12, 25));
	        
        lineChart3.getData().add(series3);
        
        /****SAMPLE PLOT*********************************/
		//defining the axes
	    xAxis4 = new NumberAxis();
	    yAxis4 = new NumberAxis();
	    yAxis4.setLabel("Velocity");
	    xAxis4.setLabel("Time");
	    //creating the chart
	    lineChart4 = new LineChart<Number,Number>(xAxis4,yAxis4);
	    lineChart4.setTitle("Velocity vs Time");
	    //defining a series
	    XYChart.Series series4 = new XYChart.Series();
        //populating the series with data
        series4.getData().add(new XYChart.Data(1, 23));
        series4.getData().add(new XYChart.Data(2, 14));
        series4.getData().add(new XYChart.Data(3, 15));
        series4.getData().add(new XYChart.Data(4, 24));
        series4.getData().add(new XYChart.Data(5, 34));
        series4.getData().add(new XYChart.Data(6, 36));
        series4.getData().add(new XYChart.Data(7, 22));
        series4.getData().add(new XYChart.Data(8, 45));
        series4.getData().add(new XYChart.Data(9, 43));
        series4.getData().add(new XYChart.Data(10, 17));
        series4.getData().add(new XYChart.Data(11, 29));
        series4.getData().add(new XYChart.Data(12, 25));
	        
        lineChart4.getData().add(series4);
     
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
		Label lbGPSNum = new Label("Port Number:");
		txNameOfStation = new TextField();
		txPortNum = new TextField();
		Button btAddStation = new Button("Add");
		btAddStation.setOnAction(arg0 -> addStations());
		
		
		GridPane addStationPane = new GridPane();
		addStationPane.setHgap(10);
		addStationPane.setVgap(10);
		addStationPane.add(lbNameOfStation, 1, 1);
		addStationPane.add(txNameOfStation, 2, 1);
		addStationPane.add(lbGPSNum, 1, 2);
		addStationPane.add(txPortNum, 2, 2);
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
		
		//check if text typed in textbox txProtNum is a 4-digit integer number
		if(isInteger(txPortNum.getText(),4)){
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
			Label lbPortNum = new Label("Port number:");
			cbSelectStation = new CheckBox();
			
			if (cbSelectStation.isSelected()){
				miRemoveStation.setOnAction(arg0 -> removeSelectedStation());
			}
			
			stationHBox.getChildren().addAll(cbSelectStation, btStation);
			stationDetailsHBox.getChildren().addAll(lbPortNum, lbStationDetails);
			stationDetailsVBox.getChildren().addAll(stationHBox, stationDetailsHBox);
			btStation.setText(txNameOfStation.getText());
			lbStationDetails.setText(txPortNum.getText());
			stationVBox.getChildren().addAll(stationDetailsVBox);
			

			//testing remove function
			miRemoveStation.setOnAction(arg0 -> removeSelectedStation());
			
			
			//assign port number to the station
			portNumber = Integer.parseInt(txPortNum.getText());
		}
	}
	
	public void removeSelectedStation(){
			
		if(cbSelectStation.isSelected()){
		
		stationDetailsVBox.getChildren().clear();
		}
	//	VBox.clearConstraints(stationDetailsVBox);

			System.out.print("jhgf");

	}
	
	public void btStationActions(){
		
		Label lbXAxis = new Label("X Axis");
		Label lbYAxis = new Label("Y Axis");
		Label lbPlot1 = new Label("Plot 1:");
		Label lbPlot2 = new Label("Plot 2:");
		Label lbPlot3 = new Label("Plot 3:");
		Label lbPlot4 = new Label("Plot 4:");
		ChoiceBox cbPlot1XAxis = new ChoiceBox();
		ChoiceBox cbPlot2XAxis = new ChoiceBox();
		ChoiceBox cbPlot3XAxis = new ChoiceBox();
		ChoiceBox cbPlot4XAxis = new ChoiceBox();
		ChoiceBox cbPlot1YAxis = new ChoiceBox();
		ChoiceBox cbPlot2YAxis = new ChoiceBox();
		ChoiceBox cbPlot3YAxis = new ChoiceBox();
		ChoiceBox cbPlot4YAxis = new ChoiceBox();
		
		cbPlot1XAxis.setItems(FXCollections.observableArrayList("Velocity", "Pressure"));
		cbPlot2XAxis.setItems(FXCollections.observableArrayList("Velocity", "Pressure"));
		cbPlot3XAxis.setItems(FXCollections.observableArrayList("Velocity", "Pressure"));
		cbPlot4XAxis.setItems(FXCollections.observableArrayList("Velocity", "Pressure"));
		cbPlot1YAxis.setItems(FXCollections.observableArrayList("Altitude"));
		cbPlot2YAxis.setItems(FXCollections.observableArrayList("Altitude"));
		cbPlot3YAxis.setItems(FXCollections.observableArrayList("Altitude"));
		cbPlot4YAxis.setItems(FXCollections.observableArrayList("Altitude"));

		
		Button btAddPlots = new Button("Add Plots");
		btAddPlots.setOnAction(arg0 -> addPlotsToGUI());
		
		
		GridPane addPlotGridPane = new GridPane();
		addPlotGridPane.setHgap(10);
		addPlotGridPane.setVgap(10);
		addPlotGridPane.add(lbXAxis, 2, 1);
		addPlotGridPane.add(lbYAxis, 3, 1);		
		addPlotGridPane.add(lbPlot1, 1, 2);
		addPlotGridPane.add(lbPlot2, 1, 3);
		addPlotGridPane.add(lbPlot3, 1, 4);
		addPlotGridPane.add(lbPlot4, 1, 5);
		addPlotGridPane.add(cbPlot1XAxis, 2, 2);
		addPlotGridPane.add(cbPlot2XAxis, 2, 3);
		addPlotGridPane.add(cbPlot3XAxis, 2, 4);
		addPlotGridPane.add(cbPlot4XAxis, 2, 5);
		addPlotGridPane.add(cbPlot1YAxis, 3, 2);
		addPlotGridPane.add(cbPlot2YAxis, 3, 3);
		addPlotGridPane.add(cbPlot3YAxis, 3, 4);
		addPlotGridPane.add(cbPlot4YAxis, 3, 5);
		addPlotGridPane.add(btAddPlots, 1, 6);
		

		
		// Create and display said the aforementioned pane in a new stage
		Scene scene = new Scene(addPlotGridPane, 550, 300);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("Choose Plot Settings");
		stage.setResizable(false);
		stage.show();
	}
	
	public void addPlotsToGUI(){
		Plot = new Tab(btStation.getText());		
		tabPanePlots.getTabs().addAll(Plot);
		Plot.setClosable(true);
		borderPane.setCenter(tabPanePlots);
		//Add a swing node
        final SwingNode swingNode = new SwingNode();        
        createSwingContent(swingNode);
		 //Plots in tabPane Plot1
        plotGridPane = new GridPane();        		
		plotGridPane.setHgap(10);
        plotGridPane.setVgap(10);
        Plot.setContent(plotGridPane);
        FlowPane  plotter = new FlowPane();
        plotter.getChildren().add(swingNode);
        plotGridPane.add(plotter,1,1);  
			
//		plotGridPane.add(lineChart1, 1, 1);
//		plotGridPane.add(lineChart2, 1, 2);
//		plotGridPane.add(lineChart3, 2, 1);
//      plotGridPane.add(lineChart4, 2, 2); 	
  
//lineChart.setVisible(true);
	}
	// Creating Swing node for plot
	private void createSwingContent(SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	XYSeries series = new XYSeries("Light Sensor Readings");
            	XYSeriesCollection dataset = new XYSeriesCollection(series);
        		JFreeChart chart = ChartFactory.createScatterPlot("Light Sensor Readings", "Time (seconds)", "ADC Reading", dataset);
        		ChartPanel plot= new ChartPanel(chart);        		
        		swingNode.setContent(plot);
        		// create a new thread that listens for incoming text and populates the graph
        		Thread thread = new Thread(){     			
        			
        			@Override public void run() {
        				while(Ycount<counter){//loop
        				
        		
        				File temp = new File(file1);
        				File temp2= new File(file2);

        				try (BufferedReader br = new BufferedReader(new FileReader(temp))) {	
        				     
        					try(BufferedReader br2 = new BufferedReader(new FileReader(temp2))){
        					    String line ;
        					    String line2;
        					    int count=0;
        					 while ((line = br.readLine()) != null && (line2 = br2.readLine()) != null) {		
        			        	count++;//count number of line 
        			        	System.out.printf("Fist count: %d \n",count);
        			        	System.out.printf("Secount Xcount: %d \n",Xcount);
        			        	System.out.printf("Secount Ycount: %d \n",Ycount);
        			        	// if the number of new line is added then do it 
        			            if( Xcount == count ) {	     			    
        							        
        						            if( Ycount == count ) {
        						            double number = Double.parseDouble(line);
        									System.out.println(number);				
        									double number2 = Double.parseDouble(line2);
        						            System.out.println(number2);	
        									series.add(number, number2);
        									plot.repaint();
        									Ycount++;
        									Xcount++;
        									counter++;
        					                }
        			            }    
        			        }
        					}catch(IOException e){
        						e.printStackTrace();
        					}
        							}catch(IOException e){
        								e.printStackTrace();
        							}					
        			            }
        			        }					

        			
        		
        			//end while
        		};
        		thread.start();
            }
        });
		
	}

	/**
	}
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
	 * Method starts executing thread in receiver class to start communication
	 * Allows connection to the Ground Station
	 * @throws IOException
	 * 
	 */
	public void Connect() throws IOException{
		MS_Manager.setSettings(portNumber);
		//Start receiving files
		System.out.println("Starting Server");
		//execute run() method in Receiver thread
		MS_Manager.fileReceiver.start();
		
	}
	
	
	/**
	 * Method to check if the text typed in a box is a 4-digit integer
	 * Checks if the string is empty
	 * Checks if string is not equal to 4
	 * Checks each character in a string for being an integer
	 * 
	 * @param str - a string to check
	 * @param radix - length of a string
	 * @return verify that string is a 4-digit integer number
	 */
	public static boolean isInteger(String str, int radix) {
		boolean verify = true;
		
		//check is string is empty
	    if(str.isEmpty()){ 
	    	verify = false;
	    }
	    
	    //check is the string is less than 4 characters long
	    if(str.length() != 4) {
        	verify = false;
        }
	    
	    //check each charter for being a number
	    for(int i = 0; i < str.length(); i++) {
	        if(i == 0 && str.charAt(i) == '-') {
	            if(str.length() == 1 ) {
	            	verify = false;
	            }
	        }
	        if(Character.digit(str.charAt(i),radix) < 0) {
	        	verify = false;
	        }
	    }
		return verify;
	}
}