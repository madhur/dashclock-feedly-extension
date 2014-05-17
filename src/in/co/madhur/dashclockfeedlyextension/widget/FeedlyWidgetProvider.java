package in.co.madhur.dashclockfeedlyextension.widget;

import in.co.madhur.dashclockfeedlyextension.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

public class FeedlyWidgetProvider extends AppWidgetProvider
{

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{

		final int N = appWidgetIds.length;
		for (int i = 0; i < N; ++i)
		{
			RemoteViews remoteViews = updateWidgetListView(context, appWidgetIds[i]);
			appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private RemoteViews updateWidgetListView(Context context, int appWidgetId)
	{

		// which layout to show on widget
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.feedly_appwidget);

		// RemoteViews Service needed to provide adapter for ListView
		Intent svcIntent = new Intent(context, FeedlyWidgetsService.class);
		// passing app widget id to that RemoteViews Service
		svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		// setting a unique Uri to the intent
		// don't know its purpose to me right now
		//svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
		// setting adapter to listview of the widget
		remoteViews.setRemoteAdapter( R.id.widgetListView, svcIntent);
		// setting an empty view in case of no data
		remoteViews.setEmptyView(R.id.widgetListView, R.id.empty_view);
		return remoteViews;
	}

}
