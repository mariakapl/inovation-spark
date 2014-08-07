package com.example.phc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
	    final long totalDays = getDayDiff(minDate, maxDate);
	    
		List<GraphViewData> bloodTestData = new ArrayList<GraphViewData>();
		double min = 100000;
		for (BloodTest t : tests)
		{
			double value = Double.parseDouble(t.Value);
			Date d = Utils.getDateFromString(t.Date);
			double xvalue = ((double)(getDayDiff(minDate, d)) / totalDays);
			bloodTestData.add(new GraphViewData(xvalue, value));
			if (value < min)
				min = value;
		}
		GraphViewData [] bloodTestDataArray = new GraphViewData[bloodTestData.size()];
		bloodTestData.toArray(bloodTestDataArray);
	    GraphViewSeries bloodTestSeries = new GraphViewSeries(
	    	testName, new GraphViewSeriesStyle(Color.BLUE, 1), bloodTestDataArray);
	    
		if (meds != null) {
		    List<GraphViewData> medicineData = new ArrayList<GraphViewData>();
			String medicineName = "Medicine";
		    for (Medicine m : meds)
		    {
				Date d = Utils.getDateFromString(m.StartDate);
				double xvalue = ((double)(getDayDiff(minDate, d)) / totalDays);
				double diff = (double)m.NumDays / totalDays;
		    	medicineData.add(new GraphViewData(xvalue, min));
		    	medicineData.add(new GraphViewData(xvalue + diff, min));
		    	medicineName = m.Name;
		    }
			GraphViewData [] medicineDataArray = new GraphViewData[medicineData.size()];
			medicineData.toArray(medicineDataArray);
		    GraphViewSeries medicineSeries = new GraphViewSeries(
		    	medicineName, new GraphViewSeriesStyle(Color.RED, 5), medicineDataArray);
		    graphView.addSeries(medicineSeries); // data
		}
	    graphView.setShowLegend(true);
	    graphView.getGraphViewStyle().setLegendWidth(200);
	    graphView.getGraphViewStyle().setNumHorizontalLabels(4);
	    graphView.getGraphViewStyle().setNumVerticalLabels(4);
	    
	    graphView.setScrollable(true);
	    // optional - activate scaling / zooming
	    graphView.setScalable(true);
	    graphView.getGraphViewStyle().setTextSize(20);
	    
    	final Date minDate1 = minDate;
	    graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
	    	  @Override
	    	  public String formatLabel(double value, boolean isValueX) {
	    		  if (!isValueX)
	    			  return null;
	    		  int days = (int)(value * totalDays);
	    		  Calendar c = Calendar.getInstance();
	    		  c.setTime(minDate1);
	    		  c.add(Calendar.DATE, days);
	    		  return Utils.getDateStringShort(c.getTime());
	    	  }
	    	});
	    
	    graphView.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
	    
	    graphView.setShowHorizontalLabels(true);
	    graphView.getGraphViewStyle().setVerticalLabelsWidth(50);
	    ((LineGraphView) graphView).setDrawDataPoints(true);
	    
	    graphView.addSeries(bloodTestSeries); // data
	   
	    LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
	    layout.addView(graphView);

	}

}
