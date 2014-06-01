package in.co.madhur.dashclockfeedlyextension.service;

import in.co.madhur.dashclockfeedlyextension.App;
import in.co.madhur.dashclockfeedlyextension.AppPreferences;
import in.co.madhur.dashclockfeedlyextension.Consts;
import in.co.madhur.dashclockfeedlyextension.Consts.UPDATESOURCE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Alarms
{
	int REQUEST_CODE=0;
	private Context context;
	private AppPreferences appPreferences;
	int LOWEST_RECUR_INTERVAL=1;
	
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
		
		//int prefInterval=appPreferences.GetSyncInterval();
		long recurInterval=LOWEST_RECUR_INTERVAL*60*60*1000;
		
		alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, recurInterval, GetPendingIntent(context, UPDATESOURCE.ALARM) );
	}
	
	public boolean DoesAlarmExist()
	{
		PendingIntent existingIntent=PendingIntent.getBroadcast(context, REQUEST_CODE, GetAlarmIntent(UPDATESOURCE.ALARM), PendingIntent.FLAG_NO_CREATE);
		
		if(existingIntent!=null)
		{
			Log.d(App.TAG, "Alarm exists");
			return true;
		}
		
		Log.d(App.TAG, "Alarm doesn't exist , scheduling");
		return false;
	}
	
	public void cancel()
	{
		GetAlarmManager(context).cancel(GetPendingIntent(context, UPDATESOURCE.ALARM));
	}
	
	
	private static AlarmManager GetAlarmManager(Context context)
	{
		return (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		
	}
	
	public PendingIntent GetPendingIntent(Context context, UPDATESOURCE source)
	{
		
		return PendingIntent.getBroadcast(context, REQUEST_CODE, GetAlarmIntent(source), PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	private Intent GetAlarmIntent(UPDATESOURCE source)
	{
		Intent updateIntent=new Intent();
		updateIntent.setAction(Consts.UPDATE_COUNT_ACTION);
		updateIntent.putExtra(Consts.UPDATE_SOURCE, source.key);
		return updateIntent;
	}
	
	public  void StartUpdate(UPDATESOURCE source)
	{
		Intent updateIntent=new Intent();
		updateIntent.putExtra(Consts.UPDATE_SOURCE, source.key);
		updateIntent.setClass(context, UpdateFeedCountService.class);
		context.startService(updateIntent);
	}

	public boolean ShouldSchedule()
	{
		return appPreferences.IsSyncEnabled();
	}
	

}
