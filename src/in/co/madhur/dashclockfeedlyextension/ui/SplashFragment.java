package in.co.madhur.dashclockfeedlyextension.ui;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.infospace.android.oauth2.AuthenticationFragment;
import com.infospace.android.oauth2.WebApiHelper;

import in.co.madhur.dashclockfeedlyextension.R;
import in.co.madhur.dashclockfeedlyextension.service.Connection;

public class SplashFragment extends Fragment implements OnClickListener
{
    private Button loginButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.splash_login, container, false);
        loginButton = (Button) v.findViewById(R.id.login_button);

        loginButton.setOnClickListener(this);


        Toolbar toolbar = (Toolbar) v.findViewById(R.id.my_awesome_toolbar);
        ((ActionBarActivity) getActivity()).setSupportActionBar(toolbar);

        ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        return v;
    }

    @Override
    public void onClick(View v)
    {
        if (loginButton == v)
        {
            if (Connection.isConnected(getActivity()))
            {

                WebApiHelper.register(getActivity());


                getFragmentManager().beginTransaction().replace(android.R.id.content, new AuthenticationFragment()).commit();

            } else
            {

                Toast.makeText(getActivity(), getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
            }

        }

    }


}
