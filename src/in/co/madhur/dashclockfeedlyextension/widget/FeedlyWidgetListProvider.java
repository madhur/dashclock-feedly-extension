package in.co.madhur.dashclockfeedlyextension.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.co.madhur.dashclockfeedlyextension.App;
import in.co.madhur.dashclockfeedlyextension.AppPreferences;
import in.co.madhur.dashclockfeedlyextension.R;
import in.co.madhur.dashclockfeedlyextension.service.ResultData;
import in.co.madhur.dashclockfeedlyextension.service.StringFormatter;
import in.co.madhur.dashclockfeedlyextension.service.WidgetData;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FeedlyWidgetListProvider implements RemoteViewsFactory
{
	private Context context;
	private List<WidgetData> widgetData;
	private WidgetConfig widgetConfig;
	private Intent blankIntent;
	
	public FeedlyWidgetListProvider(Context context)
	{
		this.context = context;
		widgetConfig = new WidgetConfig();
		blankIntent=new Intent();
	}

	@Override
	public void onCreate()
	{

		PullData();

	}

	private void PullData()
	{
		ResultData data = new StringFormatter(context).GetResultData(context);

		if (data != null)
			widgetData = data.getWidgetData();
		else
			widgetData = new ArrayList<WidgetData>();
		
		widgetConfig.RefreshData(new AppPreferences(context));

		if (widgetConfig.getSortOrder() == 0)
			Collections.sort(widgetData, Collections.reverseOrder(new CountSorter()));
		else if (widgetConfig.getSortOrder() == 1)
			Collections.sort(widgetData, new NameSorter());
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

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public RemoteViews getViewAt(int position)
	{
		RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_row);
		WidgetData rowData = widgetData.get(position);

		if (rowData != null && view != null)
		{
			view.setTextViewText(R.id.TitleTextView, rowData.getTitle());
			view.setTextViewText(R.id.CountTextView, String.valueOf(rowData.getCount()));

			view.setTextColor(R.id.TitleTextView, widgetConfig.getTitleColor());
			view.setTextColor(R.id.CountTextView, widgetConfig.getCountColor());

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
			{
				view.setTextViewTextSize(R.id.TitleTextView, TypedValue.COMPLEX_UNIT_SP, widgetConfig.getTextSize());
				view.setTextViewTextSize(R.id.CountTextView, TypedValue.COMPLEX_UNIT_SP, widgetConfig.getTextSize());
			}
			else
			{

				view.setFloat(R.id.TitleTextView, "setTextSize", widgetConfig.getTextSize());
				view.setFloat(R.id.CountTextView, "setTextSize", widgetConfig.getTextSize());
			}
		}
		
		view.setOnClickFillInIntent(R.id.widget_row, blankIntent);

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
