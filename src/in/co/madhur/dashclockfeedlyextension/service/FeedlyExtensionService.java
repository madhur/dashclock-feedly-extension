package in.co.madhur.dashclockfeedlyextension.service;

import in.co.madhur.dashclockfeedlyextension.App;
import in.co.madhur.dashclockfeedlyextension.AppPreferences;
import in.co.madhur.dashclockfeedlyextension.R;
import in.co.madhur.dashclockfeedlyextension.api.Marker;
import in.co.madhur.dashclockfeedlyextension.api.Markers;
import in.co.madhur.dashclockfeedlyextension.db.DbHelper;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;

import android.app.Service;
import android.content.Intent;
import android.graphics.DashPathEffect;
import android.os.IBinder;
import android.util.Log;

public class FeedlyExtensionService extends DashClockExtension
{
	private DbHelper dbHelper;
	private AppPreferences appPreferences;
	
	@Override
	protected void onInitialize(boolean isReconnect)
	{
		// TODO Auto-generated method stub
		super.onInitialize(isReconnect);
		dbHelper=DbHelper.getInstance(this);
		appPreferences = new AppPreferences(this);
	}

	@Override
	protected void onUpdateData(int arg0)
	{
		String status, expandedTitle, expandedBody;
		
		
		if (!appPreferences.IsTokenPresent())
		{
			Log.d(App.TAG, "Auth token not present, returning");
			return;
		}
		else
		{
			Markers markers=dbHelper.GetUnreadCountsView();
			
			for(Marker marker: markers.getUnreadcounts())
			{
				
				
				
			}
			
		
		}
		
		
////		try
////		{
////			publishUpdate(new ExtensionData().visible(true).status(status).icon(R.drawable.ic_dashclock).expandedTitle(expandedTitle).expandedBody(expandedBody.toString()));
////		}
//		catch (Exception e)
//		{
//
//			Log.e(App.TAG, "Exception while publishing:" + e.getMessage());
//		}
//		
	}
	
	

}
