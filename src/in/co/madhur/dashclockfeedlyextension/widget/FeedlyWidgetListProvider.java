package in.co.madhur.dashclockfeedlyextension.widget;

import java.util.ArrayList;
import java.util.List;

import in.co.madhur.dashclockfeedlyextension.App;
import in.co.madhur.dashclockfeedlyextension.AppPreferences;
import in.co.madhur.dashclockfeedlyextension.R;
import in.co.madhur.dashclockfeedlyextension.AppPreferences.Keys;
import in.co.madhur.dashclockfeedlyextension.service.ResultData;
import in.co.madhur.dashclockfeedlyextension.service.StringFormatter;
import in.co.madhur.dashclockfeedlyextension.service.WidgetData;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

public class FeedlyWidgetListProvider implements RemoteViewsFactory
{
	private Context context;
	private List<WidgetData> widgetData;
	private AppPreferences appPreferences;

	public FeedlyWidgetListProvider(Context context)
	{
		this.context = context;
		this.appPreferences = new AppPreferences(context);
	}

	@Override
	public void onCreate()
	{
		Log.v(App.TAG, "FeedlyWidgetListProvider:  onCreate  ;");

		PullData();

	}

	private void PullData()
	{
		ResultData data = new StringFormatter(context).GetResultData(context);

		if (data != null)
			widgetData = data.getWidgetData();
		else
			widgetData = new ArrayList<WidgetData>();
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
		WidgetData data = widgetData.get(position);

		if (data != null && view != null)
		{
			view.setTextViewText(R.id.TitleTextView, data.getTitle());
			view.setTextViewText(R.id.CountTextView, String.valueOf(data.getCount()));

			view.setTextColor(R.id.TitleTextView, appPreferences.GetColor(Keys.WIDGET_TITLE_COLOR));
			view.setTextColor(R.id.CountTextView,  appPreferences.GetColor(Keys.WIDGET_COUNT_COLOR));
			
			view.setTextViewTextSize(R.id.TitleTextView, TypedValue.COMPLEX_UNIT_SP, appPreferences.GetFontSize());
			view.setTextViewTextSize(R.id.CountTextView, TypedValue.COMPLEX_UNIT_SP, appPreferences.GetFontSize());
			
		}

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
