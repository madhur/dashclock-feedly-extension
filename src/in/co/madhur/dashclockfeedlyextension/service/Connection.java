package in.co.madhur.dashclockfeedlyextension.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connection
{

	public static ConnectivityManager getConnectivityManager(Context context)
	{
		return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
	}
	
	public static boolean isConnected(Context context) 
	{
		
		NetworkInfo networkInfo= getConnectivityManager(context).getActiveNetworkInfo();
		if(networkInfo != null && networkInfo.isConnected())
			return true;
		
		return false;
	}
	
}


