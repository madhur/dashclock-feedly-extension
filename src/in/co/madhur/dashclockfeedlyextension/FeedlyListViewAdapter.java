package in.co.madhur.dashclockfeedlyextension;

import in.co.madhur.dashclockfeedlyextension.api.Category;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

public class FeedlyListViewAdapter extends BaseExpandableListAdapter
{
	private FeedlyData result;

	public FeedlyListViewAdapter(FeedlyData result)
	{
		this.result=result;
	}

	@Override
	public int getGroupCount()
	{
		return result.getCategories().size();
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		Category category=result.getCategories().get(groupPosition);
		
		
	}

	@Override
	public Object getGroup(int groupPosition)
	{
		return result.getCategories().get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		Category category=getGroup(groupPosition);
		
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds()
	{
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
