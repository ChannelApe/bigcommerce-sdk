package com.bigcommerce.catalog.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MetafieldResponse {
	private Metafield data;

	public Metafield getData() {
		return data;
	}

	public void setData(final Metafield data) {
		this.data = data;
	}

}
