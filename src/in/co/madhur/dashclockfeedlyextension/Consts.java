package in.co.madhur.dashclockfeedlyextension;

public class Consts
{

	public static final String FEEDLY_API_URL="https://sandbox.feedly.com";
	public static int stepValues[] = { 5, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
	enum APPVIEW
	{
		WIDGET, NOTIFICATIONS
		
	};
	
	public static final String UPDATE_COUNT_ACTION="in.co.madhur.ACTION.UPDATE_COUNT";
	public static final String UPDATE_ACTION="in.co.madhur.ACTION.UPDATE";
	public static final String CATEGORY_DASHCLOCK="in.co.madhur.CATEGORY.DASHCLOCK";
	public static final String CATEGORY_WIDGET="in.co.madhur.ACTION.CATEGORY.WIDGET";
	public static final String TWITTER_URL="https://twitter.com/intent/user?screen_name=madhur25";
	public static final String UPDATE_SOURCE="update_source";
	
	public enum UPDATESOURCE
	{
		ALARM("alarm"),
		ACCEPT_BUTTON("accept_button"),
		NETWORK_CHANGE("network_change");
		
		public final String key;

		private UPDATESOURCE(String key)
		{
			this.key = key;
		}
		
	}
}
