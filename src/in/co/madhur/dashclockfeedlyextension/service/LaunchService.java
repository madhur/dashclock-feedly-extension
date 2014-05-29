package in.co.madhur.dashclockfeedlyextension.service;

import in.co.madhur.dashclockfeedlyextension.App;
import in.co.madhur.dashclockfeedlyextension.AppPreferences.Keys;

import com.crittercism.app.Crittercism;
import com.google.android.apps.dashclock.configuration.AppChooserPreference;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

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

		Intent launchIntent;
		launchIntent = AppChooserPreference.getIntentValue(Keys.NOTIFICATION_CLICK_INTENT.key, null);
		if (launchIntent != null)
		{
			Log.v(App.TAG, launchIntent.getAction());
			launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			try
			{
				startActivity(launchIntent);
			}
			catch (Exception e)
			{
				Log.e(App.TAG, e.getMessage());
				Crittercism.logHandledException(e);
			}
		}

		stopSelf();

		return START_NOT_STICKY;
	}

}
