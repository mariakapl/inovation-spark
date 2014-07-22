package com.example.phc;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ShareActivity extends ActionBarActivity{
   
	MyAdpater adapter;
	ListView listView1;
	
	@Override
      public void onCreate(Bundle savedInstanceState) {
		
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_share);
	    
	    String[] arr = new String[]{"north","center","south"};
	    
        // Getting adapter by passing xml data ArrayList
        adapter=  new MyAdpater(this,arr);
        
        ListView listView1 = (ListView) findViewById(R.id.regions);
        
        listView1.setAdapter(adapter);
 
		// Click event for single list row
        // Click event for single list row
        listView1.setOnItemClickListener(new OnItemClickListener() {
 
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				int x =5;
				x += 1;
			}
        });
		
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_share);
//        
//        ListView listView1 = (ListView) findViewById(R.id.regions);
//        
//        String[] items = { "North", "Sharon", "Gush-Dan", "Shfela", "South" };
//        
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                    R.layout.list_item , items);
//        
//        listView1.setAdapter(adapter);
    }
}

