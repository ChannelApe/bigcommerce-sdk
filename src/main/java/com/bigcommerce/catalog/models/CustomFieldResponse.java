package com.bigcommerce.catalog.models;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement
public class CustomFieldResponse {
	private CustomField data;

	@XmlTransient
	private Meta meta = new Meta();

	public CustomField getData() {
		return data;
	}

	public void setData(CustomField data) {
		this.data = data;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}
}
