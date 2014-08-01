package com.example.phc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class OcrProcessor {

	public static final String [][] Tags = {
	    {"Lab Tests", "Blood Tests", "Urine Tests"},
	    {"Doctor Referrals"},
	    {"Hospital Release Notes"},
		{"Prescriptions"},
	    {"Imaging", "X-ray", "CT", "MRI"},
	    {"Allergy"},
	    {"Vaccinations"},
	    {"x"},
	    {"y"},
	    {"z"},
	    {"t"},
	    {"r"},

	};
	
	public static final String [][] RelevantWords = {
	    {"Lab Tests", "Blood Tests", "Urine Tests"},
	    {"Blood Tests", "Glucose"},
	    {"Urine Tests", "Protein"},
	    {"Doctor Referrals", "Exam", "Pain", "Headache"},
	    {"Hospital Release Notes"},
		{"Prescriptions", "Drug", "Antibiotics", "Drops", "Pill"},
	    {"Imaging", "X-ray", "CT", "MRI"},
	    {"X-ray"},
	    {"CT"},
	    {"MRI"},
	    {"Allergy"},
	    {"Vaccinations", "varicella", "rubella"}

	};
	
//	private static final HashSet<String> _sTags =
//		new HashSet<String>(Arrays.asList(Tags));

	private String _ocr;
	
	public OcrProcessor(String ocr) {
		_ocr = ocr.toLowerCase();
	}
	
	private boolean checkIfMatches(String ocr, String tag)
	{
		for (int i = 0; i < RelevantWords.length; i++) {
			if (tag.equals(RelevantWords[i][0]))
			{
				for(int j = 0;  j < RelevantWords[i].length; j++ )
					if (_ocr.equals(RelevantWords[i][j].toLowerCase()))
						return true;
			}
		}
		return false;
	}
	
	public Collection<String> suggestedTags() {
		ArrayList<String> tags = new ArrayList<String>();
		for (int i = 0; i < Tags.length; i++) {
			for(int j = 0;  j < Tags[i].length; j++ )
				if (checkIfMatches(_ocr, Tags[i][j]))
					tags.add(Tags[i][j]);
		}
		return tags;
	}

}
