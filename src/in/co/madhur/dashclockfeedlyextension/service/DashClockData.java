package in.co.madhur.dashclockfeedlyextension.service;

import java.util.ArrayList;
import java.util.Date;


public class DashClockData extends NotificationData
{
	
	private long lastUpdated;

	public DashClockData(String title, ArrayList<String> notiText, int totalUnread)
	{
		super(title, notiText, totalUnread);
	}

	public DashClockData(NotificationData data, long lastSync)
	{
		super(data.getTitle(), data.getExpandedBody(), data.getStatus());
		this.lastUpdated=lastSync;
	}

	public long getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(long lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

}

