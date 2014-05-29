package in.co.madhur.dashclockfeedlyextension.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class SettingsActivity extends FragmentActivity
{
	
	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		
		getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new FeedlyPreferenceFragment()).commit();
	}

}
