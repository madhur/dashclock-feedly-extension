package in.co.madhur.dashclockfeedlyextension;

public class Consts
{

	
//	public static int stepValues[] = { 5, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
//	enum APPVIEW
//	{
//		WIDGET, NOTIFICATIONS
//		
//	};
	
	public static final String UPDATE_COUNT_ACTION="in.co.madhur.ACTION.UPDATE_COUNT";
	public static final String UPDATE_ACTION="in.co.madhur.ACTION.UPDATE";
	public static final String CATEGORY_DASHCLOCK="in.co.madhur.CATEGORY.DASHCLOCK";
	public static final String CATEGORY_WIDGET="in.co.madhur.ACTION.CATEGORY.WIDGET";
	public static final String TWITTER_URL="https://twitter.com/intent/user?screen_name=madhur25";
	public static final String UPDATE_SOURCE="update_source";
	public static final String ABOUT_URL="file:///android_asset/about.html";
	public static final String CHANGES_URL="file:///android_asset/changes.html";
	public static final String ABOUT_TAG="about";
	public static final String THEME_TAG="theme";
	public static final String WHATS_NEW_TAG="whatsnew";
	public static final String MY_EMAIL="ahuja.madhur@gmail.com";
	public static final String UNCATEGORIZED="Uncategorized";
	public static final String UNCAT_ID="user/%s/category/global.uncategorized";
	
	
	public enum UPDATESOURCE
	{
		ALARM("alarm"),
		ACCEPT_BUTTON("accept_button"),
		NETWORK_CHANGE("network_change"),
		SETTINGS_CHANGE("settings_change"),
		LOGOUT_BUTTON("logout_button"),
		WIDGET_REFRESH_BUTTON("widget_refresh_button");
		
		public final String key;

		private UPDATESOURCE(String key)
		{
			this.key = key;
		}
		
	}
}
