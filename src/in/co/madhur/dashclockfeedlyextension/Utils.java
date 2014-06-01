package in.co.madhur.dashclockfeedlyextension;

import java.util.Date;

import android.content.Context;

public final class Utils
{
	
	public static String GetFormattedDate(long date, Context context)
	{
		
		Date dateObj = new Date(date);
		java.text.DateFormat dateFormat = android.text.format.DateFormat.getTimeFormat(context);
		return dateFormat.format(dateObj);
	}
	
}
