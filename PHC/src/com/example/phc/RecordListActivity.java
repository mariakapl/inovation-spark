package com.example.phc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import phc.interfaces.IDocStorage;
import phc.objects.DocResult;
import phc.storage.DocStorage;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class RecordListActivity extends ListActivity  {

	/** Called when the activity is first created. */
	RecordAdapter adapter = null;
	IDocStorage _docStorage;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    _docStorage = DocStorage.get();
	    
	    fillContent();
//        setContentView(R.layout.activity_records_list);
//	    // TODO Auto-generated method stub
//        List<DocResult> docs = new ArrayList<DocResult>();
//        
//        int tag_number = 3;
//        RecordAdapter adapter=  new RecordAdapter(this,R.layout.record_row,docs, tag_number);
//        
//        ListView listView1 = (ListView) findViewById(android.R.id.list);
//        
//        listView1.setAdapter(adapter);
        
	}

	private void fillContent() {
		
	  List<DocResult> docs = new ArrayList<DocResult>();
      List<String> tags = new ArrayList<String>(_docStorage.getChildTags(null));
		
      //go over all the tags and docs in current (tag ?)
      Collections.sort(tags);
      
      for(String tag: tags)
      {
    	  docs.addAll(_docStorage.queryDocsByTags(Arrays.asList(tag)));
      }
      
      adapter =  new RecordAdapter(this, R.layout.record_row, docs, tags.size());
      this.setListAdapter(adapter);
		
	}
	
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
            // TODO Auto-generated method stub
            super.onListItemClick(l, v, position, id);
            DocResult o = adapter.getItem(position);
            //if(o.id() == -1){ //this is a tag
                  fillContent();
            //}
            //else
            //{
            //        onDocClick(o);
            //}
    }

	private void onDocClick(DocResult o) {
		// TODO Auto-generated method stub
		//open a new activity to watch the document
	}



}
