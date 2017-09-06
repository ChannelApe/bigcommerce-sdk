package com.bigcommerce.catalog.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderStatus {

	@XmlElement(name = "id")
	private Integer id;

	@XmlElement(name = "name")
	private String name;

	@XmlElement(name = "system_label")
	private String systemLabel;

	@XmlElement(name = "custom_label")
	private String customLabel;

	@XmlElement(name = "system_description")
	private String systemDescription;

	@XmlElement(name = "order")
	private int order;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSystemLabel() {
		return systemLabel;
	}

	public void setSystemLabel(String systemLabel) {
		this.systemLabel = systemLabel;
	}

	public String getCustomLabel() {
		return customLabel;
	}

	public void setCustomLabel(String customLabel) {
		this.customLabel = customLabel;
	}

	public String getSystemDescription() {
		return systemDescription;
	}

	public void setSystemDescription(String systemDescription) {
		this.systemDescription = systemDescription;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}
