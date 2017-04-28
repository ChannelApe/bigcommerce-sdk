package com.bigcommerce.catalog.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class VariantResponse {

	private Variant data;

	public Variant getData() {
		return data;
	}

	public void setData(Variant data) {
		this.data = data;
	}

}
