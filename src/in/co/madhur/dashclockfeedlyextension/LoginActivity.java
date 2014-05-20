package in.co.madhur.dashclockfeedlyextension;

import com.infospace.android.oauth2.AuthenticationFragment;
import com.infospace.android.oauth2.WebApiHelper;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

public class LoginActivity extends FragmentActivity implements LoginListener
{
	WebApiHelper apiHelper;
	AppPreferences appPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		appPreferences=new AppPreferences(this);
		
		setContentView(R.layout.activity_login);
		
		WebApiHelper.register(this);
		
		apiHelper=WebApiHelper.getInstance();
		
	}
	
	@Override
	public void Login()
	{
		AuthenticationFragment fr = new AuthenticationFragment();
		FragmentManager fm = getSupportFragmentManager();

		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		
		fragmentTransaction.replace(R.id.login_holder, fr);

		fragmentTransaction.commit();
	}

	
}
