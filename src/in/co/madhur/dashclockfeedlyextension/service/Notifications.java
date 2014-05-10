package in.co.madhur.dashclockfeedlyextension.service;

import android.content.Context;
import android.support.v4.app.NotificationCompat;

public class Notifications
{
	
	public static NotificationCompat.Builder GetNotificationBuilder(Context context, NotificationType type)
	{
		NotificationCompat.Builder noti = new NotificationCompat.Builder(context);
		noti.setContentTitle(context.getString(R.string.app_name));
		noti.setAutoCancel(true);
		String contentText = null;

		switch (type)
		{
			case LOCATION_FAILURE:
				contentText = context.getString(R.string.noti_loc_failure);

				break;

			case FB_POSTED:
				contentText = context.getString(R.string.noti_fb_posted);
				break;

			case FB_FAILURE:
				contentText = context.getString(R.string.noti_fb_failure);

				break;

			case FB_SESSION_FAILURE:
				contentText = context.getString(R.string.noti_fb_session_failure);

			case FB_NOT_CONNECTED:
				contentText = context.getString(R.string.noti_fb_not_connected);

			default:
				break;

		}

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
		toastIntent.setClass(context, ToastActivity.class);

		PendingIntent pi = PendingIntent.getActivity(context, 0, toastIntent, PendingIntent.FLAG_ONE_SHOT);
		return pi;

	}

	public static NotificationCompat.Builder GetNotificationBuilder(Context context, NotificationType type, String errorMessage)
	{
		NotificationCompat.Builder noti = new NotificationCompat.Builder(context);
		noti.setContentTitle(context.getString(R.string.app_name));
		noti.setAutoCancel(true);
		noti.setContentText(errorMessage);
		noti.setTicker(context.getString(R.string.app_name) + ": "
				+ errorMessage);
		noti.setSmallIcon(R.drawable.ic_notification);
		noti.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));

		noti.setContentIntent(GetPendingIntent(context, errorMessage));

		return noti;
	}

	public static NotificationCompat.Builder GetNotificationBuilder(Context context, String sender, String errorMessage)
	{
		NotificationCompat.Builder noti = new NotificationCompat.Builder(context);
		noti.setContentTitle(sender);
		noti.setAutoCancel(true);
		noti.setContentText(errorMessage);
		noti.setTicker(sender + ": " + errorMessage);
		noti.setSmallIcon(R.drawable.ic_notification);
		noti.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));

		noti.setContentIntent(GetPendingIntent(context, errorMessage));

		return noti;
	}

	public static NotificationCompat.Builder GetNotificationBuilderSMS(Context context, String sender, NotificationType type)
	{
		NotificationCompat.Builder noti = new NotificationCompat.Builder(context);
		noti.setContentTitle(sender);
		noti.setAutoCancel(true);
		String contentText = null;

		switch (type)
		{
			case INCOMING_SMS:
				contentText = context.getString(R.string.noti_loc_request);
				noti.setTicker(sender + ": "
						+ context.getString(R.string.noti_loc_request));

				break;

			case OUTGOING_SMS:
				contentText = context.getString(R.string.noti_loc_response);
				noti.setTicker(sender + ": "
						+ context.getString(R.string.noti_loc_response));

				break;

			case LOCATION_FAILURE:
				contentText = context.getString(R.string.noti_loc_failure);
				noti.setTicker(sender + ": "
						+ context.getString(R.string.noti_loc_failure));

				break;

			case SENDER_NOTIN_CONTACTS:
				contentText = context.getString(R.string.noti_sender_notin_contacts);
				noti.setTicker(sender
						+ ": "
						+ context.getString(R.string.noti_sender_notin_contacts));

			default:
				break;

		}

		noti.setContentText(contentText);
		noti.setSmallIcon(R.drawable.ic_notification);

		noti.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));

		noti.setContentIntent(GetPendingIntent(context, contentText));

		return noti;
	}

}
