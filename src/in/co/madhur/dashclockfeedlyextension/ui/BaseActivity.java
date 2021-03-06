package in.co.madhur.dashclockfeedlyextension.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import in.co.madhur.dashclockfeedlyextension.AppPreferences;
import in.co.madhur.dashclockfeedlyextension.R;

public abstract class BaseActivity extends AppCompatActivity implements IThemeable
{
	protected int currentTheme;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		SetTheme();
		super.onCreate(savedInstanceState);


	}
	
	@Override
	public void SetTheme()
	{
		AppPreferences appPreferences=new AppPreferences(this);
		currentTheme=appPreferences.GetTheme();
		
		if(currentTheme==0)
		{
			this.setTheme(R.style.Black);
		}
		else if(currentTheme==1)
		{
			this.setTheme(R.style.Light);
			
		}
		
	}
	
	@Override
	public int GetTheme()
	{
		return currentTheme;
	}

}
