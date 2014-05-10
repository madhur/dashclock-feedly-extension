package in.co.madhur.dashclockfeedlyextension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.co.madhur.dashclockfeedlyextension.api.Category;
import in.co.madhur.dashclockfeedlyextension.api.Feedly;
import in.co.madhur.dashclockfeedlyextension.api.FeedlyData;
import in.co.madhur.dashclockfeedlyextension.api.Marker;
import in.co.madhur.dashclockfeedlyextension.api.Markers;
import in.co.madhur.dashclockfeedlyextension.api.Profile;
import in.co.madhur.dashclockfeedlyextension.api.Subscription;
import in.co.madhur.dashclockfeedlyextension.db.DbHelper;

import com.infospace.android.oauth2.WebApiHelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Filter.FilterListener;
import android.widget.ProgressBar;
import android.widget.Toast;

//import android.support.v7

public class MainActivity extends ActionBarActivity
{

	private WebApiHelper apiHelper;
	private AppPreferences appPreferences;

	private ProgressBar progressBar;
	private ExpandableListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork() // or
																																// .detectAll()
																																// for
																																// all
																																// detectable
																																// problems
		.penaltyLog().build());

		// StrictMode.setVmPolicy(new
		// StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		progressBar = (ProgressBar) findViewById(R.id.pbHeaderProgress);
		listView = (ExpandableListView) findViewById(R.id.listview);

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

	private void GetFeedlyData(boolean forceRefresh)
	{

		String token = appPreferences.GetToken();

		Feedly feedly = Feedly.getInstance(token);

		new GetFeedlyDataTask(feedly, forceRefresh).execute(0);

	}

	private void GetFeedlyData()
	{
		GetFeedlyData(false);
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
	public boolean onOptionsItemSelected(MenuItem item)
	{
		FeedlyListViewAdapter adapter = (FeedlyListViewAdapter) listView.getExpandableListAdapter();
		
		switch (item.getItemId())
		{
			case R.id.action_refresh:
				Refresh();

				break;

			case R.id.action_settings:
				Intent prefIntent = new Intent(this, FeedlyPreferenceActivity.class);
				startActivity(prefIntent);
				break;

			case R.id.action_accept:
				SaveSelectedCategories();
				finish();
				break;

			case R.id.action_selecteverything:
				adapter.selectAll();
				break;

			case R.id.action_selectallcategories:

				adapter.selectAllCategories();
				break;

			case R.id.action_selectallfeeds:
				adapter.selectAllFeeds();
				break;
				
			case R.id.action_selectnone:
				adapter.selectNone();
				break;

			default:
				return super.onOptionsItemSelected(item);
		}

		return super.onOptionsItemSelected(item);

	}

	private void SaveSelectedCategories()
	{
		FeedlyListViewAdapter listAdapter = (FeedlyListViewAdapter) listView.getExpandableListAdapter();

		if (listAdapter != null)
		{
			listAdapter.SaveSelectedValuestoPreferences();
		}
		else
			Log.e(App.TAG, "Adapter is null while saving selected values");
	}

	private void Refresh()
	{
		GetFeedlyData(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		//SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		MenuItem searchitem=menu.findItem(R.id.action_search);
		SearchView searchView=(SearchView) MenuItemCompat.getActionView(searchitem);
		SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
//	//	if (info != null && searchView != null)
//		{
			searchView.setSearchableInfo(info);

			SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener()
			{
				@Override
				public boolean onQueryTextChange(String newText)
				{
					FeedlyListViewAdapter adapter = (FeedlyListViewAdapter) listView.getExpandableListAdapter();
					adapter.getFilter().filter(newText, new FilterListener()
					{

						@Override
						public void onFilterComplete(int count)
						{
							Toast.makeText(MainActivity.this, String.valueOf(count), Toast.LENGTH_SHORT).show();

						}
					});

					System.out.println("on text chnge text: " + newText);
					return true;
				}

				@Override
				public boolean onQueryTextSubmit(String query)
				{
					FeedlyListViewAdapter adapter = (FeedlyListViewAdapter) listView.getExpandableListAdapter();
					adapter.getFilter().filter(query, new FilterListener()
					{

						@Override
						public void onFilterComplete(int count)
						{
							Toast.makeText(MainActivity.this, String.valueOf(count), Toast.LENGTH_SHORT).show();

						}
					});

					System.out.println("on query submit: " + query);
					return true;
				}
			};
			searchView.setOnQueryTextListener(textChangeListener);
		//}

		return true;
	}

	private class GetFeedlyDataTask extends
			AsyncTask<Integer, Integer, FeedlyData>
	{
		private DbHelper dbHelper;
		private Feedly feedly;
		private boolean forceRefresh;

		public GetFeedlyDataTask(Feedly feedly, boolean forceRefresh)
		{
			this.feedly = feedly;
			this.forceRefresh = forceRefresh;
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
			Map<Category, List<Subscription>> categorySubscriptions = new HashMap<Category, List<Subscription>>();
			//Markers markers;

			try
			{
				dbHelper = DbHelper.getInstance(MainActivity.this);

				if (dbHelper.IsFetchRequired() || forceRefresh)
				{

					profile = feedly.GetProfile();
					dbHelper.TruncateProfile();
					dbHelper.WriteProfile(profile);

					categories = feedly.GetCategories();
					dbHelper.TruncateCategories();
					dbHelper.WriteCategories(categories);

					subscriptions = feedly.GetSubscriptions();
					dbHelper.TruncateSubscriptions();
					dbHelper.WriteSubscriptions(subscriptions);

//					markers = feedly.GetUnreadCounts();
//					dbHelper.TruncateMarkers();
//					dbHelper.WriteMarkers(markers);

					for (Category category : categories)
					{
						categorySubscriptions.put(category, dbHelper.GetSubScriptionsForCategory(category.getId()));
						// category.setSubscriptions(dbHelper.GetSubScriptionsForCategory(category.getId()));

						// Log.v("Tag", category.getLabel());
					}

					// for (Subscription sub : subscriptions)
					// {
					// if (sub.getWebsite() != null)
					// //Log.v("Tag", sub.getWebsite());
					// }

//					List<Marker> markerList = markers.getUnreadcounts();
//
//					for (Marker marker : markerList)
//					{
//						Log.v("Tag", String.valueOf(marker.getCount()));
//					}
				}
				else
				{
					profile = dbHelper.GetProfile();

					categories = dbHelper.GetCategories();

					for (Category category : categories)
					{
						categorySubscriptions.put(category, dbHelper.GetSubScriptionsForCategory(category.getId()));
						// category.setSubscriptions(dbHelper.GetSubScriptionsForCategory(category.getId()));
						// Log.v("Tag", category.getLabel());
					}

					subscriptions = dbHelper.GetSubscriptions();

					//markers = dbHelper.GetUnreadCounts();

				}

				return new FeedlyData(profile, categories, subscriptions, categorySubscriptions, null);

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
		if (result.isError())
		{
			// Show error
			return;
		}

		FeedlyListViewAdapter adapter = new FeedlyListViewAdapter(result, this);

		listView.setAdapter(adapter);

	}

}
