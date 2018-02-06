package com.bigcommerce.catalog.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class ProductImage {

	private Integer id;

	@XmlElement(name = "image_url")
	private String imageUrl;

	@XmlElement(name = "is_thumbnail")
	private Boolean isThumbnail;

	@XmlElement(name = "product_id")
	private String productId;

	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(final String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Boolean getIsThumbnail() {
		return isThumbnail;
	}

	public void setIsThumbnail(final Boolean isThumbnail) {
		this.isThumbnail = isThumbnail;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(final String productId) {
		this.productId = productId;
	}
}
