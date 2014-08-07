package phc.objects;

public class Medicine {
	public Medicine(String name, String startDate, String endDate, String amount, String units)
	{
		Name = name;
		StartDate = startDate;
		EndDate = endDate;
		Amount = amount;
		Units = units;
	}
	public String Name, StartDate, EndDate, Amount, Units;
}
