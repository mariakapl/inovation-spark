package com.example.phc;

import phc.objects.DocResult;

public class WorkflowManager {

	public static Class nextActivity(DocResult doc) {
		if (doc.scannedDoc().tags().contains(OcrProcessor.BLOOD_TESTS_TAG))
			return BloodTestActivity.class;
		return MainActivity.class;
	}
}
