package com.example.phc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import phc.interfaces.IDocStorage;
import phc.objects.ScannedDoc;
import phc.storage.DocStorage;

import edu.sfsu.cs.orange.ocr.CaptureActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

public class TagActivity extends FragmentActivity {

	ExpandableListView expListView = null;
	ArrayList<String> existingTags = null;
	HashMap<String, List<String>> listDataChild = null;
	List<String> listDataHeader = null;
	IDocStorage _docStorage;
	Bitmap _bitmap;
	String _ocr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag);
		Intent intent = getIntent();
		ImageView imageView = (ImageView) findViewById(R.id.imageView1);
		Parcelable p = intent.getParcelableExtra(CaptureActivity.BITMAP_EXTRA);
		_bitmap = (Bitmap) p;
		imageView.setImageBitmap(_bitmap);
		
	    listDataHeader = new ArrayList<String>();
        listDataHeader.add("Suggested tags");
        listDataHeader.add("Existing tags");
        listDataChild = new HashMap<String, List<String>>();
	   	_ocr = intent.getStringExtra(CaptureActivity.OCR_RESULT_TEXT_EXTRA);
	   	OcrProcessor op = new OcrProcessor(_ocr);
	    ArrayList<String> suggestedTags = new ArrayList<String>(op.suggestedTags());
	    listDataChild.put(listDataHeader.get(0), suggestedTags);
	    _docStorage = DocStorage.get();
	    existingTags = new ArrayList<String>();
	    if (_docStorage != null) {
	    	existingTags.addAll(_docStorage.getChildTags(null));
	    	existingTags.removeAll(suggestedTags);
	    }
	    listDataChild.put(listDataHeader.get(1), existingTags);
	    ExpandableListAdapter listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
	    expListView = (ExpandableListView) findViewById(R.id.expandableListView1);
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
	
	public void onCreateNewTagClick(View view)
	{
        DialogFragment dialog = new NoticeDialogFragment();
        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
	
	}

	public void doPositiveClick(String text) {
		// TODO Auto-generated method stub

		ExpandableListAdapter adapter = 
		        (ExpandableListAdapter) expListView.getExpandableListAdapter();

		//( (ExpandableListParentClass)adapter.getMParent().get(0)).getParentChildren().add(text);
		//(change to get(0) which you parent want to get )
		
		existingTags = new ArrayList<String>();
	    existingTags.add("tag1");
	    existingTags.add("tag2");
	    existingTags.add(text);
	    
	    listDataChild.put(listDataHeader.get(1), existingTags);
		adapter.notifyDataSetChanged();
		//adapter.notifyDataSetInvalidated();
	}

	public void doNegativeClick() {
		// TODO Auto-generated method stub
	}

	public void onSaveClick(View view) {
		if (_docStorage == null) {
			Toast.makeText(this, "Cannot save - internal error", Toast.LENGTH_SHORT);
			return;
		}
		
		EditText editText = (EditText) findViewById(R.id.doc_name);
		String name = editText.getText().toString();
		if (name == null || name.length() == 0) {
			Toast.makeText(this, "Please specify a name for the document", Toast.LENGTH_SHORT);
			return;
		}
		Bitmap bitmap = _bitmap;
		ExpandableListAdapter adapter = 
			(ExpandableListAdapter) expListView.getExpandableListAdapter();
		List<String> tags = new ArrayList<String>();
		for (int i = 0; i < adapter.getGroupCount(); i++)
		{
			for (int j = 0; j < adapter.getChildrenCount(i); j++)
			{
				View v = adapter.getChildView(i, j, false, null, null);
		        CheckBox cb = (CheckBox) v.findViewById(R.id.checkboxListItem);
		        if (cb.isChecked())
		        	tags.add(cb.getText().toString());
			}
		}
		String ocr = _ocr;
		ScannedDoc doc = new ScannedDoc(name, bitmap, tags, ocr);
		_docStorage.addDoc(doc);
		Toast.makeText(this, "Document saved", Toast.LENGTH_SHORT);
	}
}
