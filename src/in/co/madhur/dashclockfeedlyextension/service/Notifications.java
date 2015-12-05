package in.co.madhur.dashclockfeedlyextension.service;

import java.util.ArrayList;

import in.co.madhur.dashclockfeedlyextension.AppPreferences;
import in.co.madhur.dashclockfeedlyextension.R;
import in.co.madhur.dashclockfeedlyextension.AppPreferences.Keys;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class Notifications
{
	Context context;

	public Notifications(Context context)
	{

		this.context = context;
	}

	public NotificationCompat.Builder GetNotificationBuilder(String contentText, int number)
	{
		NotificationCompat.Builder noti = new NotificationCompat.Builder(context);
		noti.setContentTitle(context.getString(R.string.app_name));
		noti.setAutoCancel(true);
		noti.setTicker(contentText);
		noti.setContentText(contentText);
		noti.setSmallIcon(R.drawable.ic_notification);
		noti.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
		noti.setNumber(number);
		noti.setContentIntent(GetNotificationIntent());

		String notifSound = GetNotificationSound();
		if(notifSound.length()>0)
			noti.setSound(Uri.parse(notifSound));
		
		return noti;
	}
	
	
	private PendingIntent GetNotificationIntent()
	{
		Intent launchIntent=new Intent();
		launchIntent.setClass(context, LaunchService.class);
		Bundle data=new Bundle();
		data.putString("key", Keys.NOTIFICATION_CLICK_INTENT.key);
		launchIntent.putExtras(data);
		launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent resultPendingIntent = PendingIntent.getService(context, 0, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		return resultPendingIntent;
		
		
	}

	public NotificationCompat.Builder GetExpandedBuilder(NotificationCompat.Builder builder, ArrayList<String> lines, String summaryText)
	{

		NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
		// Sets a title for the Inbox style big view
		inboxStyle.setBigContentTitle(context.getString(R.string.app_name));
		inboxStyle.setSummaryText(summaryText);

		for (String line : lines)
			inboxStyle.addLine(line);


		// Moves the big view style object into the notification object.
		builder.setStyle(inboxStyle);

		return builder;
	}

	private String GetNotificationSound()
	{
		return new AppPreferences(context).getMetadata(Keys.NOTIFICATION_RINGTONE);
	}


	public void FireNotification(int id, NotificationCompat.Builder builder,  boolean vibrate, boolean sound, boolean led)
	{
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification=builder.build();
		
		if(vibrate)
			notification.defaults |= Notification.DEFAULT_VIBRATE;
		if(sound)
			notification.defaults |= Notification.DEFAULT_SOUND;
		if(led)
			notification.defaults |= Notification.DEFAULT_LIGHTS;
		
		mNotificationManager.notify(id, builder.build());
		
	}
	
	public void FireNotification(int id, NotificationCompat.Builder builder)
	{
		FireNotification(id, builder, true, true, true);
		
	}
	
	public void Cancel(int id)
	{
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(id);
	}
	
	

}
