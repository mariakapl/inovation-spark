package phc.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import android.content.Context;

import phc.interfaces.IDocStorage;
import phc.objects.DocResult;
import phc.objects.ScannedDoc;

public class DocStorage implements IDocStorage {

	private File _storageDir;
	private File _imagesDir;
	private File _tagIndex;
	private HashMap<String, List<String>> _docsByTag;	// tag -> doc ids
	private HashMap<String, DocResult> _docsById;		// doc id -> doc
	private HashMap<String, List<String>> _tagsByDoc;	// doc id -> tags
	
	public DocStorage(Context context)
	{
		_storageDir = context.getFilesDir();
		_imagesDir = new File(_storageDir, "Images");
		if (! _imagesDir.exists())
			_imagesDir.mkdirs();
		_tagIndex = new File(_storageDir, "index.txt");
		readTagIndex(_tagIndex);
	}
	
	public static List<String> readTextFile(File file)
	{
		List<String> lines = new ArrayList<String>();
		try {
		    BufferedReader br = new BufferedReader(new FileReader(file));
		    String line;
		    while ((line = br.readLine()) != null)
		    	lines.add(line);
		    br.close();
		}
		catch (IOException e) {
			return null;
		}
		return lines;
		
	}
	
	private void readTagIndex(File tagIndex) {
		_docsByTag = new HashMap<String, List<String>>();
		_docsById = new HashMap<String, DocResult>();
		if (! tagIndex.exists())
			return;
		
		List<String> lines = readTextFile(tagIndex);
		for (String line: lines)
		{
			String [] parts = line.split(",");
			List<String> tags = new ArrayList<String>();
			String docId = parts[0];
			for (int i = 1; i < parts.length; i++)
			{
				String tag = parts[i];
				tags.add(tag);
				addDocToTag(tag, docId);
			}
			_tagsByDoc.put(docId, tags);
		}
	}

	private void addDocToTag(String tag, String docId) {
		if (! _docsByTag.containsKey(tag))
			_docsByTag.put(tag, new ArrayList<String>());
		_docsByTag.get(tag).add(docId);
	}

	@Override
	public DocResult addDoc(ScannedDoc doc) {
		if (_tagIndex.exists())
		{
			
		}
		return null;
	}

	@Override
	public boolean deleteDoc(DocResult doc) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<DocResult> queryDocsByTags(Collection<String> tags) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getExistingTags() {
		// TODO Auto-generated method stub
		return null;
	}

}
