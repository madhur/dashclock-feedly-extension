package in.co.madhur.dashclockfeedlyextension.ui;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;

import com.infospace.android.oauth2.LoginListener;

import in.co.madhur.dashclockfeedlyextension.App;
import in.co.madhur.dashclockfeedlyextension.R;

public class MainActivity extends BaseActivity implements  LoginListener, FragmentManager.OnBackStackChangedListener

{
    private boolean isActivityActive = true;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //SetupStrictMode();

        super.onCreate(savedInstanceState);


        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainFragment()).addToBackStack(null).commit();

        // Listen for changes in the back stack
        getFragmentManager().addOnBackStackChangedListener(this);
        // Handle when activity is recreated like on orientation Change


        if (getActionBar() != null) {

            getActionBar().setDisplayUseLogoEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(false);

            shouldDisplayHomeUp();
        }

        PreferenceManager.setDefaultValues(this, R.xml.settings_layout, false);

    }

    private void SetupStrictMode() {
        if (App.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork() // or
                    // .detectAll()
                    // for
                    // all
                    // detectable
                    // problems
                    .penaltyLog().build());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setActivityActive(false);
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp() {
        // Enable Up button only if there are entries in the back stack
        boolean canback = getFragmentManager().getBackStackEntryCount() > 0;
        if(getActionBar()!=null)
            getActionBar().setDisplayHomeAsUpEnabled(canback);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // This method is called when the up button is pressed. Just the pop
        // back stack.
        getFragmentManager().popBackStack();
        return true;
    }

    @Override
    public void Login() {
        MainFragment mainFragment = new MainFragment();
        Bundle data = new Bundle();
        data.putBoolean("refresh", true);
        mainFragment.setArguments(data);
        getFragmentManager().beginTransaction().replace(android.R.id.content, mainFragment).addToBackStack(null).commit();
    }

    public boolean isActivityActive() {
        return isActivityActive;
    }

    public void setActivityActive(boolean isActivityActive) {
        this.isActivityActive = isActivityActive;
    }

}
