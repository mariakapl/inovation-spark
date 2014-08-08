package com.example.phc;

import java.util.List;

import phc.objects.BloodTest;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class InformationAdapter extends BaseAdapter {
    private BloodTestActivity activity;
    private static LayoutInflater inflater=null;
    private List<BloodTest> _tests;
    
    
    public InformationAdapter(BloodTestActivity a, List<BloodTest> tests) {
        activity = a;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _tests = tests;
    }
 
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.information_row, null);
 
        TextView title = (TextView)vi.findViewById(R.id.title); // title

 		ImageButton button = 
 				(ImageButton)vi.findViewById( R.id.infoButton);
 		
 		InfoClickHandler info_h = new InfoClickHandler(position);
 		button.setOnClickListener(info_h);
 		
 	   // button.setOnClickListener(imgButtonInfoHandler);
 	    
 		ImageButton graphButton = 
 				(ImageButton)vi.findViewById( R.id.graphButton);
 		GraphClickHandler gh = new GraphClickHandler(position);
 		graphButton.setOnClickListener(gh);
 		

        // Setting all values in listview
 		BloodTest bt = _tests.get(position);
        title.setText(bt.toString());
        if (!bt.isNormalValue())
        	title.setTextColor(Color.RED);

        return vi;
    }
    
    private class GraphClickHandler implements View.OnClickListener {
    	private int _position;
    	public GraphClickHandler(int position) {
    		_position = position;
    	}
        public void onClick(View v) {

        	activity.graphClicked(_position);
      }
    	
    }
     
    private class InfoClickHandler implements View.OnClickListener {
    	private int _position;
    	public InfoClickHandler(int position) {
    		_position = position;
    	}
        public void onClick(View v) {
        	
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            
            String title = activity.getTestTitle(_position);
            
            builder.setTitle(title!=null ? title : "Infrmation");
            
            String desc = activity.getTestDescription(_position);
            
            if(desc == null)
            	return;
            
            builder.setMessage(desc);
            
            AlertDialog alert = builder.create();
            alert.show();
      }
    	
    }
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _tests.size();
	}


	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return _tests.get(position);
	}
}
