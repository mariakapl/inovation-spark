package com.example.phc;


import phc.objects.DocResult;
import phc.storage.DocStorage;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

public class ScannedDocPresentationActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_scanned_doc_presentation);

	    String id = getIntent().getStringExtra(RecordListActivity.DOCID_EXTRA);
	    DocResult doc = DocStorage.get().getDocById(id);
	    if(doc == null)
	    	return;
	    
	    ImageView image = (ImageView)findViewById(R.id.scanned_doc);
	    Bitmap map = doc.scannedDoc().bitmap();
        if(image != null && map != null)
     	   image.setImageBitmap(map);
	}
}
