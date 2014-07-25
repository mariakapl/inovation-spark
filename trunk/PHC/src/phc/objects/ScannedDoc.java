package phc.objects;

import java.util.Collection;
import java.util.HashSet;

import android.graphics.Bitmap;

public class ScannedDoc {

	private Bitmap _bitmap;
	private String _ocr;
	private Collection<String> _tags;
	
	public ScannedDoc(Bitmap bitmap, Collection<String> tags, String ocr)
	{
		_bitmap = bitmap;
		_tags = new HashSet<String>(tags);
		_ocr = ocr;
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
