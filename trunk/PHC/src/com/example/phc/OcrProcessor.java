package com.example.phc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class OcrProcessor {

	public static final String [][] Tags = {
		{"drug"},
		{"medicine"},
	    {"Imaging", "x-ray", "ct", "mri"},
	    {"allergy"},
	    {"treatment"},
	    {"cancer"},
	    {"lab test", "blood test", "urine test"},
	    {"prescription"},
	    {"doctor referrals"}

	};
//	private static final HashSet<String> _sTags =
//		new HashSet<String>(Arrays.asList(Tags));

	private String _ocr;
	
	public OcrProcessor(String ocr) {
		_ocr = ocr.toLowerCase();
	}
	
	public Collection<String> suggestedTags() {
		ArrayList<String> tags = new ArrayList<String>();
		for (int i = 0; i < Tags.length; i++) {
			for(int j = 0;  j < Tags[i].length; j++ )
				if (_ocr.contains(Tags[i][j]))
					tags.add(Tags[i][j]);
		}
		return tags;
	}

}
