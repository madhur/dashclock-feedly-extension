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
		//return dbHelper.GetSubScriptionsForCategory(category.getId()).size();
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
		//return dbHelper.GetSubScriptionsForCategory(category.getId()).get(childPosition);
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
		}

		TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
		CheckBox groupCheckBox = (CheckBox) convertView.findViewById(R.id.GroupCheckBox);
		groupCheckBox.setOnClickListener(this);

		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		final String childText = ((Subscription) getChild(groupPosition, childPosition)).getTitle();

		if (convertView == null)
		{
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item, null);
		}

		TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
		CheckBox childCheckBox = (CheckBox) convertView.findViewById(R.id.ChildCheckBox);
		childCheckBox.setOnClickListener(this);

		txtListChild.setText(childText);
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

}
