package in.co.madhur.dashclockfeedlyextension;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class AboutActivity extends Activity
{
	private WebView aboutWebView, whatsNewView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Holo_Dialog);

		
		setContentView(R.layout.about_dialog_activity);

		TabHost tabHost = (TabHost) findViewById(R.id.about_tab);
		aboutWebView = (WebView) findViewById(R.id.about_webview);
		whatsNewView = (WebView) findViewById(R.id.whatsnew_webview);

		tabHost.setup();

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

	}

	

}
