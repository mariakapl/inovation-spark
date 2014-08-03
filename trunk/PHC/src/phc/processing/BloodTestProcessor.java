package phc.processing;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import phc.objects.DocResult;
import phc.utils.Utils;

public class BloodTestProcessor extends BaseDocProcessor {

	private static final String TXT_EXTENSION = ".txt";
	private static final String [] _tests = {
		"Glucose"
	};
	private static ArrayList<Pattern> _patterns = new ArrayList<Pattern>();
	
	static {
		for (String test : _tests)
			_patterns.add(Pattern.compile("\\b(" + test + ")\\s+((\\d|\\.)+)"));
	}
	
	public BloodTestProcessor() {
	}
	
	@Override
	public boolean process(Context context, DocResult doc) {
		String ocr = doc.scannedDoc().ocr();
		HashMap<String, String> tests = new HashMap<String, String>();
		for (Pattern p : _patterns) {
			Matcher m = p.matcher(ocr);
			if (m.find())
				tests.put(m.group(1), m.group(2));
		}
		if (tests.size() > 0)
		{
			File bloodTestDir = context.getDir("BloodTest", Context.MODE_PRIVATE);
			if (! bloodTestDir.exists())
				bloodTestDir.mkdir();
			for (String test : tests.keySet()) {
				File testFile = new File(bloodTestDir, test + TXT_EXTENSION);
				String s = "";
				if (testFile.exists())
					s = Utils.readTextFileAsString(testFile);
				s = s + tests.get(test) + "\t" + doc.date() + "\n";
				Utils.writeTextFile(testFile, s);
			}
		}
		
		return true;
	}

}
