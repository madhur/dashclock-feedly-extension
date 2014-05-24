package in.co.madhur.dashclockfeedlyextension.ui;

import com.infospace.android.oauth2.LoginListener;

import in.co.madhur.dashclockfeedlyextension.App;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.TargetApi;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v7.app.ActionBarActivity;

public class MainActivity extends ActionBarActivity implements OnBackStackChangedListener, LoginListener

{
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		SetupStrictMode();

		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayUseLogoEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);

		getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new MainFragment()).commit();

		// Listen for changes in the back stack
		getSupportFragmentManager().addOnBackStackChangedListener(this);
		// Handle when activity is recreated like on orientation Change
		shouldDisplayHomeUp();

	}

	private void SetupStrictMode()
	{
		if (App.DEBUG)
		{
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork() // or
			// .detectAll()
			// for
			// all
			// detectable
			// problems
			.penaltyLog().build());
		}

	}

	@Override
	public void onBackStackChanged()
	{
		shouldDisplayHomeUp();
	}

	public void shouldDisplayHomeUp()
	{
		// Enable Up button only if there are entries in the back stack
		boolean canback = getSupportFragmentManager().getBackStackEntryCount() > 0;
		getSupportActionBar().setDisplayHomeAsUpEnabled(canback);
	}

	@Override
	public boolean onSupportNavigateUp()
	{
		// This method is called when the up button is pressed. Just the pop
		// back stack.
		getSupportFragmentManager().popBackStack();
		return true;
	}

	@Override
	public void Login()
	{
		getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new MainFragment()).commit();
	}

}
