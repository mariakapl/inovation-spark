package com.example.phc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import phc.objects.BloodTest;
import phc.objects.Medicine;
import phc.storage.DocStorage;
import phc.utils.Utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
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
	
	private long getDayDiff(Date earlier, Date later)
	{
	    long diff = later.getTime() - earlier.getTime();
	    long days = diff / (24 * 60 * 60 * 1000);
	    return days;
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
	    
	    String testName = getIntent().getStringExtra(BloodTestActivity.BLOOD_TEST_EXTRA);
		List<BloodTest> tests = DocStorage.get().getBloodTestHistory(this, testName);
		List<Medicine> meds = DocStorage.get().getBloodTestAssociatedHistory(this, testName);

	    GraphView graphView = new LineGraphView(this, testName + " Level");

	    Date minDate = null, maxDate = null;
	    for (BloodTest test : tests) {
	    	Date d = Utils.getDateFromString(test.Date);
	    	if (minDate == null || d.compareTo(minDate) < 0)
	    		minDate = d;
	    	if (maxDate == null || d.compareTo(maxDate) > 0)
	    		maxDate = d;
	    }
	    long days = getDayDiff(minDate, maxDate);
	    
		List<GraphViewData> bloodTestData = new ArrayList<GraphViewData>();
		double min = 100000;
		for (BloodTest t : tests)
		{
			double value = Double.parseDouble(t.Value);
			Date d = Utils.getDateFromString(t.Date);
			double xvalue = ((double)(getDayDiff(minDate, d)) / days);
			bloodTestData.add(new GraphViewData(xvalue, value));
			if (value < min)
				min = value;
		}
		GraphViewData [] bloodTestDataArray = new GraphViewData[bloodTestData.size()];
		bloodTestData.toArray(bloodTestDataArray);
	    GraphViewSeries bloodTestSeries = new GraphViewSeries(
	    	testName, new GraphViewSeriesStyle(Color.BLUE, 1), bloodTestDataArray);
	    
	    List<GraphViewData> medicineData = new ArrayList<GraphViewData>();
		String medicineName = "Medicine";
	    for (Medicine m : meds)
	    {
			Date d = Utils.getDateFromString(m.StartDate);
			double xvalue = ((double)(getDayDiff(minDate, d)) / days);
			double diff = (double)m.NumDays / days;
	    	medicineData.add(new GraphViewData(xvalue, min));
	    	medicineData.add(new GraphViewData(xvalue + diff, min));
	    	medicineName = m.Name;
	    }
		GraphViewData [] medicineDataArray = new GraphViewData[medicineData.size()];
		medicineData.toArray(medicineDataArray);
	    GraphViewSeries medicineSeries = new GraphViewSeries(
	    	"Medicine", new GraphViewSeriesStyle(Color.RED, 5), medicineDataArray);
	    graphView.setShowLegend(true);
	    graphView.getGraphViewStyle().setNumHorizontalLabels(4);
	    graphView.getGraphViewStyle().setNumVerticalLabels(4);
	    
	    graphView.setScrollable(true);
	    // optional - activate scaling / zooming
	    graphView.setScalable(true);
	    
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
	    
	    graphView.addSeries(bloodTestSeries); // data
	    graphView.addSeries(medicineSeries); // data
	    //graphView.setViewPort(1, 2);
	   
	    LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
	    layout.addView(graphView);

	}

}
