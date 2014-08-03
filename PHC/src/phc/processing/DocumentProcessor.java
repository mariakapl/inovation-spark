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
	
	private HashMap<String, BaseDocProcessor> _processorByTag;
	
	private DocumentProcessor()
	{
		_processorByTag = new HashMap<String, BaseDocProcessor>();
		_processorByTag.put(TagExtractor.BLOOD_TESTS_TAG, new BloodTestProcessor());
	}
	
	public void process(Context context, DocResult doc)
	{
		List<BaseDocProcessor> processors = getDocProcessors(doc);
		for (BaseDocProcessor p : processors)
			p.process(context, doc);
	}
	
	private List<BaseDocProcessor> getDocProcessors(DocResult doc)
	{
		List<BaseDocProcessor> processors = new ArrayList<BaseDocProcessor>();
		for (String tag : doc.scannedDoc().tags())
		{
			if (_processorByTag.containsKey(tag))
				processors.add(_processorByTag.get(tag));
		}
		return processors;
	}
}
