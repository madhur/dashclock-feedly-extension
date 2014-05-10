package in.co.madhur.dashclockfeedlyextension.service;

import in.co.madhur.dashclockfeedlyextension.R;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

public class Notifications
{
	
	public static NotificationCompat.Builder GetNotificationBuilder(Context context)
	{
		NotificationCompat.Builder noti = new NotificationCompat.Builder(context);
		noti.setContentTitle(context.getString(R.string.app_name));
		noti.setAutoCancel(true);
		String contentText = null;

		noti.setTicker(contentText);
		noti.setContentText(contentText);

		noti.setSmallIcon(R.drawable.ic_notification);
		noti.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));

		noti.setContentIntent(GetPendingIntent(context, contentText));

		return noti;
	}

	private static PendingIntent GetPendingIntent(Context context, String contentText)
	{
		Intent toastIntent = new Intent();
		toastIntent.putExtra("message", contentText);

		PendingIntent pi = PendingIntent.getActivity(context, 0, toastIntent, PendingIntent.FLAG_ONE_SHOT);
		return pi;

	}
	
}
