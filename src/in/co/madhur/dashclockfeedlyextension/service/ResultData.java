package in.co.madhur.dashclockfeedlyextension.service;

import java.util.ArrayList;


public class ResultData
{
	
	private String title;
	private ArrayList<String> expandedBody=new ArrayList<String>();
	private int totalUnread;
	private long lastUpdated;
	private ArrayList<WidgetData> widgetData= new ArrayList<WidgetData>();
	
	public ResultData(String title, ArrayList<String> expandedBody, int status)
	{
		this.title=title;
		this.expandedBody=expandedBody;
		this.totalUnread=status;
				
	}
	
	
	public ResultData()
	{
	}


	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public ArrayList<String> getExpandedBody()
	{
		return expandedBody;
	}
	public void setExpandedBody(ArrayList<String> expandedBody)
	{
		this.expandedBody = expandedBody;
	}
	public int getUnreadCount()
	{
		return totalUnread;
	}
	public void setUnreadCount(int status)
	{
		this.totalUnread = status;
	}


	public long getLastUpdated()
	{
		return lastUpdated;
	}


	public void setLastUpdated(long lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}
	
	public ArrayList<WidgetData> getWidgetData()
	{
		return widgetData;
	}


	public void setWidgetData(ArrayList<WidgetData> widgetData)
	{
		this.widgetData = widgetData;
	}

	

}
