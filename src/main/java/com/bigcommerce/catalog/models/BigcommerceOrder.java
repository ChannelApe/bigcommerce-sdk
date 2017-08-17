package com.bigcommerce.catalog.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BigcommerceOrder {

	private int id;
	private String status;

	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	@XmlElement(name = "date_created")
	private DateTime dateCreated;

	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	@XmlElement(name = "date_modified")
	private DateTime dateModified;

	private String subtotalExTax;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public DateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(DateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public DateTime getDateModified() {
		return dateModified;
	}

	public void setDateModified(DateTime dateModified) {
		this.dateModified = dateModified;
	}

	public String getSubtotalExTax() {
		return subtotalExTax;
	}

	public void setSubtotalExTax(String subtotalExTax) {
		this.subtotalExTax = subtotalExTax;
	}

}
