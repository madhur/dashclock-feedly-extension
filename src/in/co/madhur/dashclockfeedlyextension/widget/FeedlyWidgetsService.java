package in.co.madhur.dashclockfeedlyextension.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViewsService;

public class FeedlyWidgetsService extends RemoteViewsService
{

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent)
	{
		return new FeedlyWidgetListProvider(this);
	}

}
