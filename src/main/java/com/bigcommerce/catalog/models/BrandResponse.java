package com.bigcommerce.catalog.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BrandResponse {

	private Brand data;

	private Meta meta = new Meta();

	public Brand getData() {
		return data;
	}

	public void setData(Brand data) {
		this.data = data;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}
}
