package com.example.phc;

import java.util.HashMap;
import java.util.List;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
 
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    public HashMap<Integer,Boolean> checkboxMapSuggested = new HashMap<Integer,Boolean>();
    public HashMap<Integer,Boolean> checkboxMapExisting = new HashMap<Integer,Boolean>();
 
    public ExpandableListAdapter(Context context, List<String> listDataHeader,
            HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }
 
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
 
        final String childText = (String) getChild(groupPosition, childPosition);
 
       // if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View grid = infalInflater.inflate(R.layout.tag_list_view_item, null);
            
        //}
 
        CheckBox txtListChild = (CheckBox)grid.findViewById(R.id.checkboxListItem);
        txtListChild.setText(childText);
        
        if(groupPosition == 0) {
        	if (!checkboxMapSuggested.containsKey(childPosition))
        		SetCheck(groupPosition, childPosition, true);	// by default, checked
        	txtListChild.setChecked(checkboxMapSuggested.get(childPosition));
        }
        if(groupPosition == 1) {
        	if (!checkboxMapExisting.containsKey(childPosition))
        		SetCheck(groupPosition, childPosition, false);	// by default, unchecked
       	 	txtListChild.setChecked(checkboxMapExisting.get(childPosition));
        }
        
        CheckListener checkL = new CheckListener();
        checkL.setPosition(groupPosition,childPosition);
        txtListChild.setOnCheckedChangeListener(checkL);
        
        return grid;
    }
 
    private final class CheckListener implements OnCheckedChangeListener{

        long pos;
        long parent;

        public void setPosition(long par, long p){
            pos = p;
            parent = par;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked) {
        	//checkboxMap.put((long)pos, isChecked);
        	SetCheck((long)parent, (long)pos, isChecked);
        }
    }
    
	public void SetCheck(long group_pos, long pos, boolean isChecked) {
		
       if(group_pos == 0)
           checkboxMapSuggested.put((int)pos, isChecked);
       if(group_pos == 1)
    	   checkboxMapExisting.put((int)pos, isChecked);
	}
	
    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }
 
    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.tag_list_view_header, null);
        }
 
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
 
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}