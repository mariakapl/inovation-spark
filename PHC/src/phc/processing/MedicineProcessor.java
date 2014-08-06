package phc.processing;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import phc.objects.DocResult;
import phc.utils.Utils;
import android.content.Context;

public class MedicineProcessor implements IDocProcessor {

	private static final String TXT_EXTENSION = ".txt";
	private static final String [] _medicine = {
		"Aspirin"
	};
	private static List<Pattern> _patterns;
	
	public MedicineProcessor() {
		_patterns = new ArrayList<Pattern>();
		for (String m : _medicine) {
			Pattern p = Pattern.compile(":\\s+(" + m.toLowerCase() + ").*\\n" +
				".*:\\s+(\\d+).*per day.*\\n" +
				".*:\\s+(\\d+)");
			_patterns.add(p);
		}
	}

	private File getDir(Context context)
	{
		File dir = context.getDir("Medicine", Context.MODE_PRIVATE);;
		if (! dir.exists())
			dir.mkdir(); 
		return dir;
	}
	
	public boolean process(Context context, DocResult doc)
	{
		String ocr = doc.scannedDoc().ocr();
		HashMap<String, Integer> matches = new HashMap<String, Integer>();
		for (Pattern p : _patterns) {
			Matcher m = p.matcher(ocr);
			if (m.find()) {
				String nPerDay = m.group(2);
				String nPills = m.group(3);
				int days = Integer.parseInt(nPills) / Integer.parseInt(nPerDay);
				matches.put(m.group(1), days);
			}
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
		for (String med : _medicine)
			new File(dir, med + TXT_EXTENSION).delete();
	}

}
