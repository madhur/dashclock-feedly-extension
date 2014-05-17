package in.co.madhur.dashclockfeedlyextension.service;

import java.util.ArrayList;


public class NotificationData
{
	
	private String title;
	private ArrayList<String> expandedBody;
	private int statusCount;
	
	public NotificationData(String title, ArrayList<String> expandedBody, int status)
	{
		this.title=title;
		this.expandedBody=expandedBody;
		this.statusCount=status;
				
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
	public int getStatus()
	{
		return statusCount;
	}
	public void setStatus(int status)
	{
		this.statusCount = status;
	}

}
