package com.example.phc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

public class DoctorNamesActivity extends Activity {

	MyAdpater adapter;
	ListView listView1;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_doctor_names);
	    
	    String[] doctorNames = null;
	    
	    Intent intent = getIntent();
	    String doctorType = intent.getStringExtra("DOCTOR_TYPE");
	    
	    if(doctorType.equals("Family Medicine"))
	    {
	    	doctorNames = new String[] {"Dr. Rami Cohen", "Dr. Alon Givati"};
	    }
	    if(doctorType.equals("Cardiology"))
	    {
	    	doctorNames = new String[] {"Dr. Jacob Rubinstein"};
	    }
	    if(doctorType.equals("Otolaryngology"))
	    {
	    	doctorNames = new String[] {"Dr. Danit Rozen", "Dr. Itay Levi", "Prof. Eli Gutman"};
	    }
	    if(doctorType.equals("Oncology"))
	    {
	    	doctorNames = new String[] {"Dr. Ronit Sade", "Prof. Daniel Kaplan"};
	    }
	  
	    if(doctorNames == null)
	    	return;
        // Getting adapter by passing xml data ArrayList
        adapter=  new MyAdpater(this,doctorNames);
        
        ListView listView1 = (ListView) findViewById(android.R.id.list);
        
        listView1.setAdapter(adapter);
	    
	    // TODO Auto-generated method stub
	}

}
