package com.example.phc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class OcrProcessor {

	public static final String [] Tags = {
		"drug", "medicine",
		"hospital", "x-ray", "ct", "mri", "allergy",
		"treatment", "cancer", "orthopedics", "pullmonary",
		"eye", "ear", "bone", "lab test", "blood test", "urine test",
		"prescription", "doctor referrals"

	};
	private static final HashSet<String> _sTags =
		new HashSet<String>(Arrays.asList(Tags));

	private String _ocr;
	
	public OcrProcessor(String ocr) {
		_ocr = ocr.toLowerCase();
	}
	
	public Collection<String> suggestedTags() {
		ArrayList<String> tags = new ArrayList<String>();
		for (String tag : Tags) {
			if (_ocr.contains(tag))
				tags.add(tag);
		}
		return tags;
	}

}
