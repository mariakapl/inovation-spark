package com.example.phc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class TagExtractor {

	public static final String BLOOD_TESTS_TAG = "Blood Tests";
	public static final String PRESCRIPTIONS_TAG = "Prescriptions";
	
	public static final String ROOT_TAG = "@Root@";
	public static final String [][] Tags = {
	    {"Lab Tests", BLOOD_TESTS_TAG, "Urine Tests"},
	    {"Doctor Referrals"},
	    {"Hospital Release Notes"},
		{PRESCRIPTIONS_TAG},
	    {"Imaging", "X-ray", "CT", "MRI"},
	    {"Allergy"},
	    {"Vaccinations"}
	};
	private static HashMap<String, List<String>> _sBuiltInTagTree;
	public static final HashMap<String, List<String>> builtInTagTree() {
		return _sBuiltInTagTree;
	}

	public static final String [][] RelevantWords = {
	    {BLOOD_TESTS_TAG, "Hemoglobin"},
	    {"Urine Tests", "Protein"},
	    {"Doctor Referrals", "Exam", "Pain", "Headache"},
		{PRESCRIPTIONS_TAG, "Drug", "Antibiotics", "Drops", "Pill", "Aspirin"},
	    {"Vaccinations", "varicella", "rubella"}
	};
	
	private static HashMap<String, List<String>> _sExpressionsToTags;

	static {
		// Create the built-in tag tree
		_sBuiltInTagTree = new HashMap<String, List<String>>();
		List<String> topLevel = new ArrayList<String>();
		for (String [] tags : TagExtractor.Tags) {
			String parent = tags[0];
			List<String> children = new ArrayList<String>(
				Arrays.asList(tags).subList(1, tags.length));
			_sBuiltInTagTree.put(parent, children);
			topLevel.add(parent);
		}
		_sBuiltInTagTree.put(ROOT_TAG, topLevel);
		
		// Create the input -> tag mapping
		// First add the "RelevantWords"
		_sExpressionsToTags = new HashMap<String, List<String>>();
		for (String [] words : RelevantWords) {
			String tag = words[0];
			for (int i = 1; i < words.length; i++) {
				String word = words[i].toLowerCase();
				if (!_sExpressionsToTags.containsKey(word))
					_sExpressionsToTags.put(word, new ArrayList<String>());
				_sExpressionsToTags.get(word).add(tag);
			}
		}
		// Then the tags themselves
		for (String [] tags : Tags) {
			for (String tag : tags) {
				String tagl = tag.toLowerCase();
				if (!_sExpressionsToTags.containsKey(tagl))
					_sExpressionsToTags.put(tagl, new ArrayList<String>());
				_sExpressionsToTags.get(tagl).add(tag);				
			}
		}
	}
	
	private String _ocr;
	
	public TagExtractor(String ocr) {
		_ocr = ocr.toLowerCase();
	}
		
	public HashSet<String> suggestedTags() {
		HashSet<String> tags = new HashSet<String>();
		for (String s : _sExpressionsToTags.keySet()) {
			if (_ocr.contains(s))
				tags.addAll(_sExpressionsToTags.get(s));
		}
		return tags;
	}

}
