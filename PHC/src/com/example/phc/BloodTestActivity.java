package com.example.phc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import phc.objects.BloodTest;
import phc.objects.DocResult;
import phc.objects.Medicine;
import phc.storage.DocStorage;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class BloodTestActivity extends Activity {

	public static final String DOCID_EXTRA = "doc_id";
	private static final String BLOOD_TEST_EXTRA = "blood_test_extra";
	InformationAdapter adapter;
	ListView listView1;
	List<BloodTest> bloodResultList = null;
	
	public static HashMap<String,String> descriptions = new HashMap<String, String>();
	
	static //static constructor 
	{
		descriptions.put("Hemoglobin",  "Iron-containing oxygen-transport metalloprotein in the red blood cells");
		descriptions.put("Glucose",     "The amount of glucose present in the blood");
		descriptions.put("Platelets",   "Blood cells whose function (along with the coagulation factors) is to stop bleeding");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blood_test);
		
		DocResult doc = null;
		
	    String id = getIntent().getStringExtra(BloodTestActivity.DOCID_EXTRA);
	    
	    if(id.equals("-1")) //test
	    {
	    	bloodResultList = new ArrayList<BloodTest>();
	    	//public BloodTest(String name, String docId, String value, String units, String date)
	    	bloodResultList.add(new BloodTest("lala","-1.0", "1.0", "mg/l", "9/3/2014"));
	    	bloodResultList.add(new BloodTest("lala2","-1.0", "1.0", "mg/l", "10/3/2014"));
	    }
	    else
	    {
	    	doc = DocStorage.get().getDocById(id);
		    if(doc == null)
		    	return;
		    
		    bloodResultList =  doc.bloodTests();
	    }
	    
	    String[] array = new String[bloodResultList.size()];
	    
	    int i=0;
	    for(BloodTest test : bloodResultList)
	    {
	    	array[i] = test.Name + " " + test.Value + " " + test.Units;
	    	i++;
	    }
	    
	    adapter=  new InformationAdapter(this,array);
	    
	    ListView listView1 = (ListView) findViewById(android.R.id.list);
        
        listView1.setAdapter(adapter);
 
		// Click event for single list row
        // Click event for single list row
        listView1.setOnItemClickListener(new OnItemClickListener() {
 
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				BloodTest test = bloodResultList.get(position);
				List<BloodTest> tests = DocStorage.get().getBloodTestHistory(
					BloodTestActivity.this, test.Name);
				for (BloodTest t : tests)
					System.err.println(t.Name + "," + t.Value + "," + t.Units);
				List<Medicine> meds = DocStorage.get().getBloodTestAssociatedHistory(
					BloodTestActivity.this, test.Name);
				for (Medicine m : meds)
					System.err.println(m.Name + "," + m.StartDate + "," + m.NumDays);
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

	public void graphClicked(int position) {
		Intent intent = new Intent(this, GraphActivity.class);
		BloodTest test = bloodResultList.get(position);
		intent.putExtra(BLOOD_TEST_EXTRA, test.Name);
		startActivity(intent);
	}




	public String getTestDescription(int position) {
		// TODO Auto-generated method stub
		BloodTest test = bloodResultList.get(position);
		if(descriptions.containsKey(test.Name))
		{
			return descriptions.get(test.Name);
		}
		
		return null;
		
	}

	public String getTestTitle(int position) {
		// TODO Auto-generated method stub
		BloodTest test = bloodResultList.get(position);
		if(descriptions.containsKey(test.Name))
		{
			return test.Name;
		}
		
		return null;
	}



}
