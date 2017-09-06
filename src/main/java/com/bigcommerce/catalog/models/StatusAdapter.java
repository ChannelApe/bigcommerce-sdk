package com.bigcommerce.catalog.models;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StatusAdapter extends XmlAdapter<String, Status> {

	@Override
	public Status unmarshal(String status) throws Exception {
		return Status.toEnum(status);
	}

	@Override
	public String marshal(Status status) throws Exception {
		return status.toString();
	}

}
