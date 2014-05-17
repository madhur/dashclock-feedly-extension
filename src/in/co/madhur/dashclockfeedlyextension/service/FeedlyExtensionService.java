package in.co.madhur.dashclockfeedlyextension.service;

import java.util.Date;

import in.co.madhur.dashclockfeedlyextension.App;
import in.co.madhur.dashclockfeedlyextension.AppPreferences;
import in.co.madhur.dashclockfeedlyextension.R;
import in.co.madhur.dashclockfeedlyextension.api.Marker;
import in.co.madhur.dashclockfeedlyextension.api.Markers;
import in.co.madhur.dashclockfeedlyextension.db.DbHelper;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;

import android.app.Service;
import android.content.Intent;
import android.graphics.DashPathEffect;
import android.os.IBinder;
import android.util.Log;

public class FeedlyExtensionService extends DashClockExtension
{
	private AppPreferences appPreferences;

	@Override
	protected void onInitialize(boolean isReconnect)
	{
		super.onInitialize(isReconnect);
		appPreferences = new AppPreferences(this);
	}

	@Override
	protected void onUpdateData(int arg0)
	{

		if (!appPreferences.IsTokenPresent())
		{
			Log.d(App.TAG, "Auth token not present, returning");
			return;
		}

		StringFormatter formatter = new StringFormatter();
		DashClockData data = formatter.GetDashclockData(this);
		String expandedBody="";
		
		if (data == null)
			return;

		if(data.getLastUpdated()!=0)
		{
			Date date = new Date(data.getLastUpdated());
			java.text.DateFormat dateFormat = android.text.format.DateFormat.getTimeFormat(getBaseContext());
			expandedBody=String.format(getString(R.string.lastupdate_display_format), dateFormat.format(date));
		}

	

		try
		{
			publishUpdate(new ExtensionData().visible(true).status(String.valueOf(data.getStatus())).icon(R.drawable.ic_dashclock).expandedTitle(data.getTitle()).expandedBody(expandedBody));
		}
		catch (Exception e)
		{

			Log.e(App.TAG, "Exception while publishing:" + e.getMessage());
		}

	}

}
