package phc.processing;

import java.util.regex.Pattern;

public class MedicineProcessor extends SimpleTermDocProcessor {

	private static final String [] _medicine = {
		"Aspirin"
	};
	
	public MedicineProcessor() {
		super("Medicine");
	}

	@Override
	protected String[] terms() {
		return _medicine;
	}
	
	@Override
	Pattern pattern(String term) {
		return Pattern.compile("\\b(" + term + ")\\s+((\\d|\\.)+)");
	}
}
