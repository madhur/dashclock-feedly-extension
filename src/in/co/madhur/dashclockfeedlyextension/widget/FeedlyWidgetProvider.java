package in.co.madhur.dashclockfeedlyextension.widget;

import in.co.madhur.dashclockfeedlyextension.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.Toast;

public class FeedlyWidgetProvider extends AppWidgetProvider
{

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		final int N = appWidgetIds.length;

		Toast.makeText(context, "onUpdate " + String.valueOf(N), Toast.LENGTH_SHORT).show();

		for (int i = 0; i < N; ++i)
		{
			RemoteViews remoteViews = updateWidgetListView(context, appWidgetIds[i]);
			appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
		}

	}

	@Override
	public void onReceive(Context context, Intent intent)
	{

		super.onReceive(context, intent);

		Toast.makeText(context, "on recieve widget" + intent.getAction(), Toast.LENGTH_SHORT).show();
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

		ComponentName name = new ComponentName(context, FeedlyWidgetProvider.class);
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(name);

		final int N = appWidgetIds.length;
		
		Toast.makeText(context, "onReceive " + String.valueOf(N), Toast.LENGTH_SHORT).show();
		
		for (int i = 0; i < N; ++i)
		{
			RemoteViews remoteViews = updateWidgetListView(context, appWidgetIds[i]);
			appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
		}

	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private RemoteViews updateWidgetListView(Context context, int appWidgetId)
	{

		Toast.makeText(context, "updateWidgetListView "
				+ String.valueOf(appWidgetId), Toast.LENGTH_SHORT).show();
		
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.feedly_appwidget);

		Intent svcIntent = new Intent(context, FeedlyWidgetsService.class);
		svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
		remoteViews.setRemoteAdapter(R.id.widgetListView, svcIntent);
		remoteViews.setEmptyView(R.id.widgetListView, R.id.empty_view);
		
		return remoteViews;
	}

	// convenience method to count the number of installed widgets
	private int widgetsInstalled(Context context)
	{
		ComponentName thisWidget = new ComponentName(context, FeedlyWidgetProvider.class);
		AppWidgetManager mgr = AppWidgetManager.getInstance(context);
		return mgr.getAppWidgetIds(thisWidget).length;
	}

}
