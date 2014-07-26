package com.example.phc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class NoticeDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	
    	LayoutInflater inflater = getActivity().getLayoutInflater();
    
    	View view = inflater.inflate(R.layout.create_tag_dialog, null);
    	final EditText edit_text = (EditText)view.findViewById(R.id.new_tag_name);
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view).
        	   setTitle("Create New Tag").
               setPositiveButton("Save", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // Send the positive button event back to the host activity
                       //mListener.onDialogPositiveClick(NoticeDialogFragment.this);
                	   String text = edit_text.getText().toString();
                	   ((TagActivity)getActivity()).doPositiveClick(text);
                   }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // Send the negative button event back to the host activity
                      // mListener.onDialogNegativeClick(NoticeDialogFragment.this);
                	   ((TagActivity)getActivity()).doNegativeClick();
                   }
               });
        return builder.create();
    }
}
