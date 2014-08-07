package phc.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;

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

	private static void copy(AssetManager am, File src, File dst) throws IOException {
	    InputStream in = am.open(src.getPath());
	    OutputStream out = new FileOutputStream(dst);

	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
	    }
	    in.close();
	    out.close();
	}

	public static void copyAllAssets(Context context, String fromAssetDir, File toDir) {
		AssetManager am = context.getAssets();
	    String [] list;
	    try {
	        list = am.list(fromAssetDir);
	        if (list.length > 0) {
	            // This is a folder
	            for (String file : list) {
	            	File assetFile = new File(fromAssetDir, file);
	            	File outFile = new File(toDir, file);
	            	copy(am, assetFile, outFile);
	            }
	        }
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
	}
	
}
