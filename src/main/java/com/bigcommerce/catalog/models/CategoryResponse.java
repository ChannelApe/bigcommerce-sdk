package com.bigcommerce.catalog.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CategoryResponse {
	private Category data;

	public Category getData() {
		return data;
	}

	public void setData(final Category data) {
		this.data = data;
	}
}
