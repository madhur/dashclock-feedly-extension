package in.co.madhur.dashclockfeedlyextension.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;

public class SettingsActivity extends ActionBarActivity
{
	
	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new FeedlyPreferenceFragment()).commit();
	}

}
