package in.co.madhur.dashclockfeedlyextension.widget;

public class WidgetData
{
	private String title;
	private int count;
	
	public WidgetData(String title, int count)
	{
		this.title=title;
		this.count=count;
	}
	
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public int getCount()
	{
		return count;
	}
	public void setCount(int count)
	{
		this.count = count;
	}

}
