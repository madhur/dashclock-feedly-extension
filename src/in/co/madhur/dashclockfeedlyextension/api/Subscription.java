package in.co.madhur.dashclockfeedlyextension.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "added", "sortid", "title", "updated", "categories",
		"visualUrl", "website" })
public class Subscription
{

	@JsonProperty("id")
	private String id;
	@JsonProperty("added")
	private Long added;
	@JsonProperty("sortid")
	private String sortid;
	@JsonProperty("title")
	private String title;
	@JsonProperty("updated")
	private Long updated;
	@JsonProperty("categories")
	private List<Category> categories = new ArrayList<Category>();
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
	public Long getAdded()
	{
		return added;
	}

	@JsonProperty("added")
	public void setAdded(Long added)
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
	public Long getUpdated()
	{
		// Updated can be absent sometimes in json
		if(updated==null)
			return (long) 0;
		return updated;
	}

	@JsonProperty("updated")
	public void setUpdated(Long updated)
	{
		this.updated = updated;
	}

	@JsonProperty("categories")
	public List<Category> getCategories()
	{
		return categories;
	}

	@JsonProperty("categories")
	public void setCategories(List<Category> categories)
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
		if(!TextUtils.isEmpty(website))
			return website;
		else
			return "";
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