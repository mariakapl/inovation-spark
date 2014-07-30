package com.example.phc;

import java.io.File;

import phc.storage.DocStorage;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class ScannedDocPresentationActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    
	    setContentView(R.layout.activity_scanned_doc_presentation);
	    
	    String file_id = null;
	    
	    if (getIntent().hasExtra("file_id")) {
	    	file_id = getIntent().getStringExtra("file_id");
	    }
	    
	    if(file_id == null)
	    	return;
	    
	    ImageView image = (ImageView)findViewById(R.id.scanned_doc);
        
	    //find the bitmap
	    File image_dir = DocStorage.get().getImageDir();
	    
	    Bitmap map = BitmapFactory.decodeFile(image_dir.getAbsolutePath() + "/" + file_id + ".png");
	    
        if(image != null && map != null)
     	   image.setImageBitmap(map);
	    // TODO Auto-generated method stub
	}

}
