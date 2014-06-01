package in.co.madhur.dashclockfeedlyextension.widget;

import in.co.madhur.dashclockfeedlyextension.App;
import in.co.madhur.dashclockfeedlyextension.AppPreferences;
import in.co.madhur.dashclockfeedlyextension.Consts.UPDATESOURCE;
import in.co.madhur.dashclockfeedlyextension.Utils;
import in.co.madhur.dashclockfeedlyextension.AppPreferences.Keys;
import in.co.madhur.dashclockfeedlyextension.R;
import in.co.madhur.dashclockfeedlyextension.service.Alarms;
import in.co.madhur.dashclockfeedlyextension.ui.SettingsActivity;
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
import android.view.View;
import android.widget.RemoteViews;

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

		SetupLookAndFeel(context, remoteViews, appPreferences);

		if (!appPreferences.IsTokenPresent())
		{
			remoteViews.setTextViewText(R.id.empty_view, context.getString(R.string.login_required_desc));

			remoteViews.setViewVisibility(R.id.empty_view, View.VISIBLE);

			remoteViews.setTextViewText(R.id.updatedTextView, "");

		}
		else
		{

			remoteViews.setViewVisibility(R.id.empty_view, View.GONE);
			remoteViews.setTextViewText(R.id.empty_view, context.getString(R.string.no_unread_items));

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

			remoteViews.setTextViewText(R.id.updatedTextView, String.format(context.getString(R.string.lastupdate_display_format), Utils.GetFormattedDate(appPreferences.GetLastSuccessfulSync(), context)));

			if (appPreferences.GetWidgetIntent() != null)
				remoteViews.setOnClickFillInIntent(R.id.widgetListView, appPreferences.GetWidgetIntent());

		}

		return remoteViews;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void SetupLookAndFeel(Context context, RemoteViews remoteViews, AppPreferences appPreferences)
	{

		remoteViews.setInt(R.id.widget_host, "setBackgroundColor", appPreferences.GetColor(Keys.WIDGET_BACKGROUND_COLOR));
		
		if(appPreferences.GetTheme()==0)
		{
			remoteViews.setImageViewResource(R.id.widget_icon, R.drawable.ic_notification);
			remoteViews.setImageViewResource(R.id.widget_refresh, R.drawable.ic_action_refresh);
			remoteViews.setImageViewResource(R.id.widget_settings, R.drawable.ic_action_settings);
		}
		else if(appPreferences.GetTheme()==1)
		{
			remoteViews.setImageViewResource(R.id.widget_icon, R.drawable.ic_notification_light);
			remoteViews.setImageViewResource(R.id.widget_refresh, R.drawable.ic_action_refresh_light);
			remoteViews.setImageViewResource(R.id.widget_settings, R.drawable.ic_action_settings_light);
			
		}

		if (appPreferences.IsWidgetHeaderEnabled())
		{
			remoteViews.setViewVisibility(R.id.widget_icon, View.VISIBLE);
			remoteViews.setViewVisibility(R.id.updatedTextView, View.VISIBLE);
			remoteViews.setViewVisibility(R.id.widget_refresh, View.VISIBLE);
			remoteViews.setViewVisibility(R.id.widget_settings, View.VISIBLE);

			remoteViews.setOnClickPendingIntent(R.id.widget_icon, GetPendingIntent(context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()), context));
			remoteViews.setOnClickPendingIntent(R.id.widget_refresh, new Alarms(context).GetPendingIntent(context, UPDATESOURCE.WIDGET_REFRESH_BUTTON));
			remoteViews.setOnClickPendingIntent(R.id.widget_settings, GetSettingsPendingIntent(context));

		}
		else
		{
			remoteViews.setViewVisibility(R.id.widget_icon, View.GONE);
			remoteViews.setViewVisibility(R.id.updatedTextView, View.GONE);
			remoteViews.setViewVisibility(R.id.widget_refresh, View.GONE);
			remoteViews.setViewVisibility(R.id.widget_settings, View.GONE);
		}


		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
		{
			remoteViews.setTextViewTextSize(R.id.updatedTextView, TypedValue.COMPLEX_UNIT_SP, appPreferences.GetFontSize());
		}
		else
		{
			remoteViews.setFloat(R.id.updatedTextView, "setTextSize", appPreferences.GetFontSize());
		}
		
		remoteViews.setTextColor(R.id.updatedTextView, appPreferences.GetColor(Keys.WIDGET_TITLE_COLOR));

	}

	private PendingIntent GetSettingsPendingIntent(Context context)
	{
		Intent intent =new Intent();
		intent.setClass(context, SettingsActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		return pendingIntent;

	}

	private PendingIntent GetPendingIntent(Intent intent, Context context)
	{

		if (intent != null)
		{
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
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
