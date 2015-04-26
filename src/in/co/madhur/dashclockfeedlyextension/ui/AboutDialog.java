package in.co.madhur.dashclockfeedlyextension.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

import in.co.madhur.dashclockfeedlyextension.Consts;
import in.co.madhur.dashclockfeedlyextension.R;

public class AboutDialog extends DialogFragment
{
	private WebView aboutWebView, whatsNewView;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		View v = LayoutInflater.from(getActivity()).inflate(R.layout.about_dialog, null);
		builder.setView(v);

		TabHost tabHost = (TabHost) v.findViewById(R.id.about_tab);
		aboutWebView = (WebView) v.findViewById(R.id.about_webview);
		whatsNewView = (WebView) v.findViewById(R.id.whatsnew_webview);

		tabHost.setup();

		builder.setNeutralButton(android.R.string.ok, new OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();

			}
		});

		builder.setPositiveButton(R.string.feedback_button, new OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", Consts.MY_EMAIL, null));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_button));
				startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email)));

			}
		});

		builder.setNegativeButton(R.string.rate_button, new OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				Uri uri = Uri.parse("market://details?id="
						+ getActivity().getPackageName());
				Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
				try
				{
					startActivity(goToMarket);
				}
				catch (ActivityNotFoundException e)
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="
							+ getActivity().getPackageName())));
				}

			}
		});
		
		TabSpec aboutTab = tabHost.newTabSpec(Consts.ABOUT_TAG);
		aboutTab.setIndicator(getString(R.string.action_about));
		aboutTab.setContent(R.id.tab1);

		TabSpec whatsnewTab = tabHost.newTabSpec(Consts.WHATS_NEW_TAG);
		whatsnewTab.setIndicator(getString(R.string.whatsnew_tab));
		whatsnewTab.setContent(R.id.tab2);

		tabHost.addTab(aboutTab);
		tabHost.addTab(whatsnewTab);

		tabHost.setOnTabChangedListener(new OnTabChangeListener()
		{

			@Override
			public void onTabChanged(String tabId)
			{

			}
		});
		
		aboutWebView.loadUrl(Consts.ABOUT_URL);
		whatsNewView.loadUrl(Consts.CHANGES_URL);
		

		return builder.create();
	}


}
