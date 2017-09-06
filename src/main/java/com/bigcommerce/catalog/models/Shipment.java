package com.bigcommerce.catalog.models;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class Shipment {

	@XmlElement(name = "id")
	private String id;

	@XmlElement(name = "shipping_provider")
	private String shippingProvider;

	@XmlElement(name = "shipping_method")
	private String shippingMethod;

	@XmlElement(name = "tracking_number")
	private String trackingNumber;

	@XmlElement(name = "items")
	private List<ShipmentLineItem> items;

	@XmlElement(name = "order_address_id")
	private Integer orderAddressId;

	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	@XmlElement(name = "date_created")
	private DateTime dateCreated;

	@XmlElement(name = "comments")
	private String comments;

	@XmlElement(name = "tracking_carrier")
	private String trackingCarrier;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShippingProvider() {
		return shippingProvider;
	}

	public void setShippingProvider(String shippingProvider) {
		this.shippingProvider = shippingProvider;
	}

	public String getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public List<ShipmentLineItem> getItems() {
		return items;
	}

	public void setItems(List<ShipmentLineItem> items) {
		this.items = items;
	}

	public Integer getOrderAddressId() {
		return orderAddressId;
	}

	public void setOrderAddressId(Integer orderAddressId) {
		this.orderAddressId = orderAddressId;
	}

	public DateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(DateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getTrackingCarrier() {
		return trackingCarrier;
	}

	public void setTrackingCarrier(String trackingCarrier) {
		this.trackingCarrier = trackingCarrier;
	}

}
