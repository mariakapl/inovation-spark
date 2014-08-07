package phc.objects;

public class BloodTest extends MedicalObject {
	public BloodTest(String name, String docId, String value, String units, String date)
	{
		super(name, docId);
		Value = value;
		Units = units;
		Date = date;
	}
	public String Value;
	public String Units;
	public String Date;
}
