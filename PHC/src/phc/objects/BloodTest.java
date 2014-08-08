package phc.objects;

import phc.processing.BloodTestProcessor;

public class BloodTest extends MedicalObject {
	public BloodTest(String name, String docId, String value, String units, String date)
	{
		super(name, docId);
		Value = value;
		Units = units;
		Date = date;
	}
	public boolean isNormalValue()
	{
		if (Name.equals(BloodTestProcessor.HEMOGLOBIN)) {
			double v = Double.parseDouble(Value);
			return (v >= 12.0 && v <= 14.0);
		}
		return true;
	}
	public String toString() {
		return Name + " " + Value + " " + Units;
	}
	public String Value;
	public String Units;
	public String Date;
}
