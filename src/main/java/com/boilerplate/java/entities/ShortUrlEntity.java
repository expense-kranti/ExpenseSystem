package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This is the class which define the short url related properties.
 * 
 * @author shiva
 *
 */
public class ShortUrlEntity extends BaseEntity implements Serializable {
	/**
	 * This is the url for which we create a uuid.
	 */
	private String longUrl;
	/**
	 * This is the short url corresponding to long url.
	 */
	private String shortUrl;
	/**
	 * This is the uuid corresponding to long url.
	 */
	private String uuid;

	/**
	 * This method get the long url value.
	 * 
	 * @return the longUrl
	 */
	public String getLongUrl() {
		return longUrl;
	}

	/**
	 * This method set the long url value.
	 * 
	 * @param longUrl
	 *            the longUrl to set
	 */
	public void setLongUrl(String longUrl) {
		this.longUrl = longUrl;
	}

	/**
	 * This method get the short url value.
	 * 
	 * @return the shortUrl
	 */
	public String getShortUrl() {
		return shortUrl;
	}

	/**
	 * This method set the short url value.
	 * 
	 * @param shortUrl
	 *            the shortUrl to set
	 */
	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	/**
	 * This method get the uuid value.
	 * 
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * This method set the uuid value.
	 * 
	 * @param uuid
	 *            the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * This is the source for which campaign is scheduled.
	 */
	private String campaignSource;

	/**
	 * This method get the campaignSource
	 * 
	 * @return the campaignSource
	 */
	public String getCampaignSource() {
		return campaignSource;
	}

	/**
	 * This method set the campaignSource
	 * 
	 * @param campaignSource
	 *            the campaignSource to set
	 */
	public void setCampaignSource(String campaignSource) {
		this.campaignSource = campaignSource;
	}

	/**
	 * This method get the publish template.
	 * 
	 * @return the publishTemplate
	 */
	public String getPublishTemplate() {
		return publishTemplate;
	}

	/**
	 * This method set the publish template.
	 * 
	 * @param publishTemplate
	 *            the publishTemplate to set
	 */
	public void setPublishTemplate(String publishTemplate) {
		this.publishTemplate = publishTemplate;
	}

	/**
	 * This is the template which is used to publish the click data.
	 */
	private String publishTemplate;

	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see BaseEntity.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see BaseEntity.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		// TODO Auto-generated method stub
		return null;
	}
}
