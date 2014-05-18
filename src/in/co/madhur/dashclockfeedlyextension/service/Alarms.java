package in.co.madhur.dashclockfeedlyextension.service;

import in.co.madhur.dashclockfeedlyextension.AppPreferences;
import in.co.madhur.dashclockfeedlyextension.Consts;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Alarms
{
	int REQUEST_CODE=0;
	private Context context;
	private AppPreferences appPreferences;
	
	public Alarms(Context context, AppPreferences appPreferences)
	{
		
		this.context=context;
		this.appPreferences=appPreferences;
	}
	
	public Alarms(Context context)
	{
		
		this.context=context;
		this.appPreferences=new AppPreferences(context);
	}
	
	public void Schedule()
	{
		
		AlarmManager alarmManager=GetAlarmManager(context);
		
		int prefInterval=appPreferences.GetSyncInterval();
		long recurInterval=prefInterval*60*60*1000;
		
		alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, recurInterval, GetPendingIntent(context) );
	}
	
	public void cancel()
	{
		GetAlarmManager(context).cancel(GetPendingIntent(context));
	}
	
	
	private static AlarmManager GetAlarmManager(Context context)
	{
		return (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		
	}
	
	private PendingIntent GetPendingIntent(Context context)
	{
		Intent updateIntent=new Intent();
		updateIntent.setAction(Consts.UPDATE_COUNT_ACTION);
		return PendingIntent.getBroadcast(context, REQUEST_CODE, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		//Intent serviceIntent=new Intent();
		//serviceIntent.setClass(context, UpdateFeedCountService.class);
		//return PendingIntent.getService(context , REQUEST_CODE, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
	}

	public boolean ShouldSchedule()
	{
		return appPreferences.IsSyncEnabled();
	}
	

}
