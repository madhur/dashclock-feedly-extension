package in.co.madhur.dashclockfeedlyextension;

import java.util.ArrayList;
import java.util.HashMap;

import in.co.madhur.dashclockfeedlyextension.api.Category;
import in.co.madhur.dashclockfeedlyextension.api.FeedlyData;
import in.co.madhur.dashclockfeedlyextension.api.Subscription;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class FeedlyListViewAdapter extends BaseExpandableListAdapter implements ISaveable, Filterable

{
	private FeedlyData result;
	private Context context;
	private ViewHolderItem item;
	private ViewHolderGroup groupItem;
	private final HashMap<String, Boolean> check_states = new HashMap<String, Boolean>();


	public FeedlyListViewAdapter(FeedlyData result, Context context)
	{
		this.result = result;
		this.context = context;
		GetSelectedValuesFromPreferences();
		
	}

	@Override
	public int getGroupCount()
	{
		return result.getCategories().size();
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		Category category = result.getCategories().get(groupPosition);
		return category.getSubscriptions().size();

	}

	@Override
	public Object getGroup(int groupPosition)
	{
		return result.getCategories().get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		Category category = (Category) getGroup(groupPosition);
		return category.getSubscriptions().get(childPosition);

	}

	@Override
	public long getGroupId(int groupPosition)
	{
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return childPosition;
	}

	@Override
	public boolean hasStableIds()
	{
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		final Category category=(Category) getGroup(groupPosition);
		if (convertView == null)
		{
			LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);

			groupItem = new ViewHolderGroup();

			groupItem.textViewItem = (TextView) convertView.findViewById(R.id.lblListHeader);
			groupItem.checked = (CheckBox) convertView.findViewById(R.id.GroupCheckBox);
			
			convertView.setTag(groupItem);
		}
		else
		{

			groupItem = (ViewHolderGroup) convertView.getTag();
		}

		groupItem.checked.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				CheckBox checkbox=(CheckBox) v;
				check_states.put(category.getId(), checkbox.isChecked());
			}
		});
		
		groupItem.textViewItem.setText(category.getLabel());
		
		if(check_states.containsKey(category.getId()))
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
			convertView = infalInflater.inflate(R.layout.list_item, null);

			item = new ViewHolderItem();

			item.title = (TextView) convertView.findViewById(R.id.lblListItem);
			item.checked = (CheckBox) convertView.findViewById(R.id.ChildCheckBox);
			item.website = (TextView) convertView.findViewById(R.id.WebsiteLabel);

			convertView.setTag(item);

		}
		else
		{
			item = (ViewHolderItem) convertView.getTag();

		}
		
		item.checked.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				CheckBox checkbox=(CheckBox) v;
				check_states.put(subscription.getId(), checkbox.isChecked());
				
			}
		});

		item.title.setText(subscription.getTitle());
		item.website.setText(subscription.getWebsite());
		
		if(check_states.containsKey(subscription.getId()))
		{
			item.checked.setChecked(check_states.get(subscription.getId()));
		}
		else
			item.checked.setChecked(false);
		

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return true;
	}

	private final static class ViewHolderGroup
	{
		TextView textViewItem;
		CheckBox checked;
	}

	private final static class ViewHolderItem
	{
		TextView title;
		TextView website;
		CheckBox checked;
	}

	@Override
	public void SaveSelectedValuestoPreferences()
	{
		AppPreferences appPreferences=new AppPreferences(context);
		appPreferences.SaveSelectedValues(check_states);
		
	}

	@Override
	public void GetSelectedValuesFromPreferences()
	{
		AppPreferences appPreferences=new AppPreferences(context);
		ArrayList<String> selectedValues=appPreferences.GetSelectedValues();
		
		for(String s : selectedValues)
		{
			check_states.put(s, true);
		}
	}

	@Override
	public Filter getFilter()
	{
		return new Filter()
		{
			
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			protected FilterResults performFiltering(CharSequence constraint)
			{
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

}
