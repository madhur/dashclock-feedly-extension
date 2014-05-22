package in.co.madhur.dashclockfeedlyextension.receiver;

import in.co.madhur.dashclockfeedlyextension.Consts;
import in.co.madhur.dashclockfeedlyextension.Consts.UPDATESOURCE;
import in.co.madhur.dashclockfeedlyextension.service.Connection;
import in.co.madhur.dashclockfeedlyextension.service.UpdateFeedCountService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;

public class FeedUpdateReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if(Connection.isConnected(context))
		{
			Intent serviceIntent=new Intent();
			serviceIntent.setClass(context, UpdateFeedCountService.class);
			serviceIntent.putExtra(Consts.UPDATE_SOURCE, UPDATESOURCE.ALARM.key);
			context.startService(serviceIntent);
		}

	}

}
