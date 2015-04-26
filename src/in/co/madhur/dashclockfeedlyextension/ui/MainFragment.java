package in.co.madhur.dashclockfeedlyextension.ui;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Filter.FilterListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crittercism.app.Crittercism;
import com.infospace.android.oauth2.WebApiHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.co.madhur.dashclockfeedlyextension.App;
import in.co.madhur.dashclockfeedlyextension.AppPreferences;
import in.co.madhur.dashclockfeedlyextension.Consts;
import in.co.madhur.dashclockfeedlyextension.Consts.UPDATESOURCE;
import in.co.madhur.dashclockfeedlyextension.R;
import in.co.madhur.dashclockfeedlyextension.api.Category;
import in.co.madhur.dashclockfeedlyextension.api.Feedly;
import in.co.madhur.dashclockfeedlyextension.api.FeedlyData;
import in.co.madhur.dashclockfeedlyextension.api.Profile;
import in.co.madhur.dashclockfeedlyextension.api.Subscription;
import in.co.madhur.dashclockfeedlyextension.db.DbHelper;
import in.co.madhur.dashclockfeedlyextension.service.Alarms;
import in.co.madhur.dashclockfeedlyextension.service.Connection;

public class MainFragment extends Fragment
{

	private WebApiHelper apiHelper;
	private AppPreferences appPreferences;
	private ProgressBar progressBar;
	private ExpandableListView listView;
	private FeedlyListViewAdapter notiAdapter;
	private TextView statusText;
	private boolean forceRefresh;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);



		// Check if it has been instantiated through Login dialog, if yes, we
		// need a force refresh action to first time sync
		Bundle data = getArguments();
		if (data != null)
		{

			forceRefresh = data.getBoolean("refresh");
		}

		// Setup alarm if it doesn't exist
		Alarms alarms = new Alarms(getActivity());

		if (!alarms.DoesAlarmExist())
		{
			if (alarms.ShouldSchedule())
				alarms.Schedule();
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.activity_main, container, false);

		progressBar = (ProgressBar) v.findViewById(R.id.pbHeaderProgress);
		listView = (ExpandableListView) v.findViewById(R.id.listview);

		appPreferences = new AppPreferences(getActivity());

		statusText = (TextView) v.findViewById(R.id.statusMessage);

		final IThemeable themeAble = (IThemeable) getActivity();


		listView.setOnGroupClickListener(new OnGroupClickListener()
		{
			@Override
			public boolean onGroupClick(ExpandableListView parent, View clickedView, int groupPosition, long rowId)
			{
				ImageView groupIndicator = (ImageView) clickedView.findViewById(R.id.group_indicator);

				if (parent.isGroupExpanded(groupPosition))
				{
					parent.collapseGroup(groupPosition);

					if (themeAble.GetTheme() == 0)
						groupIndicator.setImageResource(R.drawable.expander_open_holo_dark);
					else if (themeAble.GetTheme() == 1)
						groupIndicator.setImageResource(R.drawable.expander_open_holo_light);
				}
				else
				{
					parent.expandGroup(groupPosition);

					if (themeAble.GetTheme() == 0)
						groupIndicator.setImageResource(R.drawable.expander_close_holo_dark);
					else if (themeAble.GetTheme() == 1)
						groupIndicator.setImageResource(R.drawable.expander_close_holo_light);
				}
				return true;
			}
		});

		if (!appPreferences.IsTokenPresent())
		{
			StartLoginProcedure();
		}
		else
		{
			WebApiHelper.register(getActivity());
			apiHelper = WebApiHelper.getInstance();
			apiHelper.refreshAccessTokenIfNeeded();

			GetFeedlyData(forceRefresh);

		}


        Toolbar toolbar = (Toolbar) v.findViewById(R.id.my_awesome_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


        return v;
	}

	private void GetFeedlyData(boolean forceRefresh)
	{

		String token = appPreferences.GetToken();

		Feedly feedly = Feedly.getInstance(token, getActivity());

		new GetFeedlyDataTask(feedly, forceRefresh).execute(0);

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
				Intent i = new Intent();
				i.setClass(getActivity(), SettingsActivity.class);
				//getActivity().finish();
				startActivity(i);


				break;

			case R.id.action_accept:
				SaveSelectedCategories();
				new Alarms(getActivity()).StartUpdate(UPDATESOURCE.ACCEPT_BUTTON);
				getActivity().finish();
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

			case R.id.action_about:
				ShowDialog();
				break;

			default:
				return super.onOptionsItemSelected(item);
		}

		return super.onOptionsItemSelected(item);

	}

	private void ShowDialog()
	{
		AboutDialog dialog = new AboutDialog();
		dialog.show(getFragmentManager(), Consts.ABOUT_TAG);


	}

	private void Logout()
	{
		new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.app_name)).setMessage(getString(R.string.logout_ques)).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				appPreferences.ClearTokens();
				StartLoginProcedure();
				new Alarms(getActivity()).StartUpdate(UPDATESOURCE.LOGOUT_BUTTON);
			}
		}).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
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
		if (Connection.isConnected(getActivity()))
		{
			GetFeedlyData(true);
		}
		else
			Toast.makeText(getActivity(), getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{

		inflater.inflate(R.menu.main, menu);

		SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
		MenuItem searchitem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchitem);
		SearchableInfo info = searchManager.getSearchableInfo(getActivity().getComponentName());
		searchView.setSearchableInfo(info);

		SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener()
		{
			@Override
			public boolean onQueryTextChange(String newText)
			{
				FeedlyListViewAdapter adapter = (FeedlyListViewAdapter) listView.getExpandableListAdapter();
				if (adapter != null)
				{
					adapter.getFilter().filter(newText, new FilterListener()
					{

						@Override
						public void onFilterComplete(int count)
						{

						}
					});
				}

				return true;
			}

			@Override
			public boolean onQueryTextSubmit(String query)
			{
				FeedlyListViewAdapter adapter = (FeedlyListViewAdapter) listView.getExpandableListAdapter();
				if (adapter != null)
				{
					adapter.getFilter().filter(query, new FilterListener()
					{

						@Override
						public void onFilterComplete(int count)
						{

						}
					});
				}

				return true;
			}

		};
		searchView.setOnQueryTextListener(textChangeListener);

		super.onCreateOptionsMenu(menu, inflater);
	}

	private void StartLoginProcedure()
	{
		getFragmentManager().beginTransaction().replace(android.R.id.content, new SplashFragment()).commit();
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
			statusText.setVisibility(View.GONE);



		}

		@Override
		protected FeedlyData doInBackground(Integer... params)
		{
			List<Category> categories;
			Profile profile;
			List<Subscription> subscriptions;
			Map<Category, List<Subscription>> categorySubscriptions = new HashMap<Category, List<Subscription>>();
			boolean isuncatSet = false;

			try
			{
				dbHelper = DbHelper.getInstance(getActivity());
				if (dbHelper.IsFetchRequired() || forceRefresh)
				{

					profile = feedly.GetProfile();
					categories = feedly.GetCategories();
					subscriptions = feedly.GetSubscriptions();

					// Add handling for uncategorized feeds
					for (Subscription sub : subscriptions)
					{
						if (sub.getCategories().size() == 0)
						{
							// Create a new category "Uncategorized", if not
							// created, with a unique id
							if (!isuncatSet)
							{
								Category uncategorizedCat = new Category();
								uncategorizedCat.setLabel(Consts.UNCATEGORIZED);
								uncategorizedCat.setId(String.format(Consts.UNCAT_ID, profile.getId()));
								categories.add(uncategorizedCat);

								isuncatSet = true;
							}

							ArrayList<Category> uncatList = new ArrayList<Category>();
							uncatList.add(categories.get(categories.size() - 1));
							sub.setCategories(uncatList);

						}
					}

					// Dump all the information to db

					dbHelper.TruncateProfile();
					dbHelper.WriteProfile(profile);

					dbHelper.TruncateCategories();
					dbHelper.WriteCategories(categories);

					dbHelper.TruncateSubscriptions();
					dbHelper.WriteSubscriptions(subscriptions);

					// Create a helper Category -> Subscription map for list
					// adapter so that we don't hit the disk again while
					// scrolling in list.
					for (Category category : categories)
					{
						categorySubscriptions.put(category, dbHelper.GetSubScriptionsForCategory(category.getId()));
					}

				}
				else
				{
					profile = dbHelper.GetProfile();

					categories = dbHelper.GetCategories();

					for (Category category : categories)
					{
						categorySubscriptions.put(category, dbHelper.GetSubScriptionsForCategory(category.getId()));
					}

					subscriptions = dbHelper.GetSubscriptions();
				}

				return new FeedlyData(profile, categories, subscriptions, categorySubscriptions, null);

			}
			catch (Exception e)
			{
                Crittercism.logHandledException(e);
                String msg;
                
                if(e.getMessage()!=null)
                {
                    msg=e.getMessage();
                }
                else
                {
                     msg = "Unknown error occurred";
                }

                Log.e(App.TAG, msg);
                e.printStackTrace();
                return new FeedlyData(msg);

            }
		}

		@Override
		protected void onPostExecute(FeedlyData result)
		{
			super.onPostExecute(result);

			if (getActivity()!=null)
			{
				UpdateUI(result);
			}
		}

	}

	private void UpdateUI(FeedlyData result)
	{

		progressBar.setVisibility(View.GONE);

		if (result.isError())
		{
			statusText.setVisibility(View.VISIBLE);

			statusText.setText(result.getErrorMessage());
			return;
		}
		if (result.getCategories().size() == 0)
		{
			statusText.setVisibility(View.VISIBLE);

			statusText.setText(getString(R.string.no_feed_subscriptions));
			notiAdapter = new NotificationViewAdapter(result, getActivity());
			listView.setAdapter(notiAdapter);
			return;

		}

		listView.setVisibility(View.VISIBLE);
		statusText.setVisibility(View.GONE);

		notiAdapter = new NotificationViewAdapter(result, getActivity());
		notiAdapter.GetSelectedValuesFromPreferences();
		listView.setAdapter(notiAdapter);

	}

}
