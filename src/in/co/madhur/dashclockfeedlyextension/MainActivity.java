package in.co.madhur.dashclockfeedlyextension;

import java.util.List;

import in.co.madhur.dashclockfeedlyextension.api.Category;
import in.co.madhur.dashclockfeedlyextension.api.Feedly;
import in.co.madhur.dashclockfeedlyextension.api.FeedlyApi;

import com.infospace.android.oauth2.WebApiHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity
{

	WebApiHelper apiHelper;
	AppPreferences appPreferences;
	Feedly feedly;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		appPreferences = new AppPreferences(this);
		if (!appPreferences.IsTokenPresent())
		{
			Intent loginIntent = new Intent();
			loginIntent.setClass(this, LoginActivity.class);
			startActivityForResult(loginIntent, 1);
		}
		else
		{
			WebApiHelper.register(this);
			apiHelper = WebApiHelper.getInstance();

			if (apiHelper.shouldRefreshAccesToken())
			{

				apiHelper.refreshAccessTokenIfNeeded();
			}

			String token = appPreferences.GetToken();

			feedly = Feedly.getInstance(token);

			Thread thread = new Thread()
			{
				@Override
				public void run()
				{
					List<Category> categories = feedly.GetCategories();

					for (Category category : categories)
					{
						Log.v("Tag", category.getLabel());

					}
				}
			};

			thread.start();

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
