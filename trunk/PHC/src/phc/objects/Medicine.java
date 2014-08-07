package phc.objects;

public class Medicine extends MedicalObject {
	public Medicine(String name, String docId, String startDate, int numDays, String amount)
	{
		super(name, docId);
		StartDate = startDate;
		NumDays = numDays;
		Amount = amount;
	}
	public String StartDate, Amount;
	public int NumDays;
}
