package in.co.madhur.dashclockfeedlyextension;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class FeedlyPreferenceActivity extends PreferenceActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings_layout);
	}

}
