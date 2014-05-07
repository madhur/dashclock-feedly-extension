package in.co.madhur.dashclockfeedlyextension;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
		SELECTED_VALUES("selected_values");
		
		public final String key;

		private Keys(String key)
		{
			this.key = key;

		}
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

	public void SaveSelectedValues(HashMap<String, Boolean> check_states)
	{
		StringBuilder sb=new StringBuilder();
		for(String Id : check_states.keySet())
		{
			if(check_states.get(Id))
			{
				sb.append(Id);
				sb.append(';');
			}
		}
		
		Editor edit=sharedPreferences.edit();
		edit.putString(Keys.SELECTED_VALUES.key, sb.toString());
		edit.commit();
		
	}
	
	public ArrayList<String> GetSelectedValues()
	{
		ArrayList<String> values=new ArrayList<String>();
		
		String tokenValues=sharedPreferences.getString(Keys.SELECTED_VALUES.key, "");
		if(tokenValues.equalsIgnoreCase(""))
			return values;
		
		String[] splitValues=tokenValues.split(";");
		for(String s: splitValues)
			values.add(s);
		
		return values;
		
		
	}
	
	

}
