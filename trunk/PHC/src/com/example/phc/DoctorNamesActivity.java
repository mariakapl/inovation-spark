package com.example.phc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DoctorNamesActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_doctor_names);
	    
	    Intent intent = getIntent();
	    
	    String doctorType = intent.getStringExtra("DOCTOR_TYPE");
	    
	    
	    TextView v = (TextView)findViewById(R.id.names);
	    v.setText(doctorType); 
	    // TODO Auto-generated method stub
	}

}
