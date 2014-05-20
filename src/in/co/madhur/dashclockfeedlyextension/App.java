package in.co.madhur.dashclockfeedlyextension;

import android.app.Application;

public class App extends Application
{
	
	//private static Bus bus;
	public static final String TAG="Feedly";
	public static final String LOG="feedly.log";
	
	public static final boolean DEBUG = true;
	public static final boolean LOCAL_LOGV = DEBUG;

	@Override
	public void onCreate()
	{
		super.onCreate();

		//bus = new Bus(ThreadEnforcer.ANY);
		
		 //Crittercism.initialize(getApplicationContext(), "5378d07cb573f17d76000002");
	}
}
