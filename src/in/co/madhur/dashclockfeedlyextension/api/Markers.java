package in.co.madhur.dashclockfeedlyextension.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "unreadcounts" })
public class Markers
{

	@JsonProperty("unreadcounts")
	private List<Marker> unreadcounts = new ArrayList<Marker>();
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("unreadcounts")
	public List<Marker> getUnreadcounts()
	{
		return unreadcounts;
	}

	@JsonProperty("unreadcounts")
	public void setUnreadcounts(List<Marker> unreadcounts)
	{
		this.unreadcounts = unreadcounts;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties()
	{
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value)
	{
		this.additionalProperties.put(name, value);
	}

}