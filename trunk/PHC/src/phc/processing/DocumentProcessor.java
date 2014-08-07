package phc.processing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.example.phc.TagExtractor;

import phc.objects.DocResult;

public class DocumentProcessor {
	
	private static DocumentProcessor _sInstance = new DocumentProcessor();
	public static DocumentProcessor instance() {
		return _sInstance;
	}
	
	private HashMap<String, IDocProcessor> _processorByTag;
	
	private DocumentProcessor()
	{
		_processorByTag = new HashMap<String, IDocProcessor>();
		_processorByTag.put(TagExtractor.BLOOD_TESTS_TAG, new BloodTestProcessor());
		_processorByTag.put(TagExtractor.PRESCRIPTIONS_TAG, new MedicineProcessor());
	}
	
	public void clear(Context context)
	{
		for (IDocProcessor p : _processorByTag.values())
			p.clear(context);
	}
	
	public void process(Context context, DocResult doc)
	{
		List<IDocProcessor> processors = getDocProcessors(doc);
		for (IDocProcessor p : processors)
			p.process(context, doc);
	}
	
	private List<IDocProcessor> getDocProcessors(DocResult doc)
	{
		List<IDocProcessor> processors = new ArrayList<IDocProcessor>();
		for (String tag : doc.scannedDoc().tags())
		{
			if (_processorByTag.containsKey(tag))
				processors.add(_processorByTag.get(tag));
		}
		return processors;
	}

	public void readDataForDoc(Context context, DocResult doc) {
		List<IDocProcessor> processors = getDocProcessors(doc);
		for (IDocProcessor p : processors)
			p.readDataForDoc(context, doc);
	}

	public void copyAllAssets(Context context) {
		for (IDocProcessor processor : _processorByTag.values())
				processor.copyAllAssets(context);
	}
}
