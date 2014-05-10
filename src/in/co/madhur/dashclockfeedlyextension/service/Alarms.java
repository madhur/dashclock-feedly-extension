package in.co.madhur.dashclockfeedlyextension.service;

import java.util.prefs.Preferences;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

public class Alarms
{
	
	private Context context;
	private Preferences appPreferences;
	
	public Alarms(Context context, Preferences appPreferences)
	{
		
		this.context=context;
		this.appPreferences=appPreferences;
	}
	
	public Alarms(Context context)
	{
		
		this.context=context;
		this.appPreferences=new Preferences(context);
	}
	
	public void Schedule()
	{
		
		AlarmManager alarmManager=GetAlarmManager(context);
		int recurTime=appPreferences.getFBInterval();
		long recurInterval=recurTime*1000;
		
		alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 10000, recurInterval, GetPendingIntent(context) );
		// alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 10000, 15000, GetPendingIntent(context) );
	}
	
	public void cancel()
	{
		GetAlarmManager(context).cancel(GetPendingIntent(context));
		
	}
	
	public boolean shouldSchedule()
	{
		boolean liveTrackEanbled=appPreferences.isLiveTrackEnabled();
		return liveTrackEanbled;
	}
	
	
	private static AlarmManager GetAlarmManager(Context context)
	{
		return (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		
	}
	
	private PendingIntent GetPendingIntent(Context context)
	{
		Intent fbIntent=new Intent();
		fbIntent.setAction(Consts.FB_POST_ACTION);
		// fbIntent.setClass(context , LiveTrackService.class);
		fbIntent.setClass(context, LiveTrackWakefulService.class);
		
		return PendingIntent.getService(context , 0, fbIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
	}
	

}
