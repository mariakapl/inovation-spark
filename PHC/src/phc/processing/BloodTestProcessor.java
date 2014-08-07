package phc.processing;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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

public class BloodTestProcessor implements IDocProcessor {

	private static final String TXT_EXTENSION = ".txt";
	private static final String HEMOGLOBIN = "Hemoglobin";
	
	private static final String [] _tests = {
		HEMOGLOBIN, "Glucose", "Platelets"
	};
	private HashMap<String, Pattern> _patterns;

	private static final String [][] _associatedMedicine = {
		{HEMOGLOBIN, MedicineProcessor.ASPIRIN}
	};
	
	private static HashMap<String, List<String>> _associatedMedicineHash;
	
	public BloodTestProcessor() {
		_patterns = new HashMap<String, Pattern>();
		for (String test : _tests) {
			Pattern p = Pattern.compile(
				"\\b(" + test.toLowerCase() + ")\\s+((\\d|\\.)+)\\s+(.*)");
			_patterns.put(test, p);
		}
		_associatedMedicineHash = new HashMap<String, List<String>>();
		for (String [] assoc : _associatedMedicine) {
			List<String> list = Arrays.asList(assoc).subList(1, assoc.length);
			_associatedMedicineHash.put(assoc[0], list);
		}
	}

	private static File getDir(Context context)
	{
		File dir = context.getDir("BloodTest", Context.MODE_PRIVATE);;
		if (! dir.exists())
			dir.mkdir(); 
		return dir;
	}

	@Override
	public void readDataForDoc(Context context, DocResult doc)
	{
		for (String test : _tests)
		{
			List<BloodTest> all = readData(context, test);
			if (all == null)
				continue;
			for (BloodTest t : all)
			{
				if (t.DocId.equals(doc.id()))
					doc.addBloodTest(t);
			}
		}
	}
	
	public static List<BloodTest> readData(Context context, String test)
	{
		File dir = getDir(context);
		File file = new File(dir, test + TXT_EXTENSION);
		if (! file.exists())
			return null;
		List<String> lines = Utils.readTextFile(file);
		List<BloodTest> values = new ArrayList<BloodTest>();
		for (String line : lines) {
			String [] parts = line.split("\\t");
			BloodTest bt = new BloodTest(test, parts[0], parts[1], parts[2], parts[3]);
			values.add(bt);
		}
		final SimpleDateFormat df = new SimpleDateFormat(DocStorage.DateFormat);
		Collections.sort(values, new Comparator<BloodTest>() {
			@Override
			public int compare(BloodTest bt1, BloodTest bt2) {
				Date d1 = Utils.getDateFromString(bt1.Date);
				Date d2 = Utils.getDateFromString(bt2.Date);
				return d1.compareTo(d2);
			}
		});
		return values;
	}
	
	public void writeData(Context context, BloodTest test)
	{
		File dir = getDir(context);
		File file = new File(dir, test.Name + TXT_EXTENSION);
		String s = "";
		if (file.exists())
			s = Utils.readTextFileAsString(file);
		s = s + test.DocId + "\t" + test.Value + "\t" + test.Units + "\t" + test.Date + "\n";
		Utils.writeTextFile(file, s);
	}
	
	@Override
	public boolean process(Context context, DocResult doc) {
		String ocr = doc.scannedDoc().ocr().toLowerCase();
		HashMap<String, BloodTest> matches = new HashMap<String, BloodTest>();
		for (String test : _patterns.keySet()) {
			Pattern p = _patterns.get(test);
			Matcher m = p.matcher(ocr);
			if (m.find()) {
				BloodTest bt = new BloodTest(test, doc.id(), m.group(2), m.group(4), doc.date());
				matches.put(test, bt);
			}
		}
		if (matches.size() > 0)
		{
			for (BloodTest t : matches.values()) {
				writeData(context, t);
				doc.addBloodTest(t);
			}
		}
		return true;
	}

	@Override
	public void clear(Context context) {
		File dir = getDir(context);
		for (String test : _tests)
			new File(dir, test + TXT_EXTENSION).delete();
	}

	public static List<Medicine> readAssociatedMedicineData(Context context,
			String name) {
		List<String> list = _associatedMedicineHash.get(name);
		if (list == null || list.size() == 0)
			return null;
		List<Medicine> meds = new ArrayList<Medicine>();
		for (String med : list) {
			List<Medicine> medList = MedicineProcessor.getMedicineHistory(context, med);
			if (medList == null)
				continue;
			meds.addAll(medList);
		}
		Collections.sort(meds, new Comparator<Medicine>() {
			@Override
			public int compare(Medicine m1, Medicine m2) {
				Date d1 = Utils.getDateFromString(m1.StartDate);
				Date d2 = Utils.getDateFromString(m2.StartDate);
				if (d1 == null || d2 == null)
					return 0;
				return d1.compareTo(d2);
			}
		});
		return meds;
	}

	@Override
	public void copyAllAssets(Context context) {
		Utils.copyAllAssets(context, "app_BloodTest", getDir(context));
	}
}
