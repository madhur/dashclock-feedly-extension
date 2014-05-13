package in.co.madhur.dashclockfeedlyextension.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import in.co.madhur.dashclockfeedlyextension.App;
import in.co.madhur.dashclockfeedlyextension.AppPreferences;
import in.co.madhur.dashclockfeedlyextension.AppPreferences.Keys;
import in.co.madhur.dashclockfeedlyextension.R;
import in.co.madhur.dashclockfeedlyextension.api.Feedly;
import in.co.madhur.dashclockfeedlyextension.api.Marker;
import in.co.madhur.dashclockfeedlyextension.api.Markers;
import in.co.madhur.dashclockfeedlyextension.db.DbHelper;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.infospace.android.oauth2.WebApiHelper;

import android.content.Intent;
import android.os.Environment;
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
			Log.e(App.TAG, "Exception while getting marker count");
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
			Log.e(App.TAG, "Exception writing to database");
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

		ArrayList<String> selectedValues = appPreferences.GetSelectedValuesNotifications();
		HashMap<String, Integer> seek_states = appPreferences.GetSeekValues();
		ArrayList<String> notiText = new ArrayList<String>();
		int totalUnread = 0;

		if (selectedValues.size() == 0)
		{
			Log.d(App.TAG, "No feeds selected for notification");
			return;
		}

		for (String selValue : selectedValues)
		{
			for (Marker marker : markers.getUnreadcounts())
			{

				if (selValue.equalsIgnoreCase(marker.getId()))
				{
					Log.d(App.TAG, marker.getCount() + " feeds for "
							+ marker.getId());

					if (marker.getCount() == 0)
						continue;

					if (!seek_states.containsKey(marker.getId()))
					{
						Log.e(App.TAG, "Seek state not found for id "
								+ marker.getId());
						continue;
					}

					if (marker.getCount() > seek_states.get(marker.getId()))
					{
						totalUnread = totalUnread + marker.getCount();
						notiText.add(String.format(getString(R.string.noti_inbox_text), marker.getCount(), dbHelper.GetTitleFromSuborCategoryId(marker.getId())));

					}

				}
			}
		}

		String contentText = String.format(getString(R.string.noti_content_text), totalUnread);

		if (totalUnread != 0 && notiText.size() > 0)
		{
			Log.d(App.TAG, "Firing notification");

			NotificationCompat.Builder builder = notifications.GetNotificationBuilder(contentText, totalUnread);
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
			{
				builder=notifications.GetExpandedBuilder(builder, notiText, contentText);
			}
			
			
			notifications.FireNotification(0, builder);

		}

	}

}
