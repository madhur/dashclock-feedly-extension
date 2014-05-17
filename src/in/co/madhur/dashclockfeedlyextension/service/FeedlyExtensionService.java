package in.co.madhur.dashclockfeedlyextension.service;

import java.util.Date;

import in.co.madhur.dashclockfeedlyextension.App;
import in.co.madhur.dashclockfeedlyextension.AppPreferences;
import in.co.madhur.dashclockfeedlyextension.Consts;
import in.co.madhur.dashclockfeedlyextension.R;
import in.co.madhur.dashclockfeedlyextension.api.Marker;
import in.co.madhur.dashclockfeedlyextension.api.Markers;
import in.co.madhur.dashclockfeedlyextension.db.DbHelper;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.DashPathEffect;
import android.os.IBinder;
import android.util.Log;

public class FeedlyExtensionService extends DashClockExtension
{
	private AppPreferences appPreferences;
	private FeedChangeReceiver changeReceiver;

	@Override
	protected void onInitialize(boolean isReconnect)
	{
		super.onInitialize(isReconnect);
		appPreferences = new AppPreferences(this);
		if (changeReceiver != null)
		{
			unregisterReceiver(changeReceiver);

		}

		IntentFilter filter = new IntentFilter(Consts.UPDATE_ACTION);
		changeReceiver = new FeedChangeReceiver();
		registerReceiver(changeReceiver, filter);
	}

	@Override
	protected void onUpdateData(int arg0)
	{

		if (!appPreferences.IsTokenPresent())
		{
			Log.d(App.TAG, "Auth token not present, returning");
			UpdateData(null, getString(R.string.login_required), getString(R.string.login_required_desc));
			return;
		}

		StringFormatter formatter = new StringFormatter();
		DashClockData data = formatter.GetDashclockData(this);
		String expandedBody = "";

		if (data == null)
			return;

		if (data.getLastUpdated() != 0)
		{
			Date date = new Date(data.getLastUpdated());
			java.text.DateFormat dateFormat = android.text.format.DateFormat.getTimeFormat(getBaseContext());
			expandedBody = String.format(getString(R.string.lastupdate_display_format), dateFormat.format(date));
		}

		Log.d(App.TAG, data.getTitle());

		UpdateData(String.valueOf(data.getStatus()), data.getTitle(), expandedBody);

	}

	private void UpdateData(String status, String title, String body)
	{
		try
		{
			publishUpdate(new ExtensionData().visible(true).status(status).icon(R.drawable.ic_dashclock).expandedTitle(title).expandedBody(body));
		}
		catch (Exception e)
		{

			Log.e(App.TAG, "Exception while publishing:" + e.getMessage());
		}

	}

	private class FeedChangeReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			Log.v(App.TAG, "FeedChangeReceiver");
			onUpdateData(DashClockExtension.UPDATE_REASON_CONTENT_CHANGED);
		}
	}

}
