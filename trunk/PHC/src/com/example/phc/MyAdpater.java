package com.example.phc;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdpater extends BaseAdapter {
    private Activity activity;
    private static LayoutInflater inflater=null;
    private String[] arr;
    
    
    public MyAdpater(Activity a, String[] array) {
        activity = a;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        arr = array;
    }
 
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);
 
        TextView title = (TextView)vi.findViewById(R.id.title); // title

        // Setting all values in listview
        title.setText(arr[position]);

        return vi;
    }


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arr.length;
	}


	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arr[position];
	}
}
