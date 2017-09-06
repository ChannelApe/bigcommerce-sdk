package com.bigcommerce.catalog.models;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Customer {

	@XmlElement(name = "first_name")
	private String firstName;

	@XmlElement(name = "last_name")
	private String lastName;

	@XmlElement(name = "email")
	private String email;

	@XmlElement(name = "id")
	private int id;

	@XmlElement(name = "company")
	private String company;

	@XmlElement(name = "phone")
	private String phone;

	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	@XmlElement(name = "date_created")
	private DateTime dateCreated;

	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	@XmlElement(name = "date_modified")
	private DateTime dateModified;

	@XmlElement(name = "store_credit")
	private BigDecimal storeCredit;

	@XmlElement(name = "registration_ip_address")
	private String registrationIpAddress;

	@XmlElement(name = "customer_group_id")
	private int customerGroupId;

	@XmlElement(name = "notes")
	private String notes;

	@XmlElement(name = "tax_exempt_category")
	private String taxExemptCategory;

	@XmlElement(name = "reset_pass_on_login")
	private String resetPassOnLogin;

	@XmlElement(name = "accepts_marketing")
	private String acceptsMarketing;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public BigDecimal getStoreCredit() {
		return storeCredit;
	}

	public void setStoreCredit(BigDecimal storeCredit) {
		this.storeCredit = storeCredit;
	}

	public int getCustomerGroupId() {
		return customerGroupId;
	}

	public void setCustomerGroupId(int customerGroupId) {
		this.customerGroupId = customerGroupId;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getTaxExemptCategory() {
		return taxExemptCategory;
	}

	public void setTaxExemptCategory(String taxExemptCategory) {
		this.taxExemptCategory = taxExemptCategory;
	}

	public String getResetPassOnLogin() {
		return resetPassOnLogin;
	}

	public void setResetPassOnLogin(String resetPassOnLogin) {
		this.resetPassOnLogin = resetPassOnLogin;
	}

	public String getAcceptsMarketing() {
		return acceptsMarketing;
	}

	public void setAcceptsMarketing(String acceptsMarketing) {
		this.acceptsMarketing = acceptsMarketing;
	}

	public String getRegistrationIpAddress() {
		return registrationIpAddress;
	}

	public void setRegistrationIpAddress(String registrationIpAddress) {
		this.registrationIpAddress = registrationIpAddress;
	}
}
