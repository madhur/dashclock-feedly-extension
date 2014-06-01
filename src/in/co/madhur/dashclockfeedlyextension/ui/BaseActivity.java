package in.co.madhur.dashclockfeedlyextension.ui;

import in.co.madhur.dashclockfeedlyextension.AppPreferences;
import in.co.madhur.dashclockfeedlyextension.R;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public abstract class BaseActivity extends ActionBarActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		SetTheme();
		super.onCreate(savedInstanceState);
	}
	
	public void SetTheme()
	{
		AppPreferences appPreferences=new AppPreferences(this);
		if(appPreferences.GetTheme()==0)
		{
			this.setTheme(R.style.Black);
		}
		else if(appPreferences.GetTheme()==1)
		{
			this.setTheme(R.style.Light);
			
		}
		
	}

}
