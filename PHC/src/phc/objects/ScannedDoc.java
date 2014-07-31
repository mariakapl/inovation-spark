package phc.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class ScannedDoc implements Parcelable {

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
	
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(_name);
        out.writeParcelable(_bitmap, 0);
        out.writeString(_ocr);
        out.writeInt(_tags.size());
        for (String tag : _tags)
        	out.writeString(tag);
    }
    
    public static final Parcelable.Creator<ScannedDoc> CREATOR
            = new Parcelable.Creator<ScannedDoc>() {
        public ScannedDoc createFromParcel(Parcel in) {
            return new ScannedDoc(in);
        }

        public ScannedDoc[] newArray(int size) {
            return new ScannedDoc[size];
        }
    };
    
    private ScannedDoc(Parcel in) {
    	_name = in.readString();
    	_bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    	_ocr = in.readString();
    	int nTags = in.readInt();
    	_tags = new HashSet<String>();
    	for (int i = 0; i < nTags; i++)
    		_tags.add(in.readString());
    }

	@Override
	public int describeContents() {
		return 0;
	}

}
