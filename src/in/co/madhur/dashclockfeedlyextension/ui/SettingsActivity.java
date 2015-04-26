package in.co.madhur.dashclockfeedlyextension.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import in.co.madhur.dashclockfeedlyextension.R;

public class SettingsActivity extends BaseActivity
{
	
	@Override
	protected void onCreate(Bundle arg0)
	{


		super.onCreate(arg0);
		
        setContentView(R.layout.settings_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		
		Intent i=new Intent();
		i.setClass(this, MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
		startActivity(i);
		finish();
	}

}
