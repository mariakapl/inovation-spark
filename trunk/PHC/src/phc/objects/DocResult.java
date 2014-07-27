package phc.objects;

import java.util.Calendar;

public class DocResult {

	private ScannedDoc _scannedDoc;
	private String _id;
	private String _date;
	private boolean _shared; //indicate if the document is shared
	
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
		return _date.toString();
	}

}
