package in.co.madhur.dashclockfeedlyextension.service;

import java.util.Date;

import in.co.madhur.dashclockfeedlyextension.App;
import in.co.madhur.dashclockfeedlyextension.AppPreferences;
import in.co.madhur.dashclockfeedlyextension.Consts;
import in.co.madhur.dashclockfeedlyextension.R;
import in.co.madhur.dashclockfeedlyextension.Utils;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

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
			LocalBroadcastManager.getInstance(this).unregisterReceiver(changeReceiver);
		}

		IntentFilter filter = new IntentFilter(Consts.UPDATE_ACTION);
		filter.addCategory(Consts.CATEGORY_DASHCLOCK);

		changeReceiver = new FeedChangeReceiver();
		LocalBroadcastManager.getInstance(this).registerReceiver(changeReceiver, filter);
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

		StringFormatter formatter = new StringFormatter(this);
		ResultData data = formatter.GetResultData(this);
		String expandedBody = "";

		// If there are no feeds selected or unread count is zero, we dismiss
		// the dashclock widget
		if (data == null || data.getUnreadCount() == 0)
		{
			publishUpdate(null);
			return;
		}

		if (data.getLastUpdated() != 0)
		{

			expandedBody = String.format(getString(R.string.lastupdate_display_format), Utils.GetFormattedDate(data.getLastUpdated(), this));
		}

		Log.d(App.TAG, "Dashclock" + expandedBody);

		UpdateData(String.valueOf(data.getUnreadCount()), data.getTitle(), expandedBody, appPreferences.GetWidgetIntent());

	}

	private void UpdateData(String status, String title, String body, Intent targetIntent)
	{
		try
		{
			publishUpdate(new ExtensionData().visible(true).status(status).icon(R.drawable.ic_dashclock).expandedTitle(title).expandedBody(body).clickIntent(targetIntent));
		}
		catch (Exception e)
		{

			Log.e(App.TAG, "Exception while publishing:" + e.getMessage());
		}

	}

	private void UpdateData(String status, String title, String body)
	{
		UpdateData(status, title, body, null);
	}

	private class FeedChangeReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			Toast.makeText(context, "on recieve dashclock "
					+ intent.getAction(), Toast.LENGTH_SHORT).show();
			onUpdateData(DashClockExtension.UPDATE_REASON_CONTENT_CHANGED);
		}
	}

}
