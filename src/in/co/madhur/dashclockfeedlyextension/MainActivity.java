package in.co.madhur.dashclockfeedlyextension;

import java.util.List;

import in.co.madhur.dashclockfeedlyextension.api.Category;
import in.co.madhur.dashclockfeedlyextension.api.Feedly;
import in.co.madhur.dashclockfeedlyextension.api.FeedlyApi;
import in.co.madhur.dashclockfeedlyextension.api.Marker;
import in.co.madhur.dashclockfeedlyextension.api.Markers;
import in.co.madhur.dashclockfeedlyextension.api.Profile;
import in.co.madhur.dashclockfeedlyextension.api.Subscription;
import in.co.madhur.dashclockfeedlyextension.db.DbHelper;

import com.infospace.android.oauth2.WebApiHelper;

import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

public class MainActivity extends Activity
{

	WebApiHelper apiHelper;
	AppPreferences appPreferences;
	
	ProgressBar progressBar;
	ExpandableListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		progressBar = (ProgressBar) findViewById(R.id.pbHeaderProgress);
		listView=(ExpandableListView) findViewById(R.id.listview);

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
			
			GetFeedlyData();

			
		}

	}
	
	private void GetFeedlyData()
	{
		
		String token = appPreferences.GetToken();

		Feedly feedly = Feedly.getInstance(token);

		new GetFeedlyDataTask(feedly).execute(0);

		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == 1 && requestCode == 1)
		{
			GetFeedlyData();

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class GetFeedlyDataTask extends
			AsyncTask<Integer, Integer, FeedlyData>
	{
		DbHelper dbHelper;
		Feedly feedly;
		
		public GetFeedlyDataTask(Feedly feedly)
		{
			this.feedly=feedly;
		}

		@Override
		protected void onPreExecute()
		{

			super.onPreExecute();
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected FeedlyData doInBackground(Integer... params)
		{
			List<Category> categories;
			Profile profile;
			List<Subscription> subscriptions;
			Markers markers ;
			
			try
			{
				dbHelper = DbHelper.getInstance(MainActivity.this);
				
				if(dbHelper.IsFetchRequired())
				{
					
    				 profile = feedly.GetProfile();
					dbHelper.WriteProfile(profile);

					categories = feedly.GetCategories();
					dbHelper.WriteCategories(categories);

					subscriptions = feedly.GetSubscriptions();
					dbHelper.WriteSubscriptions(subscriptions);

					markers = feedly.GetUnreadCounts();
					dbHelper.WriteMarkers(markers);

					for (Category category : categories)
					{
						Log.v("Tag", category.getLabel());
					}

					for (Subscription sub : subscriptions)
					{
						if (sub.getWebsite() != null)
							Log.v("Tag", sub.getWebsite());
					}

					List<Marker> markerList = markers.getUnreadcounts();

					for (Marker marker : markerList)
					{
						Log.v("Tag", String.valueOf(marker.getCount()));
					}
				}
				else
				{
					profile=dbHelper.GetProfile();
					
					categories=dbHelper.GetCategories();
					
					subscriptions=dbHelper.GetSubscriptions();
					
					markers=dbHelper.GetUnreadCounts();
					
				}
				

				return new FeedlyData(profile, categories, subscriptions, markers);

			}
			catch (Exception e)
			{
				Log.e(App.TAG, e.getMessage());
				e.printStackTrace();
				return new FeedlyData(e.getMessage());
			}
		}

		@Override
		protected void onPostExecute(FeedlyData result)
		{
			super.onPostExecute(result);
			progressBar.setVisibility(View.GONE);
				UpdateUI(result);


		}

	}

	private void UpdateUI(FeedlyData result)
	{
		if(result.isError())
		{
			// Show error
			return;
		}
		
		FeedlyListViewAdapter adapter=new FeedlyListViewAdapter(result);
		
		listView.setAdapter(adapter);

	}

}
