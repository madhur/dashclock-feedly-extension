package in.co.madhur.dashclockfeedlyextension;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class AppPreferences
{
	private Context context;
	private SharedPreferences sharedPreferences;
	
	public AppPreferences(Context context)
	{
		
		this.context=context;
		this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public enum Keys
	{
		
		
		
		
		
		
	}
	
	public boolean IsTokenPresent()
	{
		
		String accessToken=sharedPreferences.getString(context.getString(R.string.feedly_api_access_token), "");
		if(TextUtils.isEmpty(accessToken))
			return false;
		
		return true;
		
		
	}
	
	public String GetToken()
	{
		String accessToken=sharedPreferences.getString(context.getString(R.string.feedly_api_access_token), "");
		return accessToken;
		
	}
	
	

}
