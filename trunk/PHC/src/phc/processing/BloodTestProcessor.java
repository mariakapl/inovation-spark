package phc.processing;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import phc.objects.DocResult;
import phc.utils.Utils;
import android.content.Context;

public class BloodTestProcessor implements IDocProcessor {

	private static final String TXT_EXTENSION = ".txt";

	private static final String [] _tests = {
		"Hemoglobin"
	};
	private HashMap<String, Pattern> _patterns;

	public BloodTestProcessor() {
		_patterns = new HashMap<String, Pattern>();
		for (String test : _tests) {
			Pattern p = Pattern.compile("\\b(" + test.toLowerCase() + ")\\s+((\\d|\\.)+)");
			_patterns.put(test, p);
		}
	}

	private File getDir(Context context)
	{
		File dir = context.getDir("BloodTest", Context.MODE_PRIVATE);;
		if (! dir.exists())
			dir.mkdir(); 
		return dir;
	}

	public HashMap<String, String> readData(Context context, String test)
	{
		File dir = getDir(context);
		File file = new File(dir, test + TXT_EXTENSION);
		String s = "";
		if (! file.exists())
			return null;
		List<String> lines = Utils.readTextFile(file);
		HashMap<String, String> values = new HashMap<String, String>();
		for (String line : lines) {
			String [] parts = line.split("\\t");
			values.put(parts[0], parts[1]);
		}
		return values;
	}
	
	public void writeData(Context context, String test, String value, String date)
	{
		File dir = getDir(context);
		File file = new File(dir, test + TXT_EXTENSION);
		String s = "";
		if (file.exists())
			s = Utils.readTextFileAsString(file);
		s = s + value + "\t" + date + "\n";
		Utils.writeTextFile(file, s);
	}
	
	@Override
	public boolean process(Context context, DocResult doc) {
		String ocr = doc.scannedDoc().ocr();
		HashMap<String, String> matches = new HashMap<String, String>();
		for (String test : _patterns.keySet()) {
			Pattern p = _patterns.get(test);
			Matcher m = p.matcher(ocr);
			if (m.find())
				matches.put(test, m.group(2));
		}
		if (matches.size() > 0)
		{
			for (String m : matches.keySet())
				writeData(context, m, matches.get(m), doc.date());
		}
		return true;
	}

	@Override
	public void clear(Context context) {
		File dir = getDir(context);
		for (String test : _tests)
			new File(dir, test + TXT_EXTENSION).delete();
	}
}
