package phc.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utils {
	public static final String DateFormat = "MM/dd/yyyy HH:mm:ss";
	public static final String DateFormatShort = "MM/yyyy";

	public static String join(List<String> lines, String separator) {
		StringBuilder sb = new StringBuilder();
		for (String line : lines)
			sb.append(line + separator);
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}	

	public static String readTextFileAsString(File file)
	{
		List<String> lines = readTextFile(file);
		return join(lines, "\n") + "\n";
	}

	public static List<String> readTextFile(File file)
	{
		List<String> lines = new ArrayList<String>();
		try {
		    BufferedReader br = new BufferedReader(new FileReader(file));
		    String line;
		    while ((line = br.readLine()) != null)
		    	lines.add(line);
		    br.close();
		}
		catch (IOException e) {
			return null;
		}
		return lines;
	}
	
	public static boolean writeTextFile(File file, String s)
	{
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		    writer.write(s);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static String getDateStringShort(Date date)
	{
		SimpleDateFormat df = new SimpleDateFormat(DateFormatShort);
		return df.format(date);
	}
	public static String getDateString(Date date)
	{
		SimpleDateFormat df = new SimpleDateFormat(DateFormat);
		return df.format(date);
	}
	
	public static Date getDateFromString(String s)
	{
		SimpleDateFormat df = new SimpleDateFormat(DateFormat);
		try {
			return df.parse(s);
		} catch (ParseException e) {
			return null;
		}
	}
	
}
