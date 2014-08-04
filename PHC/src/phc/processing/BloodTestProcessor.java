package phc.processing;

import java.util.regex.Pattern;

public class BloodTestProcessor extends SimpleTermDocProcessor {

	private static final String [] _tests = {
		"Hemoglobin"
	};
	
	public BloodTestProcessor() {
		super("BloodTest");
	}

	@Override
	protected String[] terms() {
		return _tests;
	}

	@Override
	Pattern pattern(String term) {
		return Pattern.compile("\\b(" + term + ")\\s+((\\d|\\.)+)");
	}
	
}
