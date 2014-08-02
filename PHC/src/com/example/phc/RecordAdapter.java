package com.example.phc;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import phc.objects.DocResult;

public class RecordAdapter extends  ArrayAdapter<DocResult> {
    private Context c;
    private int id;
    private int tag_number;
    private List<DocResult>items;
    LayoutInflater layoutInflater = null;
    
    public RecordAdapter(Context context, int textViewResourceId,
                    List<DocResult> objects, int tag_count) {
            super(context, textViewResourceId, objects);
            c = context;
            id = textViewResourceId;
            items = objects;
            tag_number = tag_count;
           // layoutInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    
    public DocResult getItem(int i)
    {
        return items.get(i);
     
    }
    
    @Override
    public int getItemViewType(int position) {
        return position < tag_number ? 0 : 1;
    }

    @Override
    public int getViewTypeCount() { //two types of view - 1. tag 2. document
        return 2;
    }
    
    View.OnClickListener imgButtonHandler = new View.OnClickListener() {

        public void onClick(View v) {

        
            AlertDialog.Builder builder = new AlertDialog.Builder(c);
            builder.setTitle("Share With");
            builder.setItems(new CharSequence[]{"Dr. Alon", "Dr. Sharon", "Dr. Levi"}, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    // Do something with the selection
                    //mDoneButton.setText(items[item]);
               	 Toast.makeText( c,
                  "Document Shared",
                  Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();

        }
    };
    
     @Override
    public View getView(int position, View convertView, ViewGroup parent) {
           View v = convertView;
           
           int type = getItemViewType(position);
           
           if (v == null) {
           /* create a new view of my layout and inflate it in the row */
            //convertView = ( RelativeLayout ) inflater.inflate( resource, null );
           
           final DocResult o = items.get(position);
           if (convertView == null) {
	               switch (type) {
	                   case 0: //tag
	                	  
	                	   //this is not really scanned doc - but a tag
	                	   layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                       v = convertView = layoutInflater.inflate(R.layout.tag_row, null);
	                       
	                       TextView tag_name = (TextView) convertView.findViewById(R.id.TagRow1);
	                       
	                       if(tag_name!=null)
	                    	   tag_name.setText(o.id());
	                       
	                       break;
	                   case 1: //document
	                	   
	                	   layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                       v = convertView = layoutInflater.inflate(R.layout.record_row, null);
	                       
	                       TextView t1 = (TextView) convertView.findViewById(R.id.TextView01);
	                       TextView t3 = (TextView) convertView.findViewById(R.id.TextViewDate);
	                       /* Take the ImageView from layout and set the city's image */
	                       
	                       
	                       ImageView image = (ImageView) v.findViewById(R.id.record_Icon1);
	                       
	                       
	               		  ImageButton button = 
	               				(ImageButton)v.findViewById( R.id.imageButton1);
	               			button.setOnClickListener(imgButtonHandler);
	               			
	                       if(image != null && o.scannedDoc() != null && o.scannedDoc().bitmap() != null)
	                    	   image.setImageBitmap(o.scannedDoc().bitmap());
	//                           String uri = "drawable/" + o.getImage();
	//                           int imageResource = c.getResources().getIdentifier(uri, null, c.getPackageName());
	//                           Drawable image = c.getResources().getDrawable(imageResource);
	//                           imageCity.setImageDrawable(image);
	                      
	                       if(t1!=null)
	                    	   t1.setText(o.scannedDoc().name());
	                       if(t3!=null)
	                    	   t3.setText(o.date());
	                       break;
	               }
	           } else {
	
	           }
           }
           return v;
   }
}
