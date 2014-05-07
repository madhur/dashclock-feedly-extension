package in.co.madhur.dashclockfeedlyextension;

import java.util.HashMap;

import in.co.madhur.dashclockfeedlyextension.api.Category;
import in.co.madhur.dashclockfeedlyextension.api.Subscription;
import in.co.madhur.dashclockfeedlyextension.db.DbHelper;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class FeedlyListViewAdapter extends BaseExpandableListAdapter implements
		OnClickListener
{
	private FeedlyData result;
	private DbHelper dbHelper;
	private Context context;
	ViewHolderItem item;
	ViewHolderGroup groupItem;

	// Hashmap for keeping track of our checkbox check states
	private HashMap<Integer, boolean[]> mChildCheckStates;

	// Our getChildView & getGroupView use the viewholder patter
	// Here are the viewholders defined, the inner classes are
	// at the bottom
	private ChildViewHolder childViewHolder;
	private GroupViewHolder groupViewHolder;

	public FeedlyListViewAdapter(FeedlyData result, Context context)
	{
		this.result = result;
		dbHelper = DbHelper.getInstance(context);
		this.context = context;

		// Initialize our hashmap containing our check states here
		mChildCheckStates = new HashMap<Integer, boolean[]>();
	}

	@Override
	public int getGroupCount()
	{
		// Log.v(App.TAG, String.valueOf(result.getCategories().size()));
		return result.getCategories().size();
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		Category category = result.getCategories().get(groupPosition);
		// return dbHelper.GetSubScriptionsForCategory(category.getId()).size();
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
		// return
		// dbHelper.GetSubScriptionsForCategory(category.getId()).get(childPosition);
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
		String headerTitle = ((Category) getGroup(groupPosition)).getLabel();
		if (convertView == null)
		{
			LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);
			
			groupItem=new ViewHolderGroup();
			
			groupItem.textViewItem=(TextView) convertView.findViewById(R.id.lblListHeader);
			groupItem.checked=(CheckBox) convertView.findViewById(R.id.GroupCheckBox);
			
			convertView.setTag(groupItem);
		}
		else
		{
			
			groupItem=(ViewHolderGroup) convertView.getTag();
		}

		//groupCheckBox.setOnClickListener(this);

		//groupItem.textViewItem.setTypeface(null, Typeface.BOLD);
		groupItem.textViewItem.setText(headerTitle);

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		// final String childText = ((Subscription) getChild(groupPosition,
		// childPosition)).getTitle();
		Subscription subscription = (Subscription) getChild(groupPosition, childPosition);

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

		// childCheckBox.setOnClickListener(this);

		item.title.setText(subscription.getTitle());
		item.website.setText(subscription.getWebsite());

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return true;
	}

	@Override
	public void onClick(View v)
	{

	}

	public final class GroupViewHolder
	{

		TextView mGroupText;
	}

	public final class ChildViewHolder
	{

		TextView mChildText;
		CheckBox mCheckBox;
	}

	 static class ViewHolderGroup
	{
		TextView textViewItem;
		CheckBox checked;
	}

	 static class ViewHolderItem
	{
		TextView title;
		TextView website;
		CheckBox checked;
	}

}
