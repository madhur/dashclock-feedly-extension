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
	public ResultData GetResultData(Context context)
	{
		return GetResultData(context, new AppPreferences(context));
	}

	public ResultData GetResultData(Context context, AppPreferences appPreferences)
	{

		DbHelper dbHelper = DbHelper.getInstance(context);

		ArrayList<String> selectedValues = appPreferences.GetSelectedValuesNotifications();
		HashMap<String, Integer> seek_states = appPreferences.GetSeekValues();
		Markers markers = dbHelper.GetUnreadCountsView();

		if (selectedValues.size() == 0)
		{
			Log.d(App.TAG, "No feeds selected for notification");
			return null;
		}

		ArrayList<String> notiText = new ArrayList<String>();

		ResultData data  = GetUnreadItems(context, markers, notiText, selectedValues, seek_states);

		String contentText = String.format(context.getString(R.string.noti_content_text), data.getUnreadCount());

		data.setTitle(contentText);

		long lastSync = appPreferences.GetLastSuccessfulSync();

		data.setLastUpdated(lastSync);

		return data;

	}

	private ResultData GetUnreadItems(Context context,  Markers markers, ArrayList<String> notiText, ArrayList<String> selectedValues, HashMap<String, Integer> seek_states)
	{
		ResultData resultData=new ResultData();
		
		int totalUnread = 0;
		AppPreferences appPreferences = new AppPreferences(context);
		for (String selValue : selectedValues)
		{
			for (Marker marker : markers.getUnreadcounts())
			{

				if (selValue.equalsIgnoreCase(marker.getId()))
				{

					if (marker.getCount() == 0)
						continue;

					int limitCount;

					if (!seek_states.containsKey(marker.getId()))
					{
						Log.w(App.TAG, "Seek state not found taking default "
								+ marker.getId());
						limitCount = appPreferences.GetMinimumUnreadDefault();
					}
					else
						limitCount = seek_states.get(marker.getId());

					if (marker.getCount() >= limitCount)
					{
						totalUnread = totalUnread + marker.getCount();
						resultData.getExpandedBody().add(String.format(context.getString(R.string.noti_inbox_text), marker.getCount(), marker.getTitle()));
						resultData.getWidgetData().add(new WidgetData(marker.getTitle(), String.valueOf(marker.getCount())));

					}

				}
			}
		}
		
		resultData.setUnreadCount(totalUnread);

		return resultData;

	}

}
