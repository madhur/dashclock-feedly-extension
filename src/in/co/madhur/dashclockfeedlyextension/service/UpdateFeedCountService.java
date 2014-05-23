package in.co.madhur.dashclockfeedlyextension.service;

import in.co.madhur.dashclockfeedlyextension.App;
import in.co.madhur.dashclockfeedlyextension.AppPreferences;
import in.co.madhur.dashclockfeedlyextension.AppPreferences.Keys;
import in.co.madhur.dashclockfeedlyextension.Consts;
import in.co.madhur.dashclockfeedlyextension.Consts.UPDATESOURCE;
import in.co.madhur.dashclockfeedlyextension.api.Feedly;
import in.co.madhur.dashclockfeedlyextension.api.Markers;
import in.co.madhur.dashclockfeedlyextension.db.DbHelper;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.infospace.android.oauth2.WebApiHelper;

import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

public class UpdateFeedCountService extends WakefulIntentService
{
	private WebApiHelper apiHelper;
	private DbHelper dbHelper;
	private AppPreferences appPreferences;
	private Markers markers;
	private final int NOTIFICATION_ID=0;

	public UpdateFeedCountService()
	{
		super("UpdateFeedCountService");
	}

	public UpdateFeedCountService(String name)
	{
		super("UpdateFeedCountService");
	}

	@Override
	protected void doWakefulWork(Intent intent)
	{

		String source = intent.getStringExtra(Consts.UPDATE_SOURCE);
		if (!TextUtils.isEmpty(source))
		{
			Log.d(App.TAG, "Starting update because of " + source);
		}

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

			// Sync is always performed on accept/logout button irrespective of scheduled
			// Regular sync and network change sync are constrained by sync schedule
			if (source.equalsIgnoreCase(UPDATESOURCE.ALARM.key)
					|| source.equalsIgnoreCase(UPDATESOURCE.NETWORK_CHANGE.key))
			{
				if (!CheckLastSync())
				{
					Log.d(App.TAG, "Successful sync within time interval. aborting");
					return;
				}
			}

			if (!GetUnreadCountsAndSave())
				return;

			appPreferences.SaveSuccessfulSync();

			// Update Dashclock and widgets
			SendUpdateBroadcast();

			if (appPreferences.GetBoolPreferences(Keys.ENABLE_NOTIFICATIONS))
			{
				PrepareAndSendNotifications();
			}

		}

	}

	private void SendUpdateBroadcast()
	{
		// Send update broadcast for widgets
		Intent updateIntent = new Intent();
		updateIntent.setAction(Consts.UPDATE_ACTION);
		updateIntent.addCategory(Consts.CATEGORY_WIDGET);
		sendBroadcast(updateIntent);

		// Send update broadcast for dashclock widget
		updateIntent = new Intent();
		updateIntent.setAction(Consts.UPDATE_ACTION);
		updateIntent.addCategory(Consts.CATEGORY_DASHCLOCK);
		LocalBroadcastManager.getInstance(this).sendBroadcast(updateIntent);

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
			e.printStackTrace();
			return false;

		}

		try
		{
			dbHelper.TruncateMarkers();
			dbHelper.WriteMarkers(markers);
		}
		catch (Exception e)
		{
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
		StringFormatter formatter = new StringFormatter();
		NotificationData data = formatter.GetNotificationData(this);

		if (data == null)
			return;

		if (data.getStatus() != 0 && data.getExpandedBody().size() > 0)
		{
			NotificationCompat.Builder builder = notifications.GetNotificationBuilder(data.getTitle(), data.getStatus());
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
			{
				builder = notifications.GetExpandedBuilder(builder, data.getExpandedBody(), data.getTitle());
			}

			notifications.FireNotification(NOTIFICATION_ID, builder, appPreferences.getBoolMetadata(Keys.ENABLE_VIBRATE), appPreferences.getBoolMetadata(Keys.ENABLE_SOUND), appPreferences.getBoolMetadata(Keys.ENABLE_LED));
		}
		else
			notifications.Cancel(NOTIFICATION_ID);

	}

}
