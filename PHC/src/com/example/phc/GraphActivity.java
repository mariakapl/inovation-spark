package com.example.phc;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

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
	    GraphViewSeries exampleSeries = new GraphViewSeries(
	    		new GraphViewData[] {
	        new GraphViewData(1, 2.0d)
	        , new GraphViewData(2, 1.5d)
	        , new GraphViewData(3, 2.5d)
	        , new GraphViewData(4, 1.0d)
	    });
	     
	    GraphView graphView = new LineGraphView(
	        this // context
	        , "GraphViewDemo" // heading
	    );
	    graphView.addSeries(exampleSeries); // data
	     
	    LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
	    layout.addView(graphView);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.graph, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
