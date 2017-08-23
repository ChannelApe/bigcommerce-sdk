package com.bigcommerce.catalog.models;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Address {

	@XmlElement(name = "id")
	private int id;

	@XmlElement(name = "first_name")
	private String firstName;

	@XmlElement(name = "last_name")
	private String lastName;

	@XmlElement(name = "company")
	private String company;

	@XmlElement(name = "street_1")
	private String street1;

	@XmlElement(name = "street_2")
	private String street2;

	@XmlElement(name = "city")
	private String city;

	@XmlElement(name = "state")
	private String state;

	@XmlElement(name = "zip")
	private String zip;

	@XmlElement(name = "country")
	private String country;

	@XmlElement(name = "country_iso2")
	private String countryIso2;

	@XmlElement(name = "phone")
	private String phone;

	@XmlElement(name = "email")
	private String email;

	@XmlElement(name = "items_total")
	private int itemsTotal;

	@XmlElement(name = "items_shipped")
	private int itemsShipped;

	@XmlElement(name = "shipping_method")
	private String shippingMethod;

	@XmlElement(name = "base_cost")
	private BigDecimal baseCost;

	@XmlElement(name = "base_cost_ex_tax")
	private BigDecimal baseCostExTax;

	@XmlElement(name = "base_cost_inc_tax")
	private BigDecimal baseCostIncTax;

	@XmlElement(name = "cost_tax")
	private BigDecimal costTax;

	@XmlElement(name = "cost_tax_class_id")
	private int costTaxClassId;

	@XmlElement(name = "base_handling_cost")
	private BigDecimal baseHandlingCost;

	@XmlElement(name = "handling_cost_ex_tax")
	private BigDecimal handlingCostExTax;

	@XmlElement(name = "handling_cost_inc_tax")
	private BigDecimal handlingCostIncTax;

	@XmlElement(name = "handling_cost_tax")
	private BigDecimal handlingCostTax;

	@XmlElement(name = "handling_cost_tax_class_id")
	private int handlingCostTaxClassId;

	@XmlElement(name = "shipping_zone_id")
	private int shippingZoneId;

	@XmlElement(name = "shipping_zone_name")
	private String shippingZoneName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getStreet1() {
		return street1;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountryIso2() {
		return countryIso2;
	}

	public void setCountryIso2(String countryIso2) {
		this.countryIso2 = countryIso2;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getItemsTotal() {
		return itemsTotal;
	}

	public void setItemsTotal(int itemsTotal) {
		this.itemsTotal = itemsTotal;
	}

	public int getItemsShipped() {
		return itemsShipped;
	}

	public void setItemsShipped(int itemsShipped) {
		this.itemsShipped = itemsShipped;
	}

	public String getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	public BigDecimal getBaseCost() {
		return baseCost;
	}

	public void setBaseCost(BigDecimal baseCost) {
		this.baseCost = baseCost;
	}

	public BigDecimal getBaseCostExTax() {
		return baseCostExTax;
	}

	public void setBaseCostExTax(BigDecimal baseCostExTax) {
		this.baseCostExTax = baseCostExTax;
	}

	public BigDecimal getBaseCostIncTax() {
		return baseCostIncTax;
	}

	public void setBaseCostIncTax(BigDecimal baseCostIncTax) {
		this.baseCostIncTax = baseCostIncTax;
	}

	public BigDecimal getCostTax() {
		return costTax;
	}

	public void setCostTax(BigDecimal costTax) {
		this.costTax = costTax;
	}

	public int getCostTaxClassId() {
		return costTaxClassId;
	}

	public void setCostTaxClassId(int costTaxClassId) {
		this.costTaxClassId = costTaxClassId;
	}

	public BigDecimal getBaseHandlingCost() {
		return baseHandlingCost;
	}

	public void setBaseHandlingCost(BigDecimal baseHandlingCost) {
		this.baseHandlingCost = baseHandlingCost;
	}

	public BigDecimal getHandlingCostExTax() {
		return handlingCostExTax;
	}

	public void setHandlingCostExTax(BigDecimal handlingCostExTax) {
		this.handlingCostExTax = handlingCostExTax;
	}

	public BigDecimal getHandlingCostIncTax() {
		return handlingCostIncTax;
	}

	public void setHandlingCostIncTax(BigDecimal handlingCostIncTax) {
		this.handlingCostIncTax = handlingCostIncTax;
	}

	public BigDecimal getHandlingCostTax() {
		return handlingCostTax;
	}

	public void setHandlingCostTax(BigDecimal handlingCostTax) {
		this.handlingCostTax = handlingCostTax;
	}

	public int getHandlingCostTaxClassId() {
		return handlingCostTaxClassId;
	}

	public void setHandlingCostTaxClassId(int handlingCostTaxClassId) {
		this.handlingCostTaxClassId = handlingCostTaxClassId;
	}

	public int getShippingZoneId() {
		return shippingZoneId;
	}

	public void setShippingZoneId(int shippingZoneId) {
		this.shippingZoneId = shippingZoneId;
	}

	public String getShippingZoneName() {
		return shippingZoneName;
	}

	public void setShippingZoneName(String shippingZoneName) {
		this.shippingZoneName = shippingZoneName;
	}

}
