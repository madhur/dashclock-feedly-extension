package in.co.madhur.dashclockfeedlyextension.api;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "label" })
public class Category
{
	
	//private List<Subscription> subscriptions;

	@JsonProperty("id")
	private String id;
	@JsonProperty("label")
	private String label;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

	@JsonProperty("label")
	public String getLabel()
	{
		return label;
	}

	@JsonProperty("label")
	public void setLabel(String label)
	{
		this.label = label;
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

//	public List<Subscription> getSubscriptions()
//	{
//		return subscriptions;
//	}
//
//	public void setSubscriptions(List<Subscription> subscriptions)
//	{
//		this.subscriptions = subscriptions;
//	}

}