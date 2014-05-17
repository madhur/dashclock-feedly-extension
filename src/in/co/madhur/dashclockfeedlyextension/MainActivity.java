package in.co.madhur.dashclockfeedlyextension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.co.madhur.dashclockfeedlyextension.api.Category;
import in.co.madhur.dashclockfeedlyextension.api.Feedly;
import in.co.madhur.dashclockfeedlyextension.api.FeedlyData;
import in.co.madhur.dashclockfeedlyextension.api.Profile;
import in.co.madhur.dashclockfeedlyextension.api.Subscription;
import in.co.madhur.dashclockfeedlyextension.db.DbHelper;

import com.infospace.android.oauth2.WebApiHelper;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Configuration.Builder;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Filter.FilterListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity

{
	private WebApiHelper apiHelper;
	private AppPreferences appPreferences;
	// private FeedlyData result;
	private ProgressBar progressBar;
	private ExpandableListView listView;
	private FeedlyListViewAdapter notiAdapter;
	private int LOGIN_REQUEST_CODE = 1;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		SetupStrictMode();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		progressBar = (ProgressBar) findViewById(R.id.pbHeaderProgress);
		listView = (ExpandableListView) findViewById(R.id.listview);

		getSupportActionBar().setDisplayUseLogoEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		// getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		// getSupportActionBar().setDisplayShowTitleEnabled(false);

		// final String[] dropdownValues =
		// getResources().getStringArray(R.array.dropdown);
		//
		// // Specify a SpinnerAdapter to populate the dropdown list.
		// ArrayAdapter<String> adapter = new
		// ArrayAdapter<String>(getSupportActionBar().getThemedContext(),
		// android.R.layout.simple_spinner_item, android.R.id.text1,
		// dropdownValues);
		//
		// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//
		// // Set up the dropdown list navigation in the action bar.
		// getSupportActionBar().setListNavigationCallbacks(adapter, this);

		listView.setOnGroupClickListener(new OnGroupClickListener()
		{
			@Override
			public boolean onGroupClick(ExpandableListView parent, View clickedView, int groupPosition, long rowId)
			{
				ImageView groupIndicator = (ImageView) clickedView.findViewById(R.id.group_indicator);
				if (parent.isGroupExpanded(groupPosition))
				{
					parent.collapseGroup(groupPosition);
					groupIndicator.setImageResource(R.drawable.expander_open_holo_dark);
				}
				else
				{
					parent.expandGroup(groupPosition);
					groupIndicator.setImageResource(R.drawable.expander_close_holo_dark);
				}
				return true;
			}
		});

		appPreferences = new AppPreferences(this);
		if (!appPreferences.IsTokenPresent())
		{
			StartLoginProcedure();
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

	private void StartLoginProcedure()
	{
		Intent loginIntent = new Intent();
		loginIntent.setClass(this, LoginActivity.class);
		startActivityForResult(loginIntent, LOGIN_REQUEST_CODE);

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

		// StrictMode.setVmPolicy(new
		// StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

	}

	private void GetFeedlyData(boolean forceRefresh)
	{

		String token = appPreferences.GetToken();

		Feedly feedly = Feedly.getInstance(token);

		new GetFeedlyDataTask(feedly, forceRefresh).execute(0);

	}

	// private APPVIEW GetCurrentView()
	// {
	// int index = getSupportActionBar().getSelectedNavigationIndex();
	// return APPVIEW.values()[index];
	// }

	private void GetFeedlyData()
	{
		GetFeedlyData(false);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == 1 && requestCode == LOGIN_REQUEST_CODE)
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

			case R.id.action_expandall:
				ExpandAll();
				break;

			case R.id.action_collapseall:
				CollapseAll();
				break;

			case R.id.action_logout:
				Logout();
				break;

			default:
				return super.onOptionsItemSelected(item);
		}

		return super.onOptionsItemSelected(item);

	}

	private void Logout()
	{
		new AlertDialog.Builder(this).setTitle(getString(R.string.app_name)).setMessage(getString(R.string.logout_ques)).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				// continue with delete

				appPreferences.ClearTokens();
				StartLoginProcedure();
			}
		}).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				// do nothing
			}
		}).setIcon(android.R.drawable.ic_dialog_alert).show();

	}

	private void ExpandAll()
	{

		for (int i = 0; i < notiAdapter.getGroupCount(); ++i)
		{
			listView.expandGroup(i);
		}

	}

	private void CollapseAll()
	{
		for (int i = 0; i < notiAdapter.getGroupCount(); ++i)
		{
			listView.collapseGroup(i);
		}

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
	protected void onDestroy()
	{
		super.onDestroy();
		Crouton.cancelAllCroutons();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		// SearchView searchView = (SearchView)
		// menu.findItem(R.id.action_search).getActionView();
		MenuItem searchitem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchitem);
		SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
		// // if (info != null && searchView != null)
		// {
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
						//Toast.makeText(MainActivity.this, String.valueOf(count), Toast.LENGTH_SHORT).show();

					}
				});

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
						//Toast.makeText(MainActivity.this, String.valueOf(count), Toast.LENGTH_SHORT).show();

					}
				});

				return true;
			}
		};
		searchView.setOnQueryTextListener(textChangeListener);
		// }

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
			listView.setVisibility(View.GONE);
			// Crouton.showText(MainActivity.this,
			// "Getting the latest categories and feeds from Feedly",
			// Style.INFO, null, Configuration.DURATION_INFINITE)
			// Crouton.makeText(MainActivity.this,
			// "Getting the latest categories and feeds from Feedly",
			// Style.INFO).show();
			Configuration.Builder builder = new Builder();
			builder.setDuration(Configuration.DURATION_INFINITE);

		}

		@Override
		protected FeedlyData doInBackground(Integer... params)
		{
			List<Category> categories;
			Profile profile;
			List<Subscription> subscriptions;
			Map<Category, List<Subscription>> categorySubscriptions = new HashMap<Category, List<Subscription>>();
			// Markers markers;

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

					// markers = feedly.GetUnreadCounts();
					// dbHelper.TruncateMarkers();
					// dbHelper.WriteMarkers(markers);

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

					// List<Marker> markerList = markers.getUnreadcounts();
					//
					// for (Marker marker : markerList)
					// {
					// Log.v("Tag", String.valueOf(marker.getCount()));
					// }
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

					// markers = dbHelper.GetUnreadCounts();

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
			listView.setVisibility(View.VISIBLE);
			UpdateUI(result);
		}

	}

	private void UpdateUI(FeedlyData result)
	{
		FeedlyListViewAdapter adapter = null;
		// this.result=result;
		if (result.isError())
		{
			// Show error
			return;
		}

		// APPVIEW currentView = GetCurrentView();

		// widgetAdapter = new WidgetViewAdapter(result, this);
		notiAdapter = new NotificationViewAdapter(result, this);

		// Are these both required ?
		// widgetAdapter.GetSelectedValuesFromPreferences();
		notiAdapter.GetSelectedValuesFromPreferences();
		listView.setAdapter(notiAdapter);

		// if (currentView == APPVIEW.WIDGET)
		// {
		// adapter = widgetAdapter;
		// }
		// else if (currentView == APPVIEW.NOTIFICATIONS)
		// {
		// adapter = notiAdapter;
		// }
		//
		// if (adapter != null)
		// listView.setAdapter(adapter);

	}

	// @Override
	// public boolean onNavigationItemSelected(int itemPosition, long itemId)
	// {
	// if (initializing)
	// {
	// initializing = false;
	// return true;
	// }
	//
	// switch (APPVIEW.values()[itemPosition])
	// {
	//
	// case WIDGET:
	//
	// if (notiAdapter != null && widgetAdapter != null)
	// {
	// notiAdapter.SaveSelectedValuestoPreferences();
	// widgetAdapter.GetSelectedValuesFromPreferences();
	// listView.setAdapter(widgetAdapter);
	// }
	// else
	// Log.e(App.TAG, "Error");
	// return true;
	//
	// case NOTIFICATIONS:
	// if (notiAdapter != null && widgetAdapter != null)
	// {
	// widgetAdapter.SaveSelectedValuestoPreferences();
	// notiAdapter.GetSelectedValuesFromPreferences();
	// listView.setAdapter(notiAdapter);
	// }
	// else
	// Log.e(App.TAG, "Error");
	// return true;
	//
	// }
	// return false;
	// }

}
