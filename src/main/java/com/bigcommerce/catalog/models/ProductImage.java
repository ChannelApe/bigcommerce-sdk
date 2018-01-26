package com.bigcommerce.catalog.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductImage {

	@XmlElement(name = "image_url")
	private String imageUrl;

	@XmlElement(name = "is_thumbnail")
	private Boolean isThumbnail;

	@XmlElement(name = "product_id")
	private Integer productId;

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

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(final Integer productId) {
		this.productId = productId;
	}
}
