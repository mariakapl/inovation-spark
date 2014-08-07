package com.example.phc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

public class GraphActivity extends Activity {

	public class GraphViewData implements GraphViewDataInterface {
	    private double x,y;

	    public GraphViewData(double x, double y) {
	        this.x = x;
	        this.y = y;
	    }

	    @Override
	    public double getX() {
	        return this.x;
	    }

	    @Override
	    public double getY() {
	        return this.y;
	    }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph);
		 // init example series data

	     
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
	    
	    final HashMap<Integer,String> dateMap = new  HashMap<Integer,String>();
	    Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DATE, -1);
	    dateMap.put(1,dateFormat.format(cal.getTime()));
	    cal.add(Calendar.DATE, -1);
	    dateMap.put(2,dateFormat.format(cal.getTime()));
	    cal.add(Calendar.DATE, -1);
	    dateMap.put(3,dateFormat.format(cal.getTime()));
	    cal.add(Calendar.DATE, -1);
	    dateMap.put(4,dateFormat.format(cal.getTime()));
	    
	    GraphView graphView = new LineGraphView(
	        this // context
	        , "Hemoglobin Level" // heading
	    );
	    

	    GraphViewSeries exampleSeries = new GraphViewSeries(
	    		new GraphViewData[] {
	        new GraphViewData(1, 2.0)
	        , new GraphViewData(1.2, 2.2)
	        , new GraphViewData(1.3, 2.4)
	        , new GraphViewData(2, 1.5)
	        , new GraphViewData(3, 2.5)
	        , new GraphViewData(4, 1.0)
	    });
	    
	  
	    graphView.getGraphViewStyle().setNumHorizontalLabels(4);
	    graphView.getGraphViewStyle().setNumVerticalLabels(4);
	    
	    
	    
	   // graphView.setScrollable(true);
	    // optional - activate scaling / zooming
	  //  graphView.setScalable(true);
	    
	    
	    graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
	    	  @Override
	    	  public String formatLabel(double value, boolean isValueX) {
	    	    if (isValueX) {
	    	    	
	    	    	if(value == 1.0 || value == 2.0 || value == 3.0 || value == 4.0)
	    	    			{
				    	    	String date = dateMap.get((int)value);
				    	    	
				    	    	if(value == 2.0)
				    	    	{
				    	    		date += "\nAspirin Taken";
				    	    	}
				    	    	
				    	    	return date;
				    	    	//String newDate = date.replace(" ", "\n");
				    	    //	return newDate;
	    	    			}
	    	    	else
	    	    		return "";
	    	    }
	    	    return null; // let graphview generate Y-axis label for us
	    	  }
	    	});
	    
	    graphView.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
	   // graphView.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
	    
	    graphView.setShowHorizontalLabels(true);
	    graphView.getGraphViewStyle().setVerticalLabelsWidth(50);
	    ((LineGraphView) graphView).setDrawDataPoints(true);
	    
	    graphView.addSeries(exampleSeries); // data
	    //graphView.setViewPort(1, 2);
	   
	    LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
	    layout.addView(graphView);

	}

}
