package com.bigcommerce.catalog.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProductImageResponse {

	private ProductImage data;

	public ProductImage getData() {
		return data;
	}

	public void setData(final ProductImage data) {
		this.data = data;
	}

}
