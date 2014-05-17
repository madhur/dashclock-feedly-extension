package in.co.madhur.dashclockfeedlyextension.service;

import java.util.ArrayList;
import java.util.HashMap;

import in.co.madhur.dashclockfeedlyextension.App;
import in.co.madhur.dashclockfeedlyextension.AppPreferences;
import in.co.madhur.dashclockfeedlyextension.R;
import in.co.madhur.dashclockfeedlyextension.api.Marker;
import in.co.madhur.dashclockfeedlyextension.api.Markers;
import in.co.madhur.dashclockfeedlyextension.db.DbHelper;
import android.content.Context;
import android.util.Log;

public class StringFormatter
{
	public DashClockData GetDashclockData(Context context)
	{
		AppPreferences appPreferences=new AppPreferences(context);
		long lastSync=appPreferences.GetLastSuccessfulSync();
		NotificationData data=GetNotificationData(context, appPreferences );
		
		return new DashClockData(data, lastSync);
		
	}
	
	public NotificationData GetNotificationData(Context context)
	{
		return GetNotificationData(context, new AppPreferences(context));
	}
	
	public NotificationData GetNotificationData(Context context, AppPreferences appPreferences)
	{
		
		DbHelper dbHelper=DbHelper.getInstance(context);

		ArrayList<String> selectedValues = appPreferences.GetSelectedValuesNotifications();
		HashMap<String, Integer> seek_states = appPreferences.GetSeekValues();
		Markers markers=dbHelper.GetUnreadCountsView();
		
		if (selectedValues.size() == 0)
		{
			Log.d(App.TAG, "No feeds selected for notification");
			return null;
		}
		
		int totalUnread=0;
		
		
		ArrayList<String> notiText = new ArrayList<String>();
		
		totalUnread=GetUnreadItems(context, markers, notiText, selectedValues, seek_states);
		
		String contentText = String.format(context.getString(R.string.noti_content_text), totalUnread);
		
		NotificationData data=new NotificationData(contentText, notiText, totalUnread);
		
		return data;
		
	}
	
	
	private int GetUnreadItems(Context context,  Markers markers, ArrayList<String> notiText, ArrayList<String> selectedValues, HashMap<String, Integer> seek_states  )
	{
		int totalUnread=0;
		
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
						notiText.add(String.format(context.getString(R.string.noti_inbox_text), marker.getCount(),marker.getTitle()  ));

					}

				}
			}
		}
		
		return totalUnread;
		
	}

}
