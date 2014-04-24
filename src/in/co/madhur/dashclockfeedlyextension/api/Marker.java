package in.co.madhur.dashclockfeedlyextension.api;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "count", "id", "updated" })
public class Marker
{

	@JsonProperty("count")
	private int count;
	@JsonProperty("id")
	private String id;
	@JsonProperty("updated")
	private Long updated;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("count")
	public int getCount()
	{
		return count;
	}

	@JsonProperty("count")
	public void setCount(int count)
	{
		this.count = count;
	}

	@JsonProperty("id")
	public String getId()
	{
		return id;
	}

	@JsonProperty("id")
	public void setId(String id)
	{
		this.id = id;
	}

	@JsonProperty("updated")
	public Long getUpdated()
	{
		return updated;
	}

	@JsonProperty("updated")
	public void setUpdated(Long updated)
	{
		this.updated = updated;
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