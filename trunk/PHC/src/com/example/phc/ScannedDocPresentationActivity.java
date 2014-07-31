package com.example.phc;


import phc.objects.DocResult;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

public class ScannedDocPresentationActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_scanned_doc_presentation);
	    
	    Intent intent = getIntent();
	    DocResult doc = (DocResult) getIntent().getParcelableExtra(RecordListActivity.DOCRESULT_EXTRA);
	    if(doc == null)
	    	return;
	    
	    ImageView image = (ImageView)findViewById(R.id.scanned_doc);
	    Bitmap map = doc.scannedDoc().bitmap();
        if(image != null && map != null)
     	   image.setImageBitmap(map);
	    // TODO Auto-generated method stub
	}
}
