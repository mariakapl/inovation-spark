package phc.objects;

public class DocResult {

	private ScannedDoc _scannedDoc;
	private int _id;
	
	public DocResult(ScannedDoc doc, int id)
	{
		_scannedDoc = doc;
		_id = id;
	}
	public ScannedDoc scannedDoc() {
		return _scannedDoc;
	}
	public int id() {
		return _id;
	}

}
