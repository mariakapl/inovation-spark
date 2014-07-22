package com.example.phc;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class SecondOpinionFragment extends ListFragment {
	
	MyAdpater adapter;
	ListView listView1;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.second_opinion, container, false);
        
	    String[] arr = new String[]{"Family Medicine","Cardiology","Otolaryngology", "Oncology"};
	    
        adapter=  new MyAdpater(getActivity(),arr);
        
        listView1 = (ListView)rootView.findViewById(android.R.id.list);
        
        listView1.setAdapter(adapter);
 
		// Click event for single list row
        listView1.setOnItemClickListener(new OnItemClickListener() {
 
        	
        	 
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
			}
        });
        
        return rootView;
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
      // do something with the data
		Object item = listView1.getItemAtPosition(position);
		
		Intent intent = new Intent(getActivity(), DoctorNamesActivity.class);
		intent.putExtra("DOCTOR_TYPE", (String)item);
		startActivity(intent);
    }
    
}
