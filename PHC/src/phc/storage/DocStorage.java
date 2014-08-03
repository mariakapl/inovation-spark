package phc.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import phc.interfaces.IDocStorage;
import phc.objects.DocResult;
import phc.objects.ScannedDoc;
import phc.processing.DocumentProcessor;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.phc.TagExtractor;

public class DocStorage implements IDocStorage {

	private static final String TAG_TREE_FILE = "tagTree.txt";
	private static final String TXT_EXTENSION = ".txt";
	private static final String BITMAP_EXTENSION = ".png";
	
	/* Storage is implemented as the following:
	 * 1. Images directory stores all images, with the name <id>.png
	 *    (<id> is a number, allocated when a document is added)
	 * 2. Tags directory stores all the tags of the documents. Each
	 *    document has a single file with the name <id>.txt, which contains
	 *    the tags of the document, one per line.
	 * 3. OCR directory stores the OCR of the documents. Each document
	 *    has a single file with the OCR output of that document.
	 * 4. Names directory stores the names of the documents (docId.txt)
	 * 5. tagTree.txt stores the hierarchy of tags. Each line specifies
	 *    the parent tag, followed by the child tags, tab-separated.
	 */
	private Context _context;
	private File _storageDir;
	private File _namesDir;
	private File _imagesDir;
	private File _tagsDir;
	private File _ocrDir;
	private File _tagTreeFile;

	private HashMap<String, HashSet<String>> _docsByTag;	// tag -> doc ids
	private HashMap<String, HashSet<String>> _tagsByDoc;	// doc id -> tags
	private HashMap<String, DocResult> _docsById;		// doc id -> doc
	private HashMap<String, List<String>> _tagTree;
	
	private static IDocStorage _instance;
	public static IDocStorage get() {
		return _instance;
	}
	public static IDocStorage create(Context context) {
		return (_instance = new DocStorage(context));
	}
	
	//remove all the files :(
	@Override
	public void clear() {
		
		if (_tagTreeFile.exists())
			_tagTreeFile.delete();
		
		if (_namesDir.exists() && _namesDir.isDirectory()) {
			String[] children = _namesDir.list();
	        for (int i = 0; i < children.length; i++) {
	            new File(_namesDir, children[i]).delete();
	        }
		}
		
		if (_imagesDir.exists() && _imagesDir.isDirectory()) {
			String[] children = _imagesDir.list();
	        for (int i = 0; i < children.length; i++) {
	            new File(_imagesDir, children[i]).delete();
	        }
		}
		
		if (_tagsDir.exists() && _tagsDir.isDirectory()) {
			String[] children = _tagsDir.list();
	        for (int i = 0; i < children.length; i++) {
	            new File(_tagsDir, children[i]).delete();
	        }
		}
		
		if (_ocrDir.exists() && _ocrDir.isDirectory()) {
			String[] children = _ocrDir.list();
	        for (int i = 0; i < children.length; i++) {
	            new File(_ocrDir, children[i]).delete();
	        }
		}
	}
	
	private DocStorage(Context context)
	{
		_context = context;
		
		_storageDir = context.getFilesDir();
		_namesDir = context.getDir("Names", Context.MODE_PRIVATE);
		_imagesDir = context.getDir("Images", Context.MODE_PRIVATE);
		_tagsDir = context.getDir("Tags", Context.MODE_PRIVATE);
		_ocrDir = context.getDir("OCR", Context.MODE_PRIVATE);
		
		_docsByTag = new HashMap<String, HashSet<String>>();
		_tagsByDoc = new HashMap<String, HashSet<String>>();
		_docsById = new HashMap<String, DocResult>();
		
		//RemoveAll(_storageDir);
		_tagTreeFile = new File(_storageDir, TAG_TREE_FILE);
		_tagTree = new HashMap<String, List<String>>();
		if (_tagTreeFile.exists())
			loadTagTree();
		else
			_tagTree = TagExtractor.builtInTagTree();
		
		load();
	}
		
	private void loadTagTree() {
		List<String> lines = readTextFile(_tagTreeFile);
		for (String line : lines) {
			String [] parts = line.split("\t");
			String parent = parts[0];
			List<String> children = new ArrayList<String>(
				Arrays.asList(parts).subList(1, parts.length));
			_tagTree.put(parent, children);
		}
	}
		
	public File getImageDir()
	{
		return _imagesDir;
	}
	
	public boolean writeTextFile(File file, String s)
	{
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		    writer.write (s);
			writer.close();
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
		
		for (String id : docIds) {
			List<String> tags = readTextFile(new File(_tagsDir, id + TXT_EXTENSION));
			_tagsByDoc.put(id, new HashSet<String>(tags));
			for (String tag : tags)
				addDocToTag(tag, id);
		}		
	}

	private DocResult load(String id) {
		if (!_docsById.containsKey(id)) {
			File nameFile = new File(_namesDir, id + TXT_EXTENSION);
			String name = readTextFileAsString(nameFile);
			File image = new File(_imagesDir, id + BITMAP_EXTENSION);
			Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
			File ocrFile = new File(_ocrDir, id + TXT_EXTENSION);
			String ocr = readTextFileAsString(ocrFile);
			File tagFile = new File(_tagsDir, id + TXT_EXTENSION);
			List<String> tags = readTextFile(tagFile);
			
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date date = new Date(image.lastModified());     
			String reportDate = df.format(date);
			
			_docsById.put(id, new DocResult(
				new ScannedDoc(name, bitmap, tags, ocr), id, reportDate));
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
		if (doc.name() != null) {
			File name = new File(_namesDir, id + TXT_EXTENSION);
			writeTextFile(name, doc.name());
		}
		if (doc.ocr() != null) {
			File ocr = new File(_ocrDir, id + TXT_EXTENSION);
			writeTextFile(ocr, doc.ocr());
		}
		if (doc.tags() != null) {
			StringBuilder sb = new StringBuilder();
			for (String tag : doc.tags()) {
				if (sb.length() > 0)
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
			sb.append(line + separator);
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
		DocResult res = load(id);
		DocumentProcessor.instance().process(_context, res);
		return res;
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
		HashSet<String> docs_first_tag= _docsByTag.get(queryTags.get(0));
		
		if(docs_first_tag == null)
			return res;
		
		HashSet<String> docs = new HashSet<String>(docs_first_tag);
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
		List<String> tags = new ArrayList<String>();
		for (String tag : _tagTree.get(TagExtractor.ROOT_TAG)) {
			tags.add(tag);
			if (_tagTree.containsKey(tag))
				tags.addAll(_tagTree.get(tag));
		}
		return tags;
	}


	@Override
	public Collection<String> getChildTags(String tag) {
		if (tag == null)
			tag = TagExtractor.ROOT_TAG;
		return _tagTree.get(tag);
	}
	
	@Override
	public boolean createTag(String name, String parent) {
		if (parent == null)
			parent = TagExtractor.ROOT_TAG;
		else if (! _tagTree.get(TagExtractor.ROOT_TAG).contains(parent)) {
			System.out.println("createTag - trying to add tag " + name + " under parent " + parent + ", but parent does not exist");
			return false;
		}
		if (! _tagTree.containsKey(parent))
			_tagTree.put(parent, new ArrayList<String>());
		List<String> children = _tagTree.get(parent);
		if (children.contains(name)) {
			System.out.println("createTag - trying to create top-level tag " + name + " which already exists");
			return true;
		}
		children.add(name);
		// rewrite the tag tree file
		StringBuilder sb = new StringBuilder();
		for (String topLevel : _tagTree.keySet()) {
			List<String> childList = _tagTree.get(topLevel);
			sb.append(topLevel + "\t" + join(childList, "\t") + "\n");
		}
		writeTextFile(_tagTreeFile, sb.toString());
		return true;
	}
}
