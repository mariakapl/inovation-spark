package phc.processing;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import phc.objects.DocResult;
import phc.utils.Utils;

public abstract class SimpleTermDocProcessor implements IDocProcessor
{
	private static final String TXT_EXTENSION = ".txt";

	abstract protected String [] terms();
	abstract Pattern pattern(String term);
	
	private String _name;
	private List<String> _terms;
	private List<Pattern> _patterns;
	
	public SimpleTermDocProcessor(String name)
	{
		_name = name;
		_terms = Arrays.asList(terms());
		_patterns = new ArrayList<Pattern>();
		for (String term : _terms) {
			Pattern p = pattern(term);
			if (p != null)
				_patterns.add(p);
		}
	}
	
	private File getDir(Context context)
	{
		File dir = context.getDir(_name, Context.MODE_PRIVATE);;
		if (! dir.exists())
			dir.mkdir(); 
		return dir;
	}
	
	public boolean process(Context context, DocResult doc)
	{
		String ocr = doc.scannedDoc().ocr();
		HashMap<String, String> matches = new HashMap<String, String>();
		for (Pattern p : _patterns) {
			Matcher m = p.matcher(ocr);
			if (m.find())
				matches.put(m.group(1), m.group(2));
		}
		if (matches.size() > 0)
		{
			File dir = getDir(context);
			for (String m : matches.keySet()) {
				File file = new File(dir, m + TXT_EXTENSION);
				String s = "";
				if (file.exists())
					s = Utils.readTextFileAsString(file);
				s = s + matches.get(m) + "\t" + doc.date() + "\n";
				Utils.writeTextFile(file, s);
			}
		}
		return true;
	}
	
	public void clear(Context context)
	{
		File dir = getDir(context);
		for (String term : _terms)
			new File(dir, term + TXT_EXTENSION).delete();
	}
}
