package in.co.madhur.dashclockfeedlyextension.service;

import java.util.ArrayList;

import in.co.madhur.dashclockfeedlyextension.R;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
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
		noti.setContentIntent(GetPendingIntent(context, contentText));

		return noti;
	}

	public NotificationCompat.Builder GetExpandedBuilder(NotificationCompat.Builder builder, ArrayList<String> lines, String summaryText)
	{

		NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
		// Sets a title for the Inbox style big view
		inboxStyle.setBigContentTitle(context.getString(R.string.app_name));
		inboxStyle.setSummaryText(summaryText);
		
		for(String line: lines)
			inboxStyle.addLine(line);
		
		// Moves the big view style object into the notification object.
		builder.setStyle(inboxStyle);

		return builder;
	}

	private static PendingIntent GetPendingIntent(Context context, String contentText)
	{
		Intent toastIntent = new Intent();
		PendingIntent pi = PendingIntent.getActivity(context, 0, toastIntent, PendingIntent.FLAG_ONE_SHOT);
		return pi;

	}

	public void FireNotification(int id, NotificationCompat.Builder builder)
	{
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(id, builder.build());

	}

}
