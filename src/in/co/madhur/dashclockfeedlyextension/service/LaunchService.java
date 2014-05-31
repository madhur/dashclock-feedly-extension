package in.co.madhur.dashclockfeedlyextension.service;

import in.co.madhur.dashclockfeedlyextension.App;
import in.co.madhur.dashclockfeedlyextension.AppPreferences;
import com.crittercism.app.Crittercism;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
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
		launchIntent = new AppPreferences(this).GetNotificationIntent();
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
