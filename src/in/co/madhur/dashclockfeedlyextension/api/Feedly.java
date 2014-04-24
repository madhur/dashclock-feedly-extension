package in.co.madhur.dashclockfeedlyextension.api;

import java.util.List;

import in.co.madhur.dashclockfeedlyextension.Consts;
import retrofit.RestAdapter;

public class Feedly
{
	private static FeedlyApi feedlyApi;
	private static String token;
	private static Feedly feedly;
	
	private Feedly()
	{

	}

	public static Feedly getInstance(String token)
	{

		if (feedly == null)
		{
			RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Consts.FEEDLY_API_URL).build();

			feedlyApi = restAdapter.create(FeedlyApi.class);
			feedly=new Feedly();
			
			Feedly.token=token;

			return feedly;
		}
		else
			return feedly;

	}
	
	public List<Category> GetCategories()
	{
		
		return feedlyApi.GetCategories(token);
	}
	
	public Markers GetUnreadCounts()
	{
		
		return feedlyApi.GetMarkerCounts(token);
	}
	
	public Profile GetProfile()
	{
		return feedlyApi.GetProfile(token);
	}
	
	public List<Subscription> GetSubscriptions()
	{
		return feedlyApi.GetSubscriptions(token);
	}

}
