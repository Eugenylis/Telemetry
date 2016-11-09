package Masterstation;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;

public class Plot extends ScatterChart<Number,Number>{

	public int data;
	public Object xPoint;
	public Object yPoint;


	public Plot(String dataToPlot){
		
		super(new NumberAxis(), new NumberAxis());
		this.getXAxis().setLabel(dataToPlot);
		this.getXAxis().setAutoRanging(true);
		this.getYAxis().setLabel("Altitude (meters)");
		this.getYAxis().setAutoRanging(true);
		
		setLegendVisible(false);
        setTitle("Altitude vs " + dataToPlot);
        
        //defining a series
        XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
        
        //populating the series with data
        //For each balloon object (packet)
        for (BalloonData packetData : Manager.getPacketArray()) {
        	try {
				//Plots X and Y coor. X is hard since it is an attribute, this
        		//is the code required to retrieve and plot the data.
        		series.getData().add(new XYChart.Data<Number, Number>( 
						((Number)(packetData.getClass().getMethod("get" + dataToPlot).invoke(packetData))).doubleValue()
						,packetData.getAltitude()));
			} catch (Exception e){	
				//TODO Errors?
			}
		}
        getData().add(series);
	}

}//end Plot