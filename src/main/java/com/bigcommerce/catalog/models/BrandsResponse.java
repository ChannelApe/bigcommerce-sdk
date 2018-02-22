package com.bigcommerce.catalog.models;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BrandsResponse {
	private List<Brand> data = new LinkedList<>();
	private Meta meta = new Meta();

	public List<Brand> getData() {
		return data;
	}

	public void setData(List<Brand> data) {
		this.data = data;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}
}
