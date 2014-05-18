package in.co.madhur.dashclockfeedlyextension.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.co.madhur.dashclockfeedlyextension.App;
import in.co.madhur.dashclockfeedlyextension.AppPreferences;
import in.co.madhur.dashclockfeedlyextension.R;
import in.co.madhur.dashclockfeedlyextension.api.Marker;
import in.co.madhur.dashclockfeedlyextension.api.Markers;
import in.co.madhur.dashclockfeedlyextension.db.DbHelper;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

public class FeedlyWidgetListProvider implements RemoteViewsFactory
{
	private Context context;
	private DbHelper dbHelper;
	private Markers markers;
	private List<WidgetData> widgetData;
	private AppPreferences appPreferences;
	
	public FeedlyWidgetListProvider(Context context)
	{
		this.context=context;
	}

	@Override
	public void onCreate()
	{
		Log.v(App.TAG, "FeedlyWidgetListProvider:  onCreate  ;");
		
		dbHelper=DbHelper.getInstance(context);
		
		appPreferences=new AppPreferences(context);
		
		PullData();
		
	}
	
	private void PullData()
	{
		markers=dbHelper.GetUnreadCountsView();
		ArrayList<String> selectedValues = appPreferences.GetSelectedValuesNotifications();
		HashMap<String, Integer> seek_states = appPreferences.GetSeekValues();
		widgetData=new ArrayList<WidgetData>();
		
		for (String selValue : selectedValues)
		{
			for (Marker marker : markers.getUnreadcounts())
			{

				if (selValue.equalsIgnoreCase(marker.getId()))
				{

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
						widgetData.add(new WidgetData(marker.getTitle(), marker.getCount()));

					}

				}
			}
		}
		
	}

	@Override
	public void onDataSetChanged()
	{
		Log.v(App.TAG, "FeedlyWidgetListProvider:  onDataSetChanged  ;");
		
		PullData();

	}

	@Override
	public void onDestroy()
	{
		Log.v(App.TAG, "FeedlyWidgetListProvider:  onDestroy  ;");
	}

	@Override
	public int getCount()
	{
		return widgetData.size();
	}

	@Override
	public RemoteViews getViewAt(int position)
	{
		RemoteViews view=new RemoteViews(context.getPackageName(), R.layout.widget_row);
		WidgetData data=widgetData.get(position);
		
		view.setTextViewText(R.id.TitleTextView, data.getTitle());
		view.setTextViewText(R.id.CountTextView, String.valueOf(data.getCount()));
		
		
		return view;
	}

	@Override
	public RemoteViews getLoadingView()
	{
		return null;
	}

	@Override
	public int getViewTypeCount()
	{
		return 1;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public boolean hasStableIds()
	{
		return false;
	}

}
