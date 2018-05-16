package com.bigcommerce.catalog.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CategoryResponse {
	private Category data;

	private Meta meta;

	public Category getData() {
		return data;
	}

	public void setData(final Category data) {
		this.data = data;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}
}
