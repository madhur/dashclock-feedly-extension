package in.co.madhur.dashclockfeedlyextension.widget;

import in.co.madhur.dashclockfeedlyextension.App;
import in.co.madhur.dashclockfeedlyextension.AppPreferences;
import in.co.madhur.dashclockfeedlyextension.Utils;
import in.co.madhur.dashclockfeedlyextension.AppPreferences.Keys;
import in.co.madhur.dashclockfeedlyextension.R;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class FeedlyWidgetProvider extends AppWidgetProvider
{

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		// widgets are not supported pre-honeycomb
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			UpdateWidget(context, appWidgetManager, appWidgetIds);
		}
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		super.onReceive(context, intent);

		Toast.makeText(context, "Updating widget", Toast.LENGTH_SHORT).show();
		// widgets are not supported pre-honeycomb
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{

			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

			ComponentName name = new ComponentName(context, getClass().getName());
			int[] appWidgetIds = appWidgetManager.getAppWidgetIds(name);

			UpdateWidget(context, appWidgetManager, appWidgetIds);
		}
	}

	private void UpdateWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		final int N = appWidgetIds.length;

		for (int i = 0; i < N; ++i)
		{
			try
			{
				RemoteViews remoteViews = updateWidgetListView(context, appWidgetIds[i]);
				appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
				appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.widgetListView);

			}
			catch (NullPointerException e)
			{
				Log.e(App.TAG, "Null in widget");
			}
		}

	}

	/**
	 * @param context
	 * @param appWidgetId
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private RemoteViews updateWidgetListView(Context context, int appWidgetId)
	{
		AppPreferences appPreferences = new AppPreferences(context);

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.feedly_appwidget);

		Intent svcIntent = new Intent(context, FeedlyWidgetsService.class);
		svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		{
			remoteViews.setRemoteAdapter(R.id.widgetListView, svcIntent);
		}
		else
		{
			remoteViews.setRemoteAdapter(appWidgetId, R.id.widgetListView, svcIntent);
		}
		remoteViews.setEmptyView(R.id.widgetListView, R.id.empty_view);

		if (appPreferences.GetWidgetIntent() != null)
			remoteViews.setOnClickFillInIntent(R.id.widgetListView, appPreferences.GetWidgetIntent());

		remoteViews.setTextViewText(R.id.updatedTextView, String.format(context.getString(R.string.lastupdate_display_format), Utils.GetFormattedDate(appPreferences.GetLastSuccessfulSync(), context)));
		remoteViews.setTextColor(R.id.CountTextView, appPreferences.GetColor(Keys.WIDGET_COUNT_COLOR));
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
		{
			remoteViews.setTextViewTextSize(R.id.updatedTextView, TypedValue.COMPLEX_UNIT_SP, context.getResources().getInteger(R.integer.font_medium));
		}

		remoteViews.setInt(R.id.widget_host, "setBackgroundColor", appPreferences.GetColor(Keys.WIDGET_BACKGROUND_COLOR));
		// remoteViews.setEmptyView(R.id.widgetListView, R.id.empty_view);

		return remoteViews;
	}

	private PendingIntent GetPendingIntent(Context context)
	{

		Intent widgetIntent = new AppPreferences(context).GetWidgetIntent();
		if (widgetIntent != null)
		{
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, widgetIntent, 0);
			return pendingIntent;
		}

		return null;
	}

	// convenience method to count the number of installed widgets
	private int widgetsInstalled(Context context)
	{
		ComponentName thisWidget = new ComponentName(context, FeedlyWidgetProvider.class);
		AppWidgetManager mgr = AppWidgetManager.getInstance(context);
		return mgr.getAppWidgetIds(thisWidget).length;
	}

}
