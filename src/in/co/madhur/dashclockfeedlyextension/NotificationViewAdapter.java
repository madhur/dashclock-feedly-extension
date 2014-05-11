package in.co.madhur.dashclockfeedlyextension;

import in.co.madhur.dashclockfeedlyextension.api.Category;
import in.co.madhur.dashclockfeedlyextension.api.FeedlyData;
import in.co.madhur.dashclockfeedlyextension.api.Subscription;
import android.content.Context;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class NotificationViewAdapter extends FeedlyListViewAdapter
{
	protected ViewHolderItemNotification item;
	protected ViewHolderGroupNotification groupItem;

	public NotificationViewAdapter(FeedlyData result, Context context)
	{
		super(result, context);
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
				check_states.put(category.getId(), checkbox.isChecked());
				
				notifyDataSetChanged();

//				if (checkbox.isChecked())
//					groupItem.groupSeekbar.setVisibility(View.VISIBLE);
//				else
//					groupItem.groupSeekbar.setVisibility(View.INVISIBLE);
			}
		});

		// groupItem.childSeekbar.setProgress(0);

		groupItem.textViewItem.setText(category.getLabel());

		if (check_states.containsKey(category.getId()))
		{
			boolean checkCondition=check_states.get(category.getId());
			
			groupItem.checked.setChecked(checkCondition);
			if(checkCondition)
				groupItem.groupSeekbar.setVisibility(View.VISIBLE);
			else
				groupItem.groupSeekbar.setVisibility(View.GONE);
				
		}
		else
		{
			groupItem.checked.setChecked(false);
			groupItem.groupSeekbar.setVisibility(View.GONE);

		}

		return convertView;
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
				
				check_states.put(subscription.getId(), checkbox.isChecked());
				
				notifyDataSetChanged();
				
//				if (checkbox.isChecked())
//					item.childSeekbar.setVisibility(View.VISIBLE);
//				else
//					item.childSeekbar.setVisibility(View.INVISIBLE);

			}
		});

		item.title.setText(subscription.getTitle());
		item.website.setText(subscription.getWebsite());

		if (check_states.containsKey(subscription.getId()))
		{
			boolean check_condition=check_states.get(subscription.getId());
			item.checked.setChecked(check_condition);
			
			if(check_condition)
				item.childSeekbar.setVisibility(View.VISIBLE);
			else
				item.childSeekbar.setVisibility(View.GONE);
		}
		else
		{
			item.checked.setChecked(false);
			item.childSeekbar.setVisibility(View.GONE);
		}

		return convertView;
	}

	private static class ViewHolderGroupNotification extends ViewHolderGroup
	{
		SeekBar groupSeekbar;
	}

	private static class ViewHolderItemNotification extends ViewHolderItem
	{
		SeekBar childSeekbar;

	}

}
