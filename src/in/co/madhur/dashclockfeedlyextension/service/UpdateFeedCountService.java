package in.co.madhur.dashclockfeedlyextension.service;

import java.util.ArrayList;

import in.co.madhur.dashclockfeedlyextension.App;
import in.co.madhur.dashclockfeedlyextension.AppPreferences;
import in.co.madhur.dashclockfeedlyextension.AppPreferences.Keys;
import in.co.madhur.dashclockfeedlyextension.api.Feedly;
import in.co.madhur.dashclockfeedlyextension.api.Marker;
import in.co.madhur.dashclockfeedlyextension.api.Markers;
import in.co.madhur.dashclockfeedlyextension.db.DbHelper;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.infospace.android.oauth2.WebApiHelper;

import android.content.Intent;
import android.util.Log;

public class UpdateFeedCountService extends WakefulIntentService
{
	private WebApiHelper apiHelper;
	private DbHelper dbHelper;
	private AppPreferences appPreferences;
	private Markers markers;

	public UpdateFeedCountService(String name)
	{
		super(name);
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
			WebApiHelper.register(this);
			dbHelper = DbHelper.getInstance(this);
			apiHelper = WebApiHelper.getInstance();

			if (apiHelper.shouldRefreshAccesToken())
			{
				apiHelper.refreshAccessTokenIfNeeded();
			}

			String token = appPreferences.GetToken();

			Feedly feedly = Feedly.getInstance(token);

			try
			{
				markers = feedly.GetUnreadCounts();
			}
			catch (Exception e)
			{
				Log.e(App.TAG, "Exception while getting marker count");
				return;

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
			}

			if (appPreferences.GetBoolPreferences(Keys.ENABLE_NOTIFICATIONS))
			{
				SendNotifications();

			}

		}

	}

	private void SendNotifications()
	{
		ArrayList<String> selectedValues = appPreferences.GetSelectedValuesNotifications();
		
		if(selectedValues.size()==0)
		{
			Log.d(App.TAG, "No feeds selected for notification");
			return;
		}
		
		for(String selValue: selectedValues)
		{
			for(Marker marker: markers.getUnreadcounts())
			{
				
				if(selValue.equalsIgnoreCase(marker.getId()))
				{
					Log.d(App.TAG, marker.getCount() + " feeds for " + marker.getId());
					
					
				}
			}
		}
		

	}

}
