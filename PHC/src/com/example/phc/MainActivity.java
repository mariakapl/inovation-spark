package com.example.phc;

import phc.storage.DocStorage;
import edu.sfsu.cs.orange.ocr.CaptureActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;

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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
    	Intent intent = new Intent(this, GraphActivity.class);
    	//EditText editText = (EditText) findViewById(R.id.scandoc);
    	//String message = editText.getText().toString();
    //	intent.putExtra(EXTRA_MESSAGE, message);
    	startActivity(intent);
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
