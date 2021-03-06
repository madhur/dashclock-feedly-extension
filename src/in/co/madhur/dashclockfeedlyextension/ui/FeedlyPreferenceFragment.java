package in.co.madhur.dashclockfeedlyextension.ui;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.apps.dashclock.configuration.AppChooserPreference;

import in.co.madhur.dashclockfeedlyextension.AppPreferences;
import in.co.madhur.dashclockfeedlyextension.AppPreferences.Keys;
import in.co.madhur.dashclockfeedlyextension.Consts;
import in.co.madhur.dashclockfeedlyextension.R;
import in.co.madhur.dashclockfeedlyextension.service.Alarms;

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

	private final OnPreferenceChangeListener widgetListListener = new OnPreferenceChangeListener()
	{

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue)
		{
			UpdateLabel((ListPreference) preference, newValue.toString());
			WidgetSettingsChanged();
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

		// we cannot show widgets pre-honeycomb since we dont support them on
		// those versions
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
		{
			getPreferenceScreen().removePreference(findPreference(Keys.WIDGET_OPTIONS.key));
		}


     

	}


    @Override
	public void onResume()
	{
		super.onResume();

		SetListeners();

		UpdateLabel((ListPreference) findPreference(Keys.SYNC_INTERVAL.key), null);
		UpdateLabel((ListPreference) findPreference(Keys.MINIMUM_UNREAD.key), null);
		UpdateLabel((ListPreference) findPreference(Keys.PICK_THEME.key), null);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			UpdateLabel((ListPreference) findPreference(Keys.WIDGET_TEXT_SIZE.key), null);
		}
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
			if (listPreference.getKey().equals(Keys.MINIMUM_UNREAD.key))
			{
				listPreference.setTitle(String.format(getString(R.string.min_unread), newValue));
			}
			else
				listPreference.setTitle(newValue);
		}

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected void SetListeners()
	{

		findPreference(Keys.SYNC_INTERVAL.key).setOnPreferenceChangeListener(listPreferenceChangeListerner);
		findPreference(Keys.MINIMUM_UNREAD.key).setOnPreferenceChangeListener(listPreferenceChangeListerner);

		CharSequence intentSummary1 = AppChooserPreference.getDisplayValue(getActivity(), appPreferences.getMetadata(Keys.NOTIFICATION_CLICK_INTENT));
		findPreference(Keys.NOTIFICATION_CLICK_INTENT.key).setSummary(TextUtils.isEmpty(intentSummary1)
				|| intentSummary1.equals(getString(R.string.pref_shortcut_default)) ? ""
				: intentSummary1);

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

		findPreference(Keys.PICK_THEME.key).setOnPreferenceChangeListener(widgetListListener);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			SetWidgetListeners();
		}

	}

	private void SetWidgetListeners()
	{

		findPreference(Keys.WIDGET_CLICK_INTENT.key).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
		{

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue)
			{
				CharSequence intentSummary = AppChooserPreference.getDisplayValue(getActivity(), newValue.toString());
				getPreferenceScreen().findPreference(Keys.WIDGET_CLICK_INTENT.key).setSummary(TextUtils.isEmpty(intentSummary)
						|| intentSummary.equals(getResources().getString(R.string.pref_shortcut_default)) ? ""
						: intentSummary);

				WidgetSettingsChanged();

				return true;
			}

		});

		CharSequence intentSummary = AppChooserPreference.getDisplayValue(getActivity(), appPreferences.getMetadata(Keys.WIDGET_CLICK_INTENT));
		findPreference(Keys.WIDGET_CLICK_INTENT.key).setSummary(TextUtils.isEmpty(intentSummary)
				|| intentSummary.equals(getString(R.string.pref_shortcut_default)) ? ""
				: intentSummary);

		findPreference(Keys.WIDGET_BACKGROUND_COLOR.key).setOnPreferenceChangeListener(widgetChangeListener);
		findPreference(Keys.WIDGET_COUNT_COLOR.key).setOnPreferenceChangeListener(widgetChangeListener);
		findPreference(Keys.WIDGET_TITLE_COLOR.key).setOnPreferenceChangeListener(widgetChangeListener);
		findPreference(Keys.WIDGET_SORT_ORDER.key).setOnPreferenceChangeListener(widgetChangeListener);
		findPreference(Keys.ENABLE_WIDGET_HEADER.key).setOnPreferenceChangeListener(widgetChangeListener);
		findPreference(Keys.WIDGET_TEXT_SIZE.key).setOnPreferenceChangeListener(widgetListListener);

	}

	private void WidgetSettingsChanged()
	{
		// Broadcast intent to update widget

		Intent updateIntent = new Intent();
		updateIntent.setAction(Consts.UPDATE_ACTION);
		updateIntent.addCategory(Consts.CATEGORY_WIDGET);
		getActivity().sendBroadcast(updateIntent);

	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference)
	{
		super.onPreferenceTreeClick(preferenceScreen, preference);

		// This code cannot run pre honeycomb
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT)
		{
			// If the user has clicked on a preference screen, set up the action
			// bar
			if (preference instanceof PreferenceScreen)
			{
				initializeActionBar((PreferenceScreen) preference);
			}
		}

		if (preference != null)
			if (preference instanceof PreferenceScreen)
				if (((PreferenceScreen) preference).getDialog() != null)
					((PreferenceScreen) preference).getDialog().getWindow().getDecorView().setBackgroundDrawable(getActivity().getWindow().getDecorView().getBackground().getConstantState().newDrawable());

		return false;
	}

	/** Sets up the action bar for an {@link PreferenceScreen} */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void initializeActionBar(PreferenceScreen preferenceScreen)
	{
		final Dialog dialog = preferenceScreen.getDialog();

		if (dialog != null)
		{
			// Inialize the action bar
			if(dialog.getActionBar()!=null)
                dialog.getActionBar().setDisplayHomeAsUpEnabled(true);

            

			// Apply custom home button area click listener to close the
			// PreferenceScreen because PreferenceScreens are dialogs which
			// swallow
			// events instead of passing to the activity
			// Related Issue:
			// https://code.google.com/p/android/issues/detail?id=4611
			View homeBtn = dialog.findViewById(android.R.id.home);

			if (homeBtn != null)
			{
				OnClickListener dismissDialogClickListener = new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						dialog.dismiss();
					}
				};

				// Prepare yourselves for some hacky programming
				ViewParent homeBtnContainer = homeBtn.getParent();

				// The home button is an ImageView inside a FrameLayout
				if (homeBtnContainer instanceof FrameLayout)
				{
					ViewGroup containerParent = (ViewGroup) homeBtnContainer.getParent();

					if (containerParent instanceof LinearLayout)
					{
						// This view also contains the title text, set the whole
						// view as clickable
						((LinearLayout) containerParent).setOnClickListener(dismissDialogClickListener);
					}
					else
					{
						// Just set it on the home button
						((FrameLayout) homeBtnContainer).setOnClickListener(dismissDialogClickListener);
					}
				}
				else
				{
					// The 'If all else fails' default case
					homeBtn.setOnClickListener(dismissDialogClickListener);
				}
			}
		}
	}

}
