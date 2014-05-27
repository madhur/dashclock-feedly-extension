package in.co.madhur.dashclockfeedlyextension.service;

import com.google.android.apps.dashclock.configuration.AppChooserPreference;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;

public class LaunchService extends Service
{

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		super.onStartCommand(intent, flags, startId);
		
		Bundle data=intent.getExtras();
		String key=data.getString("key");
		
		Intent launchIntent;
		if(!TextUtils.isEmpty(key))
		{
			
			launchIntent=AppChooserPreference.getIntentValue(key, null);
			startActivity(launchIntent);
		}
		
		stopSelf();
		
		return START_NOT_STICKY;
	}

}
