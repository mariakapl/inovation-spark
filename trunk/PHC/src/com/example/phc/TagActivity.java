package com.example.phc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import edu.sfsu.cs.orange.ocr.CaptureActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ImageView;

public class TagActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag);
		ImageView imageView = (ImageView) findViewById(R.id.imageView1);
		Intent intent = getIntent();
		Parcelable p = intent.getParcelableExtra(CaptureActivity.BITMAP_EXTRA);
		imageView.setImageBitmap((Bitmap) p);
		
	    List<String> listDataHeader = new ArrayList<String>();
        listDataHeader.add("Suggested tags");
        listDataHeader.add("Existing tags");
	    HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
	    ArrayList<String> suggestedTags = new ArrayList<String>();
	    String s = intent.getStringExtra(CaptureActivity.OCR_RESULT_TEXT_EXTRA);
	    String [] tags = s.split(" ");
	    for (String tag : tags)
	    	suggestedTags.add(tag);
	    listDataChild.put(listDataHeader.get(0), suggestedTags);
	    ArrayList<String> existingTags = new ArrayList<String>();
	    existingTags.add("tag1");
	    existingTags.add("tag2");
	    listDataChild.put(listDataHeader.get(1), existingTags);
	    ExpandableListAdapter listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
		ExpandableListView expListView = (ExpandableListView) findViewById(R.id.expandableListView1);
		expListView.setAdapter(listAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tag, menu);
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
