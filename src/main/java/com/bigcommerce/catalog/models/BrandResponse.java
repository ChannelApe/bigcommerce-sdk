package com.bigcommerce.catalog.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BrandResponse {

	private Brand data;

	public Brand getData() {
		return data;
	}

	public void setData(Brand data) {
		this.data = data;
	}

}
