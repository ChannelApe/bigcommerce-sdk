package com.bigcommerce.catalog.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CatalogSummaryResponse {

	private CatalogSummary data;

	public CatalogSummary getData() {
		return data;
	}

	public void setData(CatalogSummary data) {
		this.data = data;
	}

}
