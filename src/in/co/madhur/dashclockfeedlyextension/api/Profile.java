package in.co.madhur.dashclockfeedlyextension.api;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "client", "wave", "email", "familyName",
		"givenName", "google", "gender", "picture", "paymentProviderId",
		"paymentSubscriptionId", "created", "windowsLiveConnected",
		"facebookConnected", "dropboxConnected", "wordPressConnected",
		"evernoteConnected", "pocketConnected", "twitterConnected", "fullName" })
public class Profile
{

	@JsonProperty("id")
	private String id;
	@JsonProperty("client")
	private String client;
	@JsonProperty("wave")
	private String wave;
	@JsonProperty("email")
	private String email;
	@JsonProperty("familyName")
	private String familyName;
	@JsonProperty("givenName")
	private String givenName;
	@JsonProperty("google")
	private String google;
	@JsonProperty("gender")
	private String gender;
	@JsonProperty("picture")
	private String picture;
	@JsonProperty("paymentProviderId")
	private PaymentProviderId paymentProviderId;
	@JsonProperty("paymentSubscriptionId")
	private PaymentSubscriptionId paymentSubscriptionId;
	@JsonProperty("created")
	private Integer created;
	@JsonProperty("windowsLiveConnected")
	private Boolean windowsLiveConnected;
	@JsonProperty("facebookConnected")
	private Boolean facebookConnected;
	@JsonProperty("dropboxConnected")
	private Boolean dropboxConnected;
	@JsonProperty("wordPressConnected")
	private Boolean wordPressConnected;
	@JsonProperty("evernoteConnected")
	private Boolean evernoteConnected;
	@JsonProperty("pocketConnected")
	private Boolean pocketConnected;
	@JsonProperty("twitterConnected")
	private Boolean twitterConnected;
	@JsonProperty("fullName")
	private String fullName;
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

	@JsonProperty("client")
	public String getClient()
	{
		return client;
	}

	@JsonProperty("client")
	public void setClient(String client)
	{
		this.client = client;
	}

	@JsonProperty("wave")
	public String getWave()
	{
		return wave;
	}

	@JsonProperty("wave")
	public void setWave(String wave)
	{
		this.wave = wave;
	}

	@JsonProperty("email")
	public String getEmail()
	{
		return email;
	}

	@JsonProperty("email")
	public void setEmail(String email)
	{
		this.email = email;
	}

	@JsonProperty("familyName")
	public String getFamilyName()
	{
		return familyName;
	}

	@JsonProperty("familyName")
	public void setFamilyName(String familyName)
	{
		this.familyName = familyName;
	}

	@JsonProperty("givenName")
	public String getGivenName()
	{
		return givenName;
	}

	@JsonProperty("givenName")
	public void setGivenName(String givenName)
	{
		this.givenName = givenName;
	}

	@JsonProperty("google")
	public String getGoogle()
	{
		return google;
	}

	@JsonProperty("google")
	public void setGoogle(String google)
	{
		this.google = google;
	}

	@JsonProperty("gender")
	public String getGender()
	{
		return gender;
	}

	@JsonProperty("gender")
	public void setGender(String gender)
	{
		this.gender = gender;
	}

	@JsonProperty("picture")
	public String getPicture()
	{
		return picture;
	}

	@JsonProperty("picture")
	public void setPicture(String picture)
	{
		this.picture = picture;
	}

	@JsonProperty("paymentProviderId")
	public PaymentProviderId getPaymentProviderId()
	{
		return paymentProviderId;
	}

	@JsonProperty("paymentProviderId")
	public void setPaymentProviderId(PaymentProviderId paymentProviderId)
	{
		this.paymentProviderId = paymentProviderId;
	}

	@JsonProperty("paymentSubscriptionId")
	public PaymentSubscriptionId getPaymentSubscriptionId()
	{
		return paymentSubscriptionId;
	}

	@JsonProperty("paymentSubscriptionId")
	public void setPaymentSubscriptionId(PaymentSubscriptionId paymentSubscriptionId)
	{
		this.paymentSubscriptionId = paymentSubscriptionId;
	}

	@JsonProperty("created")
	public Integer getCreated()
	{
		return created;
	}

	@JsonProperty("created")
	public void setCreated(Integer created)
	{
		this.created = created;
	}

	@JsonProperty("windowsLiveConnected")
	public Boolean getWindowsLiveConnected()
	{
		return windowsLiveConnected;
	}

	@JsonProperty("windowsLiveConnected")
	public void setWindowsLiveConnected(Boolean windowsLiveConnected)
	{
		this.windowsLiveConnected = windowsLiveConnected;
	}

	@JsonProperty("facebookConnected")
	public Boolean getFacebookConnected()
	{
		return facebookConnected;
	}

	@JsonProperty("facebookConnected")
	public void setFacebookConnected(Boolean facebookConnected)
	{
		this.facebookConnected = facebookConnected;
	}

	@JsonProperty("dropboxConnected")
	public Boolean getDropboxConnected()
	{
		return dropboxConnected;
	}

	@JsonProperty("dropboxConnected")
	public void setDropboxConnected(Boolean dropboxConnected)
	{
		this.dropboxConnected = dropboxConnected;
	}

	@JsonProperty("wordPressConnected")
	public Boolean getWordPressConnected()
	{
		return wordPressConnected;
	}

	@JsonProperty("wordPressConnected")
	public void setWordPressConnected(Boolean wordPressConnected)
	{
		this.wordPressConnected = wordPressConnected;
	}

	@JsonProperty("evernoteConnected")
	public Boolean getEvernoteConnected()
	{
		return evernoteConnected;
	}

	@JsonProperty("evernoteConnected")
	public void setEvernoteConnected(Boolean evernoteConnected)
	{
		this.evernoteConnected = evernoteConnected;
	}

	@JsonProperty("pocketConnected")
	public Boolean getPocketConnected()
	{
		return pocketConnected;
	}

	@JsonProperty("pocketConnected")
	public void setPocketConnected(Boolean pocketConnected)
	{
		this.pocketConnected = pocketConnected;
	}

	@JsonProperty("twitterConnected")
	public Boolean getTwitterConnected()
	{
		return twitterConnected;
	}

	@JsonProperty("twitterConnected")
	public void setTwitterConnected(Boolean twitterConnected)
	{
		this.twitterConnected = twitterConnected;
	}

	@JsonProperty("fullName")
	public String getFullName()
	{
		return fullName;
	}

	@JsonProperty("fullName")
	public void setFullName(String fullName)
	{
		this.fullName = fullName;
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
