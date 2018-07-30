package com.bigcommerce.catalog.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class VariantResponse {

	private Variant data;

	public Variant getData() {
		return data;
	}

	public void setData(Variant data) {
		this.data = data;
	}

}
