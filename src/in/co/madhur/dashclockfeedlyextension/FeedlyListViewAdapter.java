package in.co.madhur.dashclockfeedlyextension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.co.madhur.dashclockfeedlyextension.api.Category;
import in.co.madhur.dashclockfeedlyextension.api.FeedlyData;
import in.co.madhur.dashclockfeedlyextension.api.Subscription;
import android.content.Context;
import android.util.Log;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public abstract class FeedlyListViewAdapter extends BaseExpandableListAdapter implements
		ISaveable, Filterable

{
	private List<Category> categories, originalCategories;
	private List<Subscription> subscriptions, originalSubs;
	private Map<Category, List<Subscription>> categorySubscriptions,
			originalCategorySubscriptions;
	private ArrayList<Category> blank = new ArrayList<Category>();
	private ArrayList<Subscription> blankSub = new ArrayList<Subscription>();
	protected Context context;
	protected final HashMap<String, Boolean> check_states = new HashMap<String, Boolean>();
	private FeedItemsFilter feedItemsFilter;

	public FeedlyListViewAdapter(FeedlyData result, Context context)
	{
		this.setCategories(result.getCategories());
		this.setSubscriptions(result.getSubscriptions());
		this.setCategorySubscriptions(result.getCategorySubscriptions());

		originalCategories = new ArrayList<Category>();
		originalSubs = new ArrayList<Subscription>();
		originalCategorySubscriptions = new HashMap<Category, List<Subscription>>();

		for (Category category : getCategories())
		{
			originalCategories.add(category);
			originalCategorySubscriptions.put(category, categorySubscriptions.get(category));
		}
		for (Subscription sub : getSubscriptions())
		{

			originalSubs.add(sub);
		}

		this.context = context;
		GetSelectedValuesFromPreferences();

	}

	@Override
	public int getGroupCount()
	{
		return getCategories().size();
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		Category category = getCategories().get(groupPosition);
		// return category.getSubscriptions().size();
		return categorySubscriptions.get(category).size();

	}

	@Override
	public Object getGroup(int groupPosition)
	{
		return getCategories().get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		Category category = (Category) getGroup(groupPosition);
		return categorySubscriptions.get(category).get(childPosition);
		

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
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return true;
	}

	protected abstract static class ViewHolderGroup
	{
		TextView textViewItem;
		CheckBox checked;
	}

	protected abstract static class ViewHolderItem
	{
		TextView title;
		TextView website;
		CheckBox checked;
	}

	@Override
	public abstract void SaveSelectedValuestoPreferences();

	@Override
	public abstract void GetSelectedValuesFromPreferences();
//	{
//		AppPreferences appPreferences = new AppPreferences(context);
//		ArrayList<String> selectedValues = appPreferences.GetSelectedValues();
//
//		for (String s : selectedValues)
//		{
//			check_states.put(s, true);
//		}
//	}

	@Override
	public Filter getFilter()
	{
		if(feedItemsFilter==null)
		{
			feedItemsFilter=new FeedItemsFilter();
		}
		
		return feedItemsFilter;
		
	}
	
	public void selectNone()
	{
		check_states.clear();
		notifyDataSetChanged();
	}
	
	public void selectAll()
	{
		for(Category category: getCategories())
		{
			check_states.put(category.getId(), true);
			
			for(Subscription sub: categorySubscriptions.get(category))
			{
				check_states.put(sub.getId(), true);
			}
			
		}
		
		notifyDataSetChanged();
		
	}
	
	public void selectAllFeeds()
	{
		selectNone();
		for(Category category: getCategories())
		{
			
			for(Subscription sub: categorySubscriptions.get(category))
			{
				check_states.put(sub.getId(), true);
			}
			
		}
		
		notifyDataSetChanged();
		
	}
	
	public void selectAllCategories()
	{
		selectNone();
		for(Category category: getCategories())
		{
			check_states.put(category.getId(), true);
		}
		
		notifyDataSetChanged();
		
	}

	private List<Category> getCategories()
	{
		return categories;
	}

	private void setCategories(List<Category> categories)
	{
		this.categories = categories;
	}

	private List<Subscription> getSubscriptions()
	{
		return subscriptions;
	}

	private void setSubscriptions(List<Subscription> subscriptions)
	{
		this.subscriptions = subscriptions;
	}

	private void setCategorySubscriptions(Map<Category, List<Subscription>> categorySubscriptions)
	{
		this.categorySubscriptions = categorySubscriptions;
	}
	
	private class FeedItemsFilter extends Filter
	{
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results)
		{

			FeedlyData filteredData = (FeedlyData) results.values;

			if (results.count > 0)
			{
				Log.println(Log.INFO, "Results", "FOUND");
				categories = filteredData.getCategories();
				subscriptions = filteredData.getSubscriptions();
				categorySubscriptions = filteredData.getCategorySubscriptions();

				notifyDataSetChanged();
			}
			else
			{
				Log.println(Log.INFO, "Results", "-");
				categories = blank;
				subscriptions = blankSub;
				notifyDataSetInvalidated();
			}
		}

		@Override
		protected FilterResults performFiltering(CharSequence constraint)
		{
			FilterResults results = new FilterResults();

			ArrayList<Subscription> filteredsubs = new ArrayList<Subscription>();
			ArrayList<Category> filteredcategories = new ArrayList<Category>();

			Map<Category, List<Subscription>> filteredCategorySubscriptions = new HashMap<Category, List<Subscription>>();

			if (constraint != null && constraint.length() > 0)
			{

				for (Category category : originalCategories)
				{
					ArrayList<Subscription> filteredCategorySubs = new ArrayList<Subscription>();

					for (Subscription sub : originalCategorySubscriptions.get(category))
					{

						if (sub.getTitle().trim().toLowerCase().contains(constraint.toString().trim().toLowerCase())
								|| category.getLabel().trim().toLowerCase().contains(constraint.toString().trim().toLowerCase()))
						{
							if (!filteredcategories.contains(category))
							{
								filteredcategories.add(category);
							}

							filteredsubs.add(sub);

							filteredCategorySubs.add(sub);

							filteredCategorySubscriptions.put(category, filteredCategorySubs);

						}
					}

				}
			}
			else
			{

				filteredcategories = (ArrayList<Category>) originalCategories;
				filteredsubs = (ArrayList<Subscription>) originalSubs;
				filteredCategorySubscriptions = originalCategorySubscriptions;
			}

			FeedlyData filteredData = new FeedlyData(null, filteredcategories, filteredsubs, filteredCategorySubscriptions, null);

			synchronized (this)
			{
				results.values = filteredData;
				results.count = filteredsubs.size();
			}

			return results;

		}

		
		
	}

}
