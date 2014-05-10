package in.co.madhur.dashclockfeedlyextension.service;

import in.co.madhur.dashclockfeedlyextension.AppPreferences;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Alarms
{
	
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
		long recurInterval=1000;
		alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 10000, recurInterval, GetPendingIntent(context) );
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
		Intent serviceIntent=new Intent();
		serviceIntent.setClass(context, UpdateFeedCountService.class);
		
		return PendingIntent.getService(context , 0, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
	}
	

}
