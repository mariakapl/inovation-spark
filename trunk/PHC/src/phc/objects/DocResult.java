package phc.objects;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class DocResult implements Parcelable {

	private ScannedDoc _scannedDoc;
	private String _id;
	private String _date;
	private boolean _shared; //indicate if the document is shared
	private List<BloodTest> _bloodTests = new ArrayList<BloodTest>();
	private List<Medicine> _medicine = new ArrayList<Medicine>();
	
	public DocResult(ScannedDoc doc, String id, String date)
	{
		_scannedDoc = doc;
		_id = id;
		_date = date;
	}
	public ScannedDoc scannedDoc() {
		return _scannedDoc;
	}
	public String id() {
		return _id;
	}
	public String date() {
		return _date;
	}
	
	public void addBloodTest(BloodTest test)
	{
		_bloodTests.add(test);
	}
	
	public void addMedicine(Medicine medicine)
	{
		_medicine.add(medicine);
	}
	
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(_scannedDoc, 0);
        out.writeString(_id);
        out.writeString(_date);
    	out.writeInt(_shared ? 1 : 0);
    }
    public List<BloodTest> bloodTests() {
    	return _bloodTests;
    }
    public List<Medicine> medicine() {
    	return _medicine;
    }
    public static final Parcelable.Creator<DocResult> CREATOR
            = new Parcelable.Creator<DocResult>() {
        public DocResult createFromParcel(Parcel in) {
            return new DocResult(in);
        }

        public DocResult[] newArray(int size) {
            return new DocResult[size];
        }
    };
    
    private DocResult(Parcel in) {
    	_scannedDoc = in.readParcelable(ScannedDoc.class.getClassLoader());
    	_id = in.readString();
    	_date = in.readString();
    	_shared = (in.readInt() == 1);
    }

    @Override
	public int describeContents() {
		return 0;
	}
}
