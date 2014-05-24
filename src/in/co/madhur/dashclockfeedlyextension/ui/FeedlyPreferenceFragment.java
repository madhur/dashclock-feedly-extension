package in.co.madhur.dashclockfeedlyextension.ui;

import in.co.madhur.dashclockfeedlyextension.AppPreferences;
import in.co.madhur.dashclockfeedlyextension.Consts;
import in.co.madhur.dashclockfeedlyextension.R;
import in.co.madhur.dashclockfeedlyextension.AppPreferences.Keys;
import in.co.madhur.dashclockfeedlyextension.service.Alarms;

import com.google.android.apps.dashclock.configuration.AppChooserPreference;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.support.v4.preference.PreferenceFragment;
import android.text.TextUtils;

public class FeedlyPreferenceFragment extends PreferenceFragment
{
	
	private AppPreferences appPreferences;

	private final OnPreferenceChangeListener listPreferenceChangeListerner = new OnPreferenceChangeListener()
	{

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue)
		{
			UpdateLabel((ListPreference) preference, newValue.toString());
			return true;
		}
	};
	
	
	private final OnPreferenceChangeListener widgetChangeListener = new OnPreferenceChangeListener()
	{

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue)
		{
			WidgetSettingsChanged();
			return true;
		}
	};

	
	
	
	@Override
	public void onCreate(Bundle paramBundle)
	{
		super.onCreate(paramBundle);
		
		addPreferencesFromResource(R.xml.settings_layout);
		
		appPreferences = new AppPreferences(getActivity());
	}
	

	@Override
	public void onResume()
	{
		super.onResume();

		SetListeners();

		UpdateLabel((ListPreference) findPreference(Keys.SYNC_INTERVAL.key), null);
		UpdateLabel((ListPreference) findPreference(Keys.MINIMUM_UNREAD.key), null);
		UpdateLabel((ListPreference) findPreference(Keys.PICK_THEME.key), null);
		UpdateLabel((ListPreference) findPreference(Keys.WIDGET_TEXT_SIZE.key), null);
	}

	protected void UpdateLabel(ListPreference listPreference, String newValue)
	{

		if (newValue == null)
		{
			newValue = listPreference.getValue();
		}

		int index = listPreference.findIndexOfValue(newValue);
		if (index != -1)
		{
			newValue = (String) listPreference.getEntries()[index];
			if(listPreference.getKey().equals(Keys.MINIMUM_UNREAD.key))
			{
				listPreference.setTitle(String.format(getString(R.string.min_unread), newValue));
			}
			else
			listPreference.setTitle(newValue);
		}

	}

	protected void SetListeners()
	{


		findPreference(Keys.SYNC_INTERVAL.key).setOnPreferenceChangeListener(listPreferenceChangeListerner);
		findPreference(Keys.MINIMUM_UNREAD.key).setOnPreferenceChangeListener(listPreferenceChangeListerner);

		CharSequence intentSummary = AppChooserPreference.getDisplayValue(getActivity(), appPreferences.getMetadata(Keys.CLICK_INTENT));
		findPreference(Keys.CLICK_INTENT.key).setSummary(TextUtils.isEmpty(intentSummary)
				|| intentSummary.equals(getString(R.string.pref_shortcut_default)) ? ""
				: intentSummary);

		intentSummary = AppChooserPreference.getDisplayValue(getActivity(), appPreferences.getMetadata(Keys.NOTIFICATION_CLICK_INTENT));
		findPreference(Keys.NOTIFICATION_CLICK_INTENT.key).setSummary(TextUtils.isEmpty(intentSummary)
				|| intentSummary.equals(getString(R.string.pref_shortcut_default)) ? ""
				: intentSummary);

		findPreference(Keys.CLICK_INTENT.key).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
		{

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue)
			{
				CharSequence intentSummary = AppChooserPreference.getDisplayValue(getActivity(), newValue.toString());
				getPreferenceScreen().findPreference(Keys.CLICK_INTENT.key).setSummary(TextUtils.isEmpty(intentSummary)
						|| intentSummary.equals(getResources().getString(R.string.pref_shortcut_default)) ? ""
						: intentSummary);
				return true;
			}

		});

		findPreference(Keys.NOTIFICATION_CLICK_INTENT.key).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
		{

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue)
			{
				CharSequence intentSummary = AppChooserPreference.getDisplayValue(getActivity(), newValue.toString());
				getPreferenceScreen().findPreference(Keys.NOTIFICATION_CLICK_INTENT.key).setSummary(TextUtils.isEmpty(intentSummary)
						|| intentSummary.equals(getResources().getString(R.string.pref_shortcut_default)) ? ""
						: intentSummary);
				return true;
			}

		});

		findPreference(Keys.ENABLE_AUTOSYNC.key).setOnPreferenceChangeListener(new OnPreferenceChangeListener()
		{

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue)
			{
				Boolean newVal = (Boolean) newValue;
				Alarms alarms = new Alarms(getActivity());

				if (newVal)
					alarms.Schedule();
				else
					alarms.cancel();

				return true;
			}
		});
		
		findPreference(Keys.ACTION_ABOUT.key).setOnPreferenceClickListener(new OnPreferenceClickListener()
		{
			
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				AboutDialog dialog = new AboutDialog();
				dialog.show(getFragmentManager(), Consts.ABOUT_TAG);
				
				return true;
			}
		});

		findPreference(Keys.FOLLOW_TWITTER.key).setOnPreferenceClickListener(new OnPreferenceClickListener()
		{

			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Consts.TWITTER_URL)));
				return true;
			}
		});
		
		findPreference(Keys.PICK_THEME.key).setOnPreferenceClickListener(new OnPreferenceClickListener()
		{
			
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		findPreference(Keys.WIDGET_BACKGROUND_COLOR.key).setOnPreferenceChangeListener(widgetChangeListener);
		findPreference(Keys.WIDGET_COUNT_COLOR.key).setOnPreferenceChangeListener(widgetChangeListener);
		findPreference(Keys.WIDGET_TITLE_COLOR.key).setOnPreferenceChangeListener(widgetChangeListener);
		findPreference(Keys.WIDGET_TEXT_SIZE.key).setOnPreferenceChangeListener(widgetChangeListener);
		
		
	}
	
	private void WidgetSettingsChanged()
	{
		// Broadcast intent to update widget
		
		Intent updateIntent = new Intent();
		updateIntent.setAction(Consts.UPDATE_ACTION);
		updateIntent.addCategory(Consts.CATEGORY_WIDGET);
		getActivity().sendBroadcast(updateIntent);
		
		
	}

}
