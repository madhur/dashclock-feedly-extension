package in.co.madhur.dashclockfeedlyextension;

import in.co.madhur.dashclockfeedlyextension.api.Category;
import in.co.madhur.dashclockfeedlyextension.api.FeedlyData;
import in.co.madhur.dashclockfeedlyextension.api.Subscription;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

public class WidgetViewAdapter extends FeedlyListViewAdapter
{
	protected ViewHolderItemWidget item;
	protected ViewHolderGroupWidget groupItem;

	public WidgetViewAdapter(FeedlyData result, Context context)
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
			convertView = infalInflater.inflate(R.layout.list_group_widget, null);

			groupItem = new ViewHolderGroupWidget();

			groupItem.textViewItem = (TextView) convertView.findViewById(R.id.lblListHeader);
			groupItem.checked = (CheckBox) convertView.findViewById(R.id.GroupCheckBox);

			convertView.setTag(groupItem);
		}
		else
		{

			groupItem = (ViewHolderGroupWidget) convertView.getTag();
		}

		groupItem.checked.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				CheckBox checkbox = (CheckBox) v;
				check_states.put(category.getId(), checkbox.isChecked());
			}
		});

		groupItem.textViewItem.setText(category.getLabel());

		if (check_states.containsKey(category.getId()))
		{
			groupItem.checked.setChecked(check_states.get(category.getId()));
		}
		else
		{
			groupItem.checked.setChecked(false);

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
			convertView = infalInflater.inflate(R.layout.list_item_widget, null);

			item = new ViewHolderItemWidget();

			item.title = (TextView) convertView.findViewById(R.id.lblListItem);
			item.checked = (CheckBox) convertView.findViewById(R.id.ChildCheckBox);
			item.website = (TextView) convertView.findViewById(R.id.WebsiteLabel);

			convertView.setTag(item);

		}
		else
		{
			item = (ViewHolderItemWidget) convertView.getTag();

		}

		item.checked.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				CheckBox checkbox = (CheckBox) v;
				check_states.put(subscription.getId(), checkbox.isChecked());

			}
		});

		item.title.setText(subscription.getTitle());
		item.website.setText(subscription.getWebsite());

		if (check_states.containsKey(subscription.getId()))
		{
			item.checked.setChecked(check_states.get(subscription.getId()));
		}
		else
			item.checked.setChecked(false);

		return convertView;
	}
	
	private static class ViewHolderGroupWidget extends ViewHolderGroup
	{
		
	}
	
	private static class ViewHolderItemWidget extends ViewHolderItem
	{
		
		
	}


}
