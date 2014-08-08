package com.example.phc;

import phc.storage.DocStorage;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import edu.sfsu.cs.orange.ocr.CaptureActivity;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	DocStorage.create(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//        	DocStorage.get().clear();
//            return true;
//        }
        
        switch (item.getItemId()) {
        case R.id.action_settings:
        	DocStorage.get().clear();
            return true;
        case R.id.action_resetDatabase:
        	DocStorage.get().resetDatabase(this);
        	return true;
        case R.id.action_info:
            OpenInfo();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    
    private void OpenInfo() {
		// TODO Auto-generated method stub
        AlertDialog alertDialog = new AlertDialog.Builder(this).create(); //Read Update
        alertDialog.setTitle("Clinical Summary");
        alertDialog.setMessage("Name: Haim Levi \nAge: 54\nMedication:\n	Aspirin\n	Repaglinide\n	Januet\n");
        alertDialog.show();  //<-- See This!
	}


	public void scanDocument(View view)
    {
    	Intent intent = new Intent(this, CaptureActivity.class); //in order test without photo
    	//Intent intent = new Intent(this, TagActivity.class);
    	startActivity(intent);
    }
    
    public void onClickShare(View view)
    {
        // Do something in response to button
    	//Intent intent = new Intent(this, GraphActivity.class);
//    	Intent intent = new Intent(this, BloodTestActivity.class);
//    	intent.putExtra("doc_id", "-1");
    	//EditText editText = (EditText) findViewById(R.id.scandoc);
    	//String message = editText.getText().toString();
    //	
    	//startActivity(intent);
    }
    
    public void onClickHealthportal(View view)
    {
        // Do something in response to button
    	Intent intent = new Intent(this, HealthPortalActivity.class);
    	startActivity(intent);
    }
    
    public void onClickRecords(View view)
    {
    	Intent intent = new Intent(this, RecordListActivity.class);
    	startActivity(intent);
    }
}
