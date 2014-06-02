package in.co.madhur.dashclockfeedlyextension.receiver;

import in.co.madhur.dashclockfeedlyextension.App;
import in.co.madhur.dashclockfeedlyextension.Consts;
import in.co.madhur.dashclockfeedlyextension.service.Connection;
import in.co.madhur.dashclockfeedlyextension.service.UpdateFeedCountService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class FeedUpdateReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (Connection.isConnected(context))
		{
			Intent serviceIntent = new Intent();
			serviceIntent.setClass(context, UpdateFeedCountService.class);
			if (intent.getStringExtra(Consts.UPDATE_SOURCE) != null)
			{
				Log.v(App.TAG, "Found extra " + intent.getStringExtra(Consts.UPDATE_SOURCE));
				serviceIntent.putExtra(Consts.UPDATE_SOURCE, intent.getStringExtra(Consts.UPDATE_SOURCE));
			}
			context.startService(serviceIntent);
		}

	}

}
