package in.co.madhur.dashclockfeedlyextension.ui;

import in.co.madhur.dashclockfeedlyextension.App;
import in.co.madhur.dashclockfeedlyextension.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

public class ThemeDialog extends DialogFragment
{

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.pick_theme).setItems(R.array.themes, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				Log.v(App.TAG, String.valueOf(which));

				switch (which)
				{
					case 0:
					//		getActivity().setTheme(R.style.Dark);
						break;

					case 1:
						getActivity().setTheme(R.style.Black);
						break;

					case 2:
						getActivity().setTheme(R.style.Light);
						break;

					case 3:
						// getActivity().setTheme(R.style.Sepia);
						break;
						
						
					

				}
				
				dialog.dismiss();
				//getActivity().recreate();
			}
		});
		return builder.create();
	}
}
