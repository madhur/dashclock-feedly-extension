package in.co.madhur.dashclockfeedlyextension.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

public class SettingsActivity extends BaseActivity
{
	
	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new FeedlyPreferenceFragment()).commit();
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		Intent i=new Intent();
		i.setClass(this, MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		finish();
	}

}
