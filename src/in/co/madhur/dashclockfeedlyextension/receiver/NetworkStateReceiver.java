package in.co.madhur.dashclockfeedlyextension.receiver;

import in.co.madhur.dashclockfeedlyextension.App;
import in.co.madhur.dashclockfeedlyextension.Consts;
import in.co.madhur.dashclockfeedlyextension.Consts.UPDATESOURCE;
import in.co.madhur.dashclockfeedlyextension.service.UpdateFeedCountService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkStateReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (intent.getExtras() != null)
		{
			NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
			if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED)
			{
				Intent serviceIntent=new Intent();
				serviceIntent.putExtra(Consts.UPDATE_SOURCE, UPDATESOURCE.NETWORK_CHANGE.key);
				serviceIntent.setClass(context, UpdateFeedCountService.class);
				context.startService(serviceIntent);
			}
		}

	}

}
