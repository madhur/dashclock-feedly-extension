package in.co.madhur.dashclockfeedlyextension.receiver;

import in.co.madhur.dashclockfeedlyextension.service.Alarms;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Alarms alarms = new Alarms(context);
		if (alarms.ShouldSchedule())
		{
			alarms.Schedule();
		}
	}

}
