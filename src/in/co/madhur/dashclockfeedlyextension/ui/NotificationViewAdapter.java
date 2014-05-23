package in.co.madhur.dashclockfeedlyextension.ui;

import java.util.ArrayList;
import java.util.HashMap;

import in.co.madhur.dashclockfeedlyextension.AppPreferences;
import in.co.madhur.dashclockfeedlyextension.R;
import in.co.madhur.dashclockfeedlyextension.api.Category;
import in.co.madhur.dashclockfeedlyextension.api.FeedlyData;
import in.co.madhur.dashclockfeedlyextension.api.Subscription;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class NotificationViewAdapter extends FeedlyListViewAdapter
{
	private ViewHolderItemNotification item;
	private ViewHolderGroupNotification groupItem;
	private HashMap<String, Integer> seek_states = new HashMap<String, Integer>();
	private AppPreferences appPreferences;
	
	public NotificationViewAdapter(FeedlyData result, Context context)
	{
		super(result, context);
		appPreferences=new AppPreferences(context);
	}

	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		final Category category = (Category) getGroup(groupPosition);
		if (convertView == null)
		{
			LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group_notification, null);

			groupItem = new ViewHolderGroupNotification();

			groupItem.textViewItem = (TextView) convertView.findViewById(R.id.lblListHeader);
			groupItem.checked = (CheckBox) convertView.findViewById(R.id.GroupCheckBox);
			groupItem.groupSeekbar = (SeekBar) convertView.findViewById(R.id.GroupSeekbar);
			groupItem.groupSeekCount = (TextView) convertView.findViewById(R.id.GroupSeekCount);
			groupItem.selectedChildCount=(TextView) convertView.findViewById(R.id.SelectedCountTextView);

			convertView.setTag(groupItem);
		}
		else
		{

			groupItem = (ViewHolderGroupNotification) convertView.getTag();
		}

		groupItem.checked.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				CheckBox checkbox = (CheckBox) v;
				if(checkbox.isChecked())
				{
					check_states.put(category.getId(), true);
					
					if(!seek_states.containsKey(category.getId()))
					{
						seek_states.put(category.getId(), GetMinimumUnreadDefault());
					}
				}
				else
				{
					check_states.remove(category.getId());
					
					//We do not remove seek states for unchecked items in a hope that they will be checked later and user will not have to set them agian
					// These values also go into preference
					// check_states should be subset of seek_states always
				}

				notifyDataSetChanged();

			}
		});

		groupItem.textViewItem.setText(category.getLabel());
		//groupItem.groupSeekCount.setText(String.valueOf(GetMinimumUnreadDefault()));
		
		groupItem.groupSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
				notifyDataSetChanged();
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				SetSeek(seekBar, category.getId(), progress);
				
			}
		});

		if (check_states.containsKey(category.getId()) && seek_states.containsKey(category.getId()))
		{
			groupItem.groupSeekCount.setText(String.valueOf(seek_states.get(category.getId())));
			groupItem.groupSeekbar.setProgress(seek_states.get(category.getId()));
			
			boolean checkCondition = check_states.get(category.getId());

			groupItem.checked.setChecked(checkCondition);
			if (checkCondition)
			{
				groupItem.groupSeekbar.setVisibility(View.VISIBLE);
				groupItem.groupSeekCount.setVisibility(View.VISIBLE);
			
			}
			else
			{
				groupItem.groupSeekbar.setVisibility(View.GONE);
				groupItem.groupSeekCount.setVisibility(View.GONE);
			}

		}
		else
		{
			groupItem.checked.setChecked(false);
			groupItem.groupSeekbar.setVisibility(View.GONE);
			groupItem.groupSeekCount.setVisibility(View.GONE);

		}
		
		HighlightGroup(groupPosition);
		
		return convertView;
	}
	
	private void HighlightGroup(int groupPosition)
	{
		int selectedChild=0;
		for(int i=0;i<getChildrenCount(groupPosition); ++i)
		{
			Subscription sub=(Subscription) getChild(groupPosition, i);
			if(check_states.containsKey(sub.getId()))
			{
				selectedChild++;
			}
			
		}
		
		if(selectedChild!=0)
		{
			groupItem.selectedChildCount.setVisibility(View.VISIBLE);
			groupItem.selectedChildCount.setText("("+String.valueOf(selectedChild)+")");

			groupItem.textViewItem.setTypeface(null, Typeface.BOLD);
		}
		else
		{
			groupItem.selectedChildCount.setVisibility(View.GONE);
			groupItem.textViewItem.setTypeface(null, Typeface.NORMAL);
		}
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		final Subscription subscription = (Subscription) getChild(groupPosition, childPosition);

		if (convertView == null)
		{
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item_notification, null);

			item = new ViewHolderItemNotification();

			item.title = (TextView) convertView.findViewById(R.id.lblListItem);
			item.checked = (CheckBox) convertView.findViewById(R.id.ChildCheckBox);
			item.website = (TextView) convertView.findViewById(R.id.WebsiteLabel);
			item.childSeekbar = (SeekBar) convertView.findViewById(R.id.ChildSeekbar);
			item.chidlSeekCount = (TextView) convertView.findViewById(R.id.ChildSeekCount);

			convertView.setTag(item);

		}
		else
		{
			item = (ViewHolderItemNotification) convertView.getTag();

		}

		item.checked.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				CheckBox checkbox = (CheckBox) v;

				if(checkbox.isChecked())
					check_states.put(subscription.getId(), true);
				else
					check_states.remove(subscription.getId());

				notifyDataSetChanged();

			}
		});

		item.title.setText(subscription.getTitle());
		item.website.setText(subscription.getWebsite());

		if (seek_states.containsKey(subscription.getId()))
		{
			item.chidlSeekCount.setText(String.valueOf(seek_states.get(subscription.getId())));
			item.childSeekbar.setProgress(seek_states.get(subscription.getId()));
		}

		item.childSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{

			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
				notifyDataSetChanged();

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				SetSeek(seekBar, subscription.getId(), progress);
			}
		});

		if (check_states.containsKey(subscription.getId()))
		{
			boolean check_condition = check_states.get(subscription.getId());
			item.checked.setChecked(check_condition);

			if (check_condition)
			{
				item.childSeekbar.setVisibility(View.VISIBLE);
				item.chidlSeekCount.setVisibility(View.VISIBLE);
				
				
			}
			else
			{
				item.childSeekbar.setVisibility(View.GONE);
				item.chidlSeekCount.setVisibility(View.GONE);
			}
		}
		else
		{
			item.checked.setChecked(false);
			item.childSeekbar.setVisibility(View.GONE);
			item.chidlSeekCount.setVisibility(View.GONE);
		}

		return convertView;
	}
	
	private void SetSeek(SeekBar seekBar, String id, int progress)
	{
//		for (int i = Consts.stepValues.length - 1; i > -1; --i)
//		{
//			if (progress > Consts.stepValues[i])
//			{
//				progress = progress / Consts.stepValues[i];
//				progress = progress * Consts.stepValues[i];
//				
//				seek_states.put(id, Consts.stepValues[i+1]);
//				break;
//
//			}
//
//		}
		
		seek_states.put(id, progress);
	}

	private static class ViewHolderGroupNotification extends ViewHolderGroup
	{
		SeekBar groupSeekbar;
		TextView groupSeekCount;
	}

	private static class ViewHolderItemNotification extends ViewHolderItem
	{
		SeekBar childSeekbar;
		TextView chidlSeekCount;

	}
	
	private int GetMinimumUnreadDefault()
	{
		AppPreferences appPreferences = new AppPreferences(context);
		return appPreferences.GetMinimumUnreadDefault();
		
	}

	@Override
	public void SaveSelectedValuestoPreferences()
	{
		AppPreferences appPreferences = new AppPreferences(context);
		appPreferences.SaveSelectedValuesNotifications(check_states, seek_states);
		
	}

	@Override
	public void GetSelectedValuesFromPreferences()
	{
		check_states.clear();
		
		AppPreferences appPreferences = new AppPreferences(context);
		ArrayList<String> selectedValues = appPreferences.GetSelectedValuesNotifications();

		for (String s : selectedValues)
		{
			check_states.put(s, true);
		}
		
		HashMap<String, Integer> seekValues=appPreferences.GetSeekValues();
		seek_states=seekValues;
		
	}

}
