package com.bigcommerce.catalog.models;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement
public class CustomFieldResponse {
	private List<CustomField> data = new LinkedList<>();
	private Meta meta = new Meta();

	public List<CustomField> getData() {
		return data;
	}

	public void setData(List<CustomField> data) {
		this.data = data;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}
}
