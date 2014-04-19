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
@JsonPropertyOrder({ "id", "added", "sortid", "title", "updated", "categories",
		"visualUrl", "website" })
public class Subscriptions
{

	@JsonProperty("id")
	private String id;
	@JsonProperty("added")
	private Integer added;
	@JsonProperty("sortid")
	private String sortid;
	@JsonProperty("title")
	private String title;
	@JsonProperty("updated")
	private Integer updated;
	@JsonProperty("categories")
	private List<Object> categories = new ArrayList<Object>();
	@JsonProperty("visualUrl")
	private String visualUrl;
	@JsonProperty("website")
	private String website;
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

	@JsonProperty("added")
	public Integer getAdded()
	{
		return added;
	}

	@JsonProperty("added")
	public void setAdded(Integer added)
	{
		this.added = added;
	}

	@JsonProperty("sortid")
	public String getSortid()
	{
		return sortid;
	}

	@JsonProperty("sortid")
	public void setSortid(String sortid)
	{
		this.sortid = sortid;
	}

	@JsonProperty("title")
	public String getTitle()
	{
		return title;
	}

	@JsonProperty("title")
	public void setTitle(String title)
	{
		this.title = title;
	}

	@JsonProperty("updated")
	public Integer getUpdated()
	{
		return updated;
	}

	@JsonProperty("updated")
	public void setUpdated(Integer updated)
	{
		this.updated = updated;
	}

	@JsonProperty("categories")
	public List<Object> getCategories()
	{
		return categories;
	}

	@JsonProperty("categories")
	public void setCategories(List<Object> categories)
	{
		this.categories = categories;
	}

	@JsonProperty("visualUrl")
	public String getVisualUrl()
	{
		return visualUrl;
	}

	@JsonProperty("visualUrl")
	public void setVisualUrl(String visualUrl)
	{
		this.visualUrl = visualUrl;
	}

	@JsonProperty("website")
	public String getWebsite()
	{
		return website;
	}

	@JsonProperty("website")
	public void setWebsite(String website)
	{
		this.website = website;
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