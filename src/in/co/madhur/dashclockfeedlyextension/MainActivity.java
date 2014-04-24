package in.co.madhur.dashclockfeedlyextension;

import java.util.List;

import in.co.madhur.dashclockfeedlyextension.api.Category;
import in.co.madhur.dashclockfeedlyextension.api.Feedly;
import in.co.madhur.dashclockfeedlyextension.api.FeedlyApi;
import in.co.madhur.dashclockfeedlyextension.api.Profile;
import in.co.madhur.dashclockfeedlyextension.db.DbHelper;

import com.infospace.android.oauth2.WebApiHelper;

import android.os.AsyncTask;
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

			new GetFeedlyDataTask().execute(0);

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private class GetFeedlyDataTask extends AsyncTask<Integer, Integer, Integer>
	{
		DbHelper dbHelper;
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
		}
		

		@Override
		protected Integer doInBackground(Integer... params)
		{
			try
			{
				dbHelper=DbHelper.getInstance(MainActivity.this);
				
				Profile profile=feedly.GetProfile();
				
				Log.v(App.TAG, profile.getId());
				Log.v(App.TAG, profile.getEmail());
				
				dbHelper.WriteProfile(profile);
				
				List<Category> categories = feedly.GetCategories();
				
				dbHelper.WriteCategories(categories);
				
				for (Category category : categories)
				{
					Log.v("Tag", category.getLabel());
				}
				
			}
			catch(Exception e)
			{
				Log.e(App.TAG, e.getMessage());
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Integer result)
		{
			super.onPostExecute(result);
		}
		
		
	}

}
