package com.example.phc;

import java.util.List;

import phc.objects.BloodTest;
import phc.objects.DocResult;
import phc.storage.DocStorage;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class BloodTestActivity extends Activity {

	MyAdpater adapter;
	ListView listView1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blood_test);
		
	    String id = getIntent().getStringExtra(RecordListActivity.DOCID_EXTRA);
	    DocResult doc = DocStorage.get().getDocById(id);
	    if(doc == null)
	    	return;
	    
	    List<BloodTest> bloodResultList = doc.bloodTests();
	    
	    String[] array = new String[bloodResultList.size()];
	    
	    int i=0;
	    for(BloodTest test : bloodResultList)
	    {
	    	array[i] = test.Name + " " + test.Value + " " + test.Units;
	    }
	    
	    adapter=  new MyAdpater(this,array);
	    
	    ListView listView1 = (ListView) findViewById(android.R.id.list);
        
        listView1.setAdapter(adapter);
 
		// Click event for single list row
        // Click event for single list row
        listView1.setOnItemClickListener(new OnItemClickListener() {
 
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
			}
        });
	    
	}

	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.blood_test, menu);
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
