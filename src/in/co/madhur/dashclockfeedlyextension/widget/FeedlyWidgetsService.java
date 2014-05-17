package in.co.madhur.dashclockfeedlyextension.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViewsService;

public class FeedlyWidgetsService extends RemoteViewsService
{

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent)
	{
		// TODO Auto-generated method stub
		//int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		
		return new FeedlyWidgetListProvider(this);
	}

}
