package in.co.madhur.dashclockfeedlyextension;

import com.crittercism.app.Crittercism;

import android.app.Application;

public class App extends Application
{

	public static final String TAG = "Feedly";
	public static final String LOG = "feedly.log";

	public static final boolean DEBUG = true;
	public static final boolean LOCAL_LOGV = DEBUG;

	@Override
	public void onCreate()
	{
		super.onCreate();
		Crittercism.initialize(getApplicationContext(), "5378d07cb573f17d76000002");
	}
}
