package phc.processing;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import phc.objects.BloodTest;
import phc.objects.DocResult;
import phc.objects.Medicine;
import phc.storage.DocStorage;
import phc.utils.Utils;
import android.content.Context;

public class MedicineProcessor implements IDocProcessor {

	private static final String TXT_EXTENSION = ".txt";
	public static final String ASPIRIN = "Aspirin";
	
	private static final String [] _medicine = {
		ASPIRIN
	};
	private static HashMap<String, Pattern> _patterns;
	
	public MedicineProcessor() {
		_patterns = new HashMap<String, Pattern>();
		for (String m : _medicine) {
			Pattern p = Pattern.compile(":\\s+(" + m.toLowerCase() + ").*\\n" +
				".*:\\s+(\\d+).*per day.*\\n" +
				".*:\\s+(\\d+)");
			_patterns.put(m, p);
		}
	}

	private static File getDir(Context context)
	{
		File dir = context.getDir("Medicine", Context.MODE_PRIVATE);;
		if (! dir.exists())
			dir.mkdir(); 
		return dir;
	}
	
	public boolean process(Context context, DocResult doc)
	{
		String ocr = doc.scannedDoc().ocr();
		HashMap<String, Medicine> matches = new HashMap<String, Medicine>();
		for (String med : _medicine) {
			Pattern p = _patterns.get(med);
			Matcher m = p.matcher(ocr);
			if (m.find()) {
				String nPerDay = m.group(2);
				String nPills = m.group(3);
				int days = Integer.parseInt(nPills) / Integer.parseInt(nPerDay);
				Medicine medicine = new Medicine(med, doc.id(), doc.date(), days, m.group(3));
				matches.put(med, medicine);
			}
		}
		if (matches.size() > 0)
		{
			for (Medicine m : matches.values())
				writeData(context, m);
		}
		return true;
	}
	
	public void writeData(Context context, Medicine med)
	{
		File dir = getDir(context);
		File file = new File(dir, med.Name + TXT_EXTENSION);
		String s = "";
		if (file.exists())
			s = Utils.readTextFileAsString(file);
		s = s + med.DocId + "\t" + med.StartDate + "\t" + med.Amount + "\t" + med.NumDays + "\n";
		Utils.writeTextFile(file, s);
	}
	
	public void clear(Context context)
	{
		File dir = getDir(context);
		for (String med : _medicine)
			new File(dir, med + TXT_EXTENSION).delete();
	}

	public static List<Medicine> getMedicineHistory(Context context, String med)
	{
		File dir = getDir(context);
		File file = new File(dir, med + TXT_EXTENSION);
		if (! file.exists())
			return null;
		List<String> lines = Utils.readTextFile(file);
		List<Medicine> values = new ArrayList<Medicine>();
		for (String line : lines) {
			String [] parts = line.split("\\t");
			//String name, String docId, String startDate, int numDays, String amount
			Medicine bt = new Medicine(med, parts[0], parts[1], Integer.valueOf(parts[2]), parts[3]);
			values.add(bt);
		}
		return values;
	}
}
