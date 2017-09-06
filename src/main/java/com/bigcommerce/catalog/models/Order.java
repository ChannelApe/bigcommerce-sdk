package com.bigcommerce.catalog.models;

import java.math.BigDecimal;

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
public class Order {

	private Integer id;

	@XmlJavaTypeAdapter(StatusAdapter.class)
	@XmlElement(name = "status")
	private Status status;

	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	@XmlElement(name = "date_created")
	private DateTime dateCreated;

	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	@XmlElement(name = "date_modified")
	private DateTime dateModified;

	@XmlElement(name = "subtotal_ex_tax")
	private BigDecimal subtotalExTax;

	@XmlElement(name = "total_inc_tax")
	private BigDecimal totalIncTax;

	@XmlElement(name = "subtotal_inc_tax")
	private BigDecimal subtotalIncTax;

	@XmlElement(name = "shipping_cost_inc_tax")
	private BigDecimal shippingCostIncTax;

	@XmlElement(name = "shipping_cost_tax")
	private BigDecimal shippingCostTax;

	@XmlElement(name = "subtotal_tax")
	private BigDecimal subtotalTax;

	@XmlElement(name = "total_tax")
	private BigDecimal totalTax;

	@XmlElement(name = "currency_code")
	private String currencyCode;

	@XmlElement(name = "customer_id")
	private Integer customerId;

	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	@XmlElement(name = "date_shipped")
	private DateTime dateShipped;

	@XmlElement(name = "status_id")
	private Integer statusId;

	@XmlElement(name = "base_shipping_cost")
	private BigDecimal baseShippingCost;

	@XmlElement(name = "shipping_cost_ex_tax")
	private BigDecimal shippingCostExTax;

	@XmlElement(name = "shipping_cost_tax_class_id")
	private Integer shippingCostTaxClassId;

	@XmlElement(name = "base_handling_cost")
	private BigDecimal baseHandlingCost;

	@XmlElement(name = "handling_cost_ex_tax")
	private BigDecimal handlingCostExTax;

	@XmlElement(name = "handling_cost_inc_tax")
	private BigDecimal handlingCostIncTax;

	@XmlElement(name = "handling_cost_tax")
	private BigDecimal handlingCostTax;

	@XmlElement(name = "base_wrapping_cost")
	private BigDecimal baseWrappingCost;

	@XmlElement(name = "handling_cost_tax_class_id")
	private Integer handlingCostTaxClassId;

	@XmlElement(name = "wrapping_cost_ex_tax")
	private BigDecimal wrappingCostExTax;

	@XmlElement(name = "wrapping_cost_inc_tax")
	private BigDecimal wrappingCostIncTax;

	@XmlElement(name = "wrapping_cost_tax")
	private BigDecimal wrappingCostTax;

	@XmlElement(name = "wrapping_cost_tax_class_id")
	private Integer wrappingCostTaxClassId;

	@XmlElement(name = "total_ex_tax")
	private BigDecimal totalExTax;

	@XmlElement(name = "items_total")
	private Integer itemsTotal;

	@XmlElement(name = "items_shipped")
	private String itemsShipped;

	@XmlElement(name = "payment_method")
	private String paymentMethod;

	@XmlElement(name = "payment_provider_id")
	private String paymentProviderId;

	@XmlElement(name = "payment_status")
	private String paymentStatus;

	@XmlElement(name = "refunded_amount")
	private BigDecimal refundedAmount;

	@XmlElement(name = "order_is_digital")
	private String orderIsDigital;

	@XmlElement(name = "store_credit_amount")
	private BigDecimal storeCreditAmount;

	@XmlElement(name = "gift_certificate_amount")
	private BigDecimal giftCertificateAmount;

	@XmlElement(name = "ip_address")
	private String ipAddress;

	@XmlElement(name = "geoip_country")
	private String geoipCountry;

	@XmlElement(name = "geoip_country_iso2")
	private String geoipCountryIso2;

	@XmlElement(name = "currency_id")
	private Integer currencyId;

	@XmlElement(name = "currency_exchange_rate")
	private String currencyExchangeRate;

	@XmlElement(name = "default_currency_id")
	private String defaultCurrencyId;

	@XmlElement(name = "default_currency_code")
	private String defaultCurrencyCode;

	@XmlElement(name = "staff_notes")
	private String staffNotes;

	@XmlElement(name = "customer_message")
	private String customerMessage;

	@XmlElement(name = "discount_amount")
	private BigDecimal discountAmount;

	@XmlElement(name = "coupon_discount")
	private String couponDiscount;

	@XmlElement(name = "shipping_address_count")
	private String shippingAddressCount;

	@XmlElement(name = "is_deleted")
	private String isDeleted;

	@XmlElement(name = "ebay_order_id")
	private String ebayOrderId;

	@XmlElement(name = "is_email_opt_in")
	private String isEmailOptIn;

	@XmlElement(name = "credit_card_type")
	private String creditCardType;

	@XmlElement(name = "order_source")
	private String orderSource;

	@XmlElement(name = "first_name")
	private String firstName;

	@XmlElement(name = "last_name")
	private String lastName;

	private String email;

	@XmlElement(name = "external_source")
	private String externalSource;

	@XmlElement(name = "external_id")
	private String externalId;

	@XmlElement(name = "customer_status")
	private String customStatus;

	@XmlElement(name = "billing_address")
	private Address billingAddress;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
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

	public BigDecimal getSubtotalExTax() {
		return subtotalExTax;
	}

	public void setSubtotalExTax(BigDecimal subtotalExTax) {
		this.subtotalExTax = subtotalExTax;
	}

	public BigDecimal getSubtotalTax() {
		return subtotalTax;
	}

	public void setSubtotalTax(BigDecimal subtotalTax) {
		this.subtotalTax = subtotalTax;
	}

	public BigDecimal getTotalIncTax() {
		return totalIncTax;
	}

	public void setTotalIncTax(BigDecimal totalIncTax) {
		this.totalIncTax = totalIncTax;
	}

	public BigDecimal getSubtotalIncTax() {
		return subtotalIncTax;
	}

	public void setSubtotalIncTax(BigDecimal subtotalIncTax) {
		this.subtotalIncTax = subtotalIncTax;
	}

	public BigDecimal getShippingCostIncTax() {
		return shippingCostIncTax;
	}

	public void setShippingCostIncTax(BigDecimal shippingCostIncTax) {
		this.shippingCostIncTax = shippingCostIncTax;
	}

	public BigDecimal getShippingCostTax() {
		return shippingCostTax;
	}

	public void setShippingCostTax(BigDecimal shippingCostTax) {
		this.shippingCostTax = shippingCostTax;
	}

	public BigDecimal getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(BigDecimal totalTax) {
		this.totalTax = totalTax;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public DateTime getDateShipped() {
		return dateShipped;
	}

	public void setDateShipped(DateTime dateShipped) {
		this.dateShipped = dateShipped;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public BigDecimal getBaseShippingCost() {
		return baseShippingCost;
	}

	public void setBaseShippingCost(BigDecimal baseShippingCost) {
		this.baseShippingCost = baseShippingCost;
	}

	public BigDecimal getShippingCostExTax() {
		return shippingCostExTax;
	}

	public void setShippingCostExTax(BigDecimal shippingCostExTax) {
		this.shippingCostExTax = shippingCostExTax;
	}

	public Integer getShippingCostTaxClassId() {
		return shippingCostTaxClassId;
	}

	public void setShippingCostTaxClassId(Integer shippingCostTaxClassId) {
		this.shippingCostTaxClassId = shippingCostTaxClassId;
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

	public Integer getHandlingCostTaxClassId() {
		return handlingCostTaxClassId;
	}

	public void setHandlingCostTaxClassId(Integer handlingCostTaxClassId) {
		this.handlingCostTaxClassId = handlingCostTaxClassId;
	}

	public BigDecimal getBaseWrappingCost() {
		return baseWrappingCost;
	}

	public void setBaseWrappingCost(BigDecimal baseWrappingCost) {
		this.baseWrappingCost = baseWrappingCost;
	}

	public BigDecimal getWrappingCostExTax() {
		return wrappingCostExTax;
	}

	public void setWrappingCostExTax(BigDecimal wrappingCostExTax) {
		this.wrappingCostExTax = wrappingCostExTax;
	}

	public BigDecimal getWrappingCostIncTax() {
		return wrappingCostIncTax;
	}

	public void setWrappingCostIncTax(BigDecimal wrappingCostIncTax) {
		this.wrappingCostIncTax = wrappingCostIncTax;
	}

	public BigDecimal getWrappingCostTax() {
		return wrappingCostTax;
	}

	public void setWrappingCostTax(BigDecimal wrappingCostTax) {
		this.wrappingCostTax = wrappingCostTax;
	}

	public Integer getWrappingCostTaxClassId() {
		return wrappingCostTaxClassId;
	}

	public void setWrappingCostTaxClassId(Integer wrappingCostTaxClassId) {
		this.wrappingCostTaxClassId = wrappingCostTaxClassId;
	}

	public BigDecimal getTotalExTax() {
		return totalExTax;
	}

	public void setTotalExTax(BigDecimal totalExTax) {
		this.totalExTax = totalExTax;
	}

	public Integer getItemsTotal() {
		return itemsTotal;
	}

	public void setItemsTotal(Integer itemsTotal) {
		this.itemsTotal = itemsTotal;
	}

	public String getItemsShipped() {
		return itemsShipped;
	}

	public void setItemsShipped(String itemsShipped) {
		this.itemsShipped = itemsShipped;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPaymentProviderId() {
		return paymentProviderId;
	}

	public void setPaymentProviderId(String paymentProviderId) {
		this.paymentProviderId = paymentProviderId;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public BigDecimal getRefundedAmount() {
		return refundedAmount;
	}

	public void setRefundedAmount(BigDecimal refundedAmount) {
		this.refundedAmount = refundedAmount;
	}

	public String getOrderIsDigital() {
		return orderIsDigital;
	}

	public void setOrderIsDigital(String orderIsDigital) {
		this.orderIsDigital = orderIsDigital;
	}

	public BigDecimal getStoreCreditAmount() {
		return storeCreditAmount;
	}

	public void setStoreCreditAmount(BigDecimal storeCreditAmount) {
		this.storeCreditAmount = storeCreditAmount;
	}

	public BigDecimal getGiftCertificateAmount() {
		return giftCertificateAmount;
	}

	public void setGiftCertificateAmount(BigDecimal giftCertificateAmount) {
		this.giftCertificateAmount = giftCertificateAmount;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getGeoipCountry() {
		return geoipCountry;
	}

	public void setGeoipCountry(String geoipCountry) {
		this.geoipCountry = geoipCountry;
	}

	public String getGeoipCountryIso2() {
		return geoipCountryIso2;
	}

	public void setGeoipCountryIso2(String geoipCountryIso2) {
		this.geoipCountryIso2 = geoipCountryIso2;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public String getCurrencyExchangeRate() {
		return currencyExchangeRate;
	}

	public void setCurrencyExchangeRate(String currencyExchangeRate) {
		this.currencyExchangeRate = currencyExchangeRate;
	}

	public String getDefaultCurrencyId() {
		return defaultCurrencyId;
	}

	public void setDefaultCurrencyId(String defaultCurrencyId) {
		this.defaultCurrencyId = defaultCurrencyId;
	}

	public String getDefaultCurrencyCode() {
		return defaultCurrencyCode;
	}

	public void setDefaultCurrencyCode(String defaultCurrencyCode) {
		this.defaultCurrencyCode = defaultCurrencyCode;
	}

	public String getStaffNotes() {
		return staffNotes;
	}

	public void setStaffNotes(String staffNotes) {
		this.staffNotes = staffNotes;
	}

	public String getCustomerMessage() {
		return customerMessage;
	}

	public void setCustomerMessage(String customerMessage) {
		this.customerMessage = customerMessage;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public String getCouponDiscount() {
		return couponDiscount;
	}

	public void setCouponDiscount(String couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	public String getShippingAddressCount() {
		return shippingAddressCount;
	}

	public void setShippingAddressCount(String shippingAddressCount) {
		this.shippingAddressCount = shippingAddressCount;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getEbayOrderId() {
		return ebayOrderId;
	}

	public void setEbayOrderId(String ebayOrderId) {
		this.ebayOrderId = ebayOrderId;
	}

	public String getIsEmailOptIn() {
		return isEmailOptIn;
	}

	public void setIsEmailOptIn(String isEmailOptIn) {
		this.isEmailOptIn = isEmailOptIn;
	}

	public String getCreditCardType() {
		return creditCardType;
	}

	public void setCreditCardType(String creditCardType) {
		this.creditCardType = creditCardType;
	}

	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getExternalSource() {
		return externalSource;
	}

	public void setExternalSource(String externalSource) {
		this.externalSource = externalSource;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getCustomStatus() {
		return customStatus;
	}

	public void setCustomStatus(String customStatus) {
		this.customStatus = customStatus;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

}
