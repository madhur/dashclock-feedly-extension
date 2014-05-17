package in.co.madhur.dashclockfeedlyextension.service;

import in.co.madhur.dashclockfeedlyextension.App;
import in.co.madhur.dashclockfeedlyextension.AppPreferences;
import in.co.madhur.dashclockfeedlyextension.AppPreferences.Keys;
import in.co.madhur.dashclockfeedlyextension.api.Feedly;
import in.co.madhur.dashclockfeedlyextension.api.Markers;
import in.co.madhur.dashclockfeedlyextension.db.DbHelper;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.infospace.android.oauth2.WebApiHelper;

import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class UpdateFeedCountService extends WakefulIntentService
{
	private WebApiHelper apiHelper;
	private DbHelper dbHelper;
	private AppPreferences appPreferences;
	private Markers markers;

	public UpdateFeedCountService()
	{
		super("UpdateFeedCountService");
	}

	public UpdateFeedCountService(String name)
	{
		super("UpdateFeedCountService");
	}

	@Override
	protected void doWakefulWork(Intent arg0)
	{
		Log.v(App.TAG, "do WakefulWork");
		
		appPreferences = new AppPreferences(this);
		if (!appPreferences.IsTokenPresent())
		{
			Log.d(App.TAG, "Auth token not present, returning");
			return;
		}
		else
		{
			// Check if network disconnected, else return
			if (!Connection.isConnected(this))
				return;

			RefreshTokenIfRequired();

			// if(!CheckLastSync())
			// {
			//
			// Log.d(App.TAG, "Successful sync within time interval. aborting");
			// return;
			// }

			if (!GetUnreadCountsAndSave())
				return;

			appPreferences.SaveSuccessfulSync();

			if (appPreferences.GetBoolPreferences(Keys.ENABLE_NOTIFICATIONS))
			{
				PrepareAndSendNotifications();
			}

		}

	}

	// Return false if unsucessful, true if successful
	private boolean GetUnreadCountsAndSave()
	{
		String token = appPreferences.GetToken();

		Feedly feedly = Feedly.getInstance(token);

		try
		{
			markers = feedly.GetUnreadCounts();
		}
		catch (Exception e)
		{
			Log.e(App.TAG, e.getMessage());
			return false;

		}

		try
		{
			dbHelper.TruncateMarkers();
			dbHelper.WriteMarkers(markers);
		}
		catch (Exception e)
		{
			Log.e(App.TAG, e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;

	}

	private boolean CheckLastSync()
	{
		long lastSync = appPreferences.GetLastSuccessfulSync();

		// First time sync, Just return true
		if (lastSync == 0)
			return true;

		// Else see if we are not doing too much sync within the sync interval
		int syncIntervalHour = appPreferences.GetSyncInterval();
		long syncIntervalMillisec = syncIntervalHour * 60 * 60 * 1000;
		if (System.currentTimeMillis() - lastSync > syncIntervalMillisec)
			return true;

		return false;
	}

	private void RefreshTokenIfRequired()
	{
		WebApiHelper.register(this);
		dbHelper = DbHelper.getInstance(this);
		apiHelper = WebApiHelper.getInstance();

		if (apiHelper.shouldRefreshAccesToken())
		{
			apiHelper.refreshAccessTokenIfNeeded();
		}
	}

	private void PrepareAndSendNotifications()
	{
		Notifications notifications = new Notifications(this);
		StringFormatter formatter=new StringFormatter();
		NotificationData data=formatter.GetNotificationData(this);
		
		if(data==null)
			return;

		if (data.getStatus() != 0 && data.getExpandedBody().size() > 0)
		{
			Log.d(App.TAG, "Firing notification");

			NotificationCompat.Builder builder = notifications.GetNotificationBuilder(data.getTitle(), data.getStatus());
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
			{
				builder = notifications.GetExpandedBuilder(builder, data.getExpandedBody(), data.getTitle());
			}

			notifications.FireNotification(0, builder);

		}

	}

}
