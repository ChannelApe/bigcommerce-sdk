package com.bigcommerce.catalog.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProductResponse {
	private Product data;

	public Product getData() {
		return data;
	}

	public void setData(final Product data) {
		this.data = data;
	}
}
