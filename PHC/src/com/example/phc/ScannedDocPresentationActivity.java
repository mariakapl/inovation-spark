package com.example.phc;


import java.util.List;

import phc.objects.BloodTest;
import phc.objects.DocResult;
import phc.storage.DocStorage;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ScannedDocPresentationActivity extends Activity {

	private DocResult _doc;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_scanned_doc_presentation);

	    String id = getIntent().getStringExtra(RecordListActivity.DOCID_EXTRA);
	    _doc = DocStorage.get().getDocById(id);
	    if(_doc == null)
	    	return;
	    
	    ImageView image = (ImageView)findViewById(R.id.scanned_doc);
	    Bitmap map = _doc.scannedDoc().bitmap();
        if(image != null && map != null)
     	   image.setImageBitmap(map);
	}
	
	public void onClick(View view)
	{
		List<BloodTest> tests = _doc.bloodTests();
		if (tests != null && tests.size() > 0) {
			Intent intent = new Intent(this, BloodTestActivity.class);
			intent.putExtra(RecordListActivity.DOCID_EXTRA, _doc.id());
			startActivity(intent);
		}
	}
}
