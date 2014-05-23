package in.co.madhur.dashclockfeedlyextension.ui;

import com.google.android.apps.dashclock.configuration.AppChooserPreference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

public class LauncherActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Intent intent=getIntent();
		Bundle data=intent.getExtras();
		String key=data.getString("key");
		
		Intent launchIntent;
		if(!TextUtils.isEmpty(key))
		{
			
			launchIntent=AppChooserPreference.getIntentValue(key, null);
			startActivity(launchIntent);
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	
		
	}
	
	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		
		Bundle data=intent.getExtras();
		String key=data.getString("key");
		
		Intent launchIntent;
		if(!TextUtils.isEmpty(key))
		{
			
			launchIntent=AppChooserPreference.getIntentValue(key, null);
			startActivity(launchIntent);
		}
	}
	
	
}
