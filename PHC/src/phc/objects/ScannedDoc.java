package phc.objects;

import java.util.Collection;
import java.util.HashSet;

import android.graphics.Bitmap;

public class ScannedDoc {

	private String _name;
	private Bitmap _bitmap;
	private String _ocr;
	private Collection<String> _tags;
	
	public ScannedDoc(String name, Bitmap bitmap, Collection<String> tags, String ocr)
	{
		_name = name;
		_bitmap = bitmap;
		_tags = new HashSet<String>(tags);
		_ocr = ocr;
	}
	
	public String name()
	{
		return _name;
	}
	
	public Bitmap bitmap()
	{
		return _bitmap;
	}
	
	public String ocr()
	{
		return _ocr;
	}
	
	public Collection<String> tags()
	{
		return _tags;
	}
}
