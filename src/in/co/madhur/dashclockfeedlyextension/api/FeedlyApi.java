package in.co.madhur.dashclockfeedlyextension.api;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Header;

public interface FeedlyApi
{
	@GET("/v3/categories")
	List<Category> GetCategories(@Header("Authorization") String authorization);
	
	@GET("/v3/markers/counts")
	Markers GetMarkerCounts(@Header("Authorization") String authorization);
	
	@GET("/v3/subscriptions")
	List<Subscriptions> GetSubscriptions(@Header("Authorization") String authorization);

}
