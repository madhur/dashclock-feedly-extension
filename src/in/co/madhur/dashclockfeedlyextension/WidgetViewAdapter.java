package in.co.madhur.dashclockfeedlyextension;

import java.util.ArrayList;

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
			groupItem.selectedChildCount = (TextView) convertView.findViewById(R.id.SelectedCountTextView);

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
				if (checkbox.isChecked())
				{
					check_states.put(category.getId(), true);
				}
				else
					check_states.remove(category.getId());
				
				notifyDataSetChanged();
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

		HighlightGroup(groupPosition);

		return convertView;
	}

	private void HighlightGroup(int groupPosition)
	{
		int selectedChild = 0;
		for (int i = 0; i < getChildrenCount(groupPosition); ++i)
		{
			Subscription sub = (Subscription) getChild(groupPosition, i);
			if (check_states.containsKey(sub.getId()))
			{
				selectedChild++;
			}

		}

		if (selectedChild != 0)
		{
			groupItem.selectedChildCount.setVisibility(View.VISIBLE);
			groupItem.selectedChildCount.setText("("
					+ String.valueOf(selectedChild) + ")");

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
				if(checkbox.isChecked())
					check_states.put(subscription.getId(), true);
				else
					check_states.remove(subscription.getId());
				
				notifyDataSetChanged();

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

	@Override
	public void SaveSelectedValuestoPreferences()
	{
		AppPreferences appPreferences = new AppPreferences(context);
		appPreferences.SaveSelectedValuesWidgets(check_states);

	}

	@Override
	public void GetSelectedValuesFromPreferences()
	{
		check_states.clear();

		AppPreferences appPreferences = new AppPreferences(context);
		ArrayList<String> selectedValues = appPreferences.GetSelectedValuesWidgets();

		for (String s : selectedValues)
		{
			check_states.put(s, true);
		}

	}

}
