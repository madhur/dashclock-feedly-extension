package in.co.madhur.dashclockfeedlyextension.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FeedlyData
{
	private Profile profile;
	private List<Category> categories;
	private List<Subscription> subscriptions;
	private Map<Category, List<Subscription>> categorySubscriptions;
	private Markers markers;
	private String errorMessage;
	private boolean isError;
	
	
	public FeedlyData(String errorMessage)
	{
		
		setError(true);
		this.setErrorMessage(errorMessage);
	}
	
	public FeedlyData(Profile profile, List<Category> categories, List<Subscription> subscriptions, Map<Category, List<Subscription>> categorySubscriptions, Markers markers)
	{
		this.setProfile(profile);
		this.setCategories(categories);
		this.setSubscriptions(subscriptions);
		this.setCategorySubscriptions(categorySubscriptions);
		
		this.setMarkers(markers);
	}

	public Profile getProfile()
	{
		return profile;
	}

	public void setProfile(Profile profile)
	{
		this.profile = profile;
	}

	public List<Category> getCategories()
	{
		return categories;
	}

	public void setCategories(List<Category> categories)
	{
		this.categories = categories;
	}

	public List<Subscription> getSubscriptions()
	{
		return subscriptions;
	}

	public void setSubscriptions(List<Subscription> subscriptions)
	{
		this.subscriptions = subscriptions;
	}

	public Markers getMarkers()
	{
		return markers;
	}

	public void setMarkers(Markers markers)
	{
		this.markers = markers;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public boolean isError()
	{
		return isError;
	}

	public void setError(boolean isError)
	{
		this.isError = isError;
	}

	public Map<Category, List<Subscription>> getCategorySubscriptions()
	{
		return categorySubscriptions;
	}

	public void setCategorySubscriptions(Map<Category, List<Subscription>> categorySubscriptions2)
	{
		this.categorySubscriptions = categorySubscriptions2;
	}

}
