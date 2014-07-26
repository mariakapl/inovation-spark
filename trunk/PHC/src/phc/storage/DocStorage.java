package phc.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import phc.interfaces.IDocStorage;
import phc.objects.DocResult;
import phc.objects.ScannedDoc;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;



public class DocStorage implements IDocStorage {

	private static final String TAG_TREE_FILE = "tagTree.txt";
	private static final String TXT_EXTENSION = ".txt";
	private static final String BITMAP_EXTENSION = ".png";
	private static final String ROOT_TAG = "@Root@";
	
	/* Storage is implemented as the following:
	 * 1. Images directory stores all images, with the name <id>.png
	 *    (<id> is a number, allocated when a document is added)
	 * 2. Tags directory stores all the tags of the documents. Each
	 *    document has a single file with the name <id>.txt, which contains
	 *    the tags of the document, one per line.
	 * 3. OCR directory stores the OCR of the documents. Each document
	 *    has a single file with the OCR output of that document.
	 * 4. tagTree.txt stores the hierarchy of tags. Each line specifies
	 *    the parent tag, followed by the child tags, tab-separated.
	 */
	private Context _context;
	private File _storageDir;
	private File _imagesDir;
	private File _tagsDir;
	private File _ocrDir;
	private File _tagTreeFile;

	private HashMap<String, HashSet<String>> _docsByTag;	// tag -> doc ids
	private HashMap<String, HashSet<String>> _tagsByDoc;	// doc id -> tags
	private HashMap<String, DocResult> _docsById;		// doc id -> doc
	private HashMap<String, List<String>> _tagTree;
	
	private TagTree _tagsOfTag; //for each tag, get the under tags - not good! should be tree or something
	
	private static IDocStorage _instance;
	public 
	static IDocStorage get() {
		return _instance;
	}
	public static IDocStorage create(Context context) {
		return (_instance = new DocStorage(context));
	}
	
	private DocStorage(Context context)
	{
		_context = context;
		_storageDir = context.getFilesDir();
		_imagesDir = new File(_storageDir, "Images");
		if (! _imagesDir.exists())
			_imagesDir.mkdirs();
		_tagsDir = new File(_storageDir, "Tags");
		if (! _tagsDir.exists())
			_tagsDir.mkdirs();
		_ocrDir = new File(_storageDir, "OCR");
		if (! _ocrDir.exists())
			_ocrDir.mkdirs();
		_tagTreeFile = new File(_storageDir, TAG_TREE_FILE);
		load();
	}
	

	public boolean writeTextFile(File file, String s)
	{
		FileOutputStream fos;
		try {
			fos = _context.openFileOutput(file.getAbsolutePath(), Context.MODE_PRIVATE);
			fos.write(s.getBytes());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void load() {
		File [] files = _imagesDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File file, String name) {
				return name.endsWith(BITMAP_EXTENSION);
			}
		});
		List<String> docIds = new ArrayList<String>();
		for (File f : files) {
			String name = f.getName();
			docIds.add(name.substring(0, name.length() - BITMAP_EXTENSION.length()));
		}
		
		_docsByTag = new HashMap<String, HashSet<String>>();
		_tagsByDoc = new HashMap<String, HashSet<String>>();
		_docsById = new HashMap<String, DocResult>();
		_tagTree = new HashMap<String, List<String>>();
		
		for (String id : docIds) {
			List<String> tags = readTextFile(new File(_tagsDir, id + TXT_EXTENSION));
			_tagsByDoc.put(id, new HashSet<String>(tags));
			for (String tag : tags)
				addDocToTag(tag, id);
		}
		
		if (_tagTreeFile.exists()) {
			List<String> lines = readTextFile(_tagTreeFile);
			for (String line : lines) {
				String [] parts = line.split("\t");
				String parent = parts[0];
				List<String> children = Arrays.asList(parts).subList(1, parts.length);
				_tagTree.put(parent, children);
			}
		}
	}

	private DocResult load(String id) {
		if (!_docsById.containsKey(id)) {
			File image = new File(_imagesDir, id + BITMAP_EXTENSION);
			Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
			File ocrFile = new File(_ocrDir, id + TXT_EXTENSION);
			String ocr = readTextFileAsString(ocrFile);
			File tagFile = new File(_tagsDir, id + TXT_EXTENSION);
			List<String> tags = readTextFile(tagFile);
			_docsById.put(id, new DocResult(new ScannedDoc(bitmap, tags, ocr), id));
		}
		return _docsById.get(id);
	}
	
	private boolean save(String id, ScannedDoc doc) {
		if (doc.bitmap() != null) {
			File image = new File(_imagesDir, id + BITMAP_EXTENSION);
			FileOutputStream fOut;
			try {
				fOut = new FileOutputStream(image);
			    doc.bitmap().compress(Bitmap.CompressFormat.PNG, 100, fOut);
			    fOut.flush();
			    fOut.close();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		if (doc.ocr() != null) {
			File ocr = new File(_ocrDir, id + TXT_EXTENSION);
			writeTextFile(ocr, doc.ocr());
		}
		if (doc.tags() != null) {
			StringBuilder sb = new StringBuilder();
			for (String tag : doc.tags()) {
				if (sb.length() == 0)
					sb.append("\n");
				sb.append(tag);
			}
			File tagFile = new File(_tagsDir, id + TXT_EXTENSION);
			writeTextFile(tagFile, sb.toString());
		}
		return true;
	}

	private void addTagToDoc(String docId, String tag) {
		if (!_tagsByDoc.containsKey(docId))
			_tagsByDoc.put(docId, new HashSet<String>());
		_tagsByDoc.get(docId).add(tag);
	}

	public static String join(List<String> lines, String separator) {
		StringBuilder sb = new StringBuilder();
		for (String line : lines)
			sb.append(line + "\n");
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}
	
	public static String readTextFileAsString(File file)
	{
		List<String> lines = readTextFile(file);
		return join(lines, "\n");
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
	
	private void addDocToTag(String tag, String docId) {
		if (! _docsByTag.containsKey(tag))
			_docsByTag.put(tag, new HashSet<String>());
		_docsByTag.get(tag).add(docId);
	}

	private String allocateDocId()	{
		for (int i = 1; i < 1000; i++) {
			String id = String.valueOf(i);
			if (!_tagsByDoc.containsKey(id))
				return id;
		}
		return null;
	}
	
	@Override
	public DocResult addDoc(ScannedDoc doc) {
		String id = allocateDocId();
		if (id == null)
			return null;
		_tagsByDoc.put(id, new HashSet<String>(doc.tags()));
		for (String tag : doc.tags())
			addDocToTag(tag, id);
		save(id, doc);
		return null;
	}

	@Override
	public boolean deleteDoc(DocResult doc) {
		new File(_imagesDir, doc.id() + BITMAP_EXTENSION).delete();
		new File(_tagsDir, doc.id() + TXT_EXTENSION).delete();
		new File(_ocrDir, doc.id() + TXT_EXTENSION).delete();
		return false;
	}
	
	@Override
	public Collection<DocResult> queryDocsByTags(Collection<String> tags) {
		Collection<DocResult> res = new ArrayList<DocResult>();
		if (tags.isEmpty())
			return res;
		ArrayList<String> queryTags = new ArrayList<String>(tags);
		HashSet<String> docs = new HashSet<String>(_docsByTag.get(queryTags.get(0)));
		queryTags.remove(0);
		for (String tag : queryTags) {
			if (!_docsByTag.containsKey(tag))
				return res;
			docs.retainAll(_docsByTag.get(tag));
		}
		for (String doc: docs)
			res.add(load(doc));
		return res;
	}

	@Override
	public Collection<String> getExistingTags() {
		return null;
	}


	@Override
	public Collection<String> getChildTags(String tag) {
		if (tag == null)
			tag = ROOT_TAG;
		return _tagTree.get(tag);
	}

}
