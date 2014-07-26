package phc.objects;

import java.util.Calendar;

public class DocResult {

	private ScannedDoc scannedDoc;
	private int id;
	private String name;
	private Calendar date;
	private boolean shared; //indicate if the document is shared
	
	public DocResult(ScannedDoc doc, int new_id, String new_name)
	{
		scannedDoc = doc;
		id = new_id;
		name = new_name;
	}
	public ScannedDoc scannedDoc() {
		return scannedDoc;
	}
	public int id() {
		return id;
	}
	
	public String name() {
		return name;
	}
	
	public String getDate(){
		
		return date.toString();
	}

}
