package in.co.madhur.dashclockfeedlyextension.api;

import java.util.List;

import retrofit.http.GET;

public interface FeedlyApi
{
	@GET("/v3/categories")
	List<Category> GetCategories();
	
	@GET("/v3/markers/counts")
	Markers GetMarkerCounts();

}
