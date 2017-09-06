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
public class LineItem {

	private int id;
	private String sku;
	private int quantity;

	@XmlElement(name = "price_inc_tax")
	private BigDecimal priceIncTax;

	private String name;

	@XmlElement(name = "weight")
	private BigDecimal weight;

	@XmlElement(name = "fixed_shipping_cost")
	private BigDecimal fixedShippingCost;

	@XmlElement(name = "product_id")
	private int productId;

	@XmlElement(name = "order_address_id")
	private int orderAddressId;

	private String type;

	@XmlElement(name = "base_price")
	private BigDecimal basePrice;

	@XmlElement(name = "price_ex_tax")
	private BigDecimal priceExTax;

	@XmlElement(name = "base_total")
	private BigDecimal baseTotal;

	@XmlElement(name = "total_ex_tax")
	private BigDecimal totalExTax;

	@XmlElement(name = "total_inc_tax")
	private BigDecimal totalIncTax;

	@XmlElement(name = "total_tax")
	private BigDecimal totalTax;

	@XmlElement(name = "base_cost_price")
	private BigDecimal baseCostPrice;

	@XmlElement(name = "cost_price_inc_tax")
	private BigDecimal costPriceIncTax;

	@XmlElement(name = "cost_price_ex_tax")
	private BigDecimal costPriceExTax;

	@XmlElement(name = "cost_price_tax")
	private BigDecimal costPriceTax;

	@XmlElement(name = "is_refunded")
	private boolean isRefunded;

	@XmlElement(name = "quantity_refunded")
	private int quantityRefunded;

	@XmlElement(name = "refund_amount")
	private BigDecimal refundAmount;

	@XmlElement(name = "return_id")
	private int returnId;

	@XmlElement(name = "wrapping_name")
	private String wrappingName;

	@XmlElement(name = "base_wrapping_cost")
	private BigDecimal baseWrappingCost;

	@XmlElement(name = "wrapping_cost_ex_tax")
	private BigDecimal wrappingCostExTax;

	@XmlElement(name = "wrapping_cost_inc_tax")
	private BigDecimal wrappingCostIncTax;

	@XmlElement(name = "wrapping_cost_tax")
	private BigDecimal wrappingCostTax;

	@XmlElement(name = "wrapping_message")
	private String wrappingMessage;

	@XmlElement(name = "quantity_shipped")
	private int quantityShipped;

	@XmlElement(name = "event_name")
	private String eventName;

	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	@XmlElement(name = "event_date")
	private DateTime eventDate;

	@XmlElement(name = "ebay_item_id")
	private String ebayItemId;

	@XmlElement(name = "ebay_transaction_id")
	private String ebayTransactionId;

	@XmlElement(name = "option_set_id")
	private int optionSetId;

	@XmlElement(name = "parent_order_product_id")
	private int parentOrderProductId;

	@XmlElement(name = "is_bundled_product")
	private boolean isBundledProduct;

	@XmlElement(name = "bin_picking_number")
	private String binPickingNumber;

	@XmlElement(name = "external_id")
	private String externalId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPriceIncTax() {
		return priceIncTax;
	}

	public void setPriceIncTax(BigDecimal priceIncTax) {
		this.priceIncTax = priceIncTax;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BigDecimal getFixedShippingCost() {
		return fixedShippingCost;
	}

	public void setFixedShippingCost(BigDecimal fixedShippingCost) {
		this.fixedShippingCost = fixedShippingCost;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getOrderAddressId() {
		return orderAddressId;
	}

	public void setOrderAddressId(int orderAddressId) {
		this.orderAddressId = orderAddressId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}

	public BigDecimal getPriceExTax() {
		return priceExTax;
	}

	public void setPriceExTax(BigDecimal priceExTax) {
		this.priceExTax = priceExTax;
	}

	public BigDecimal getBaseTotal() {
		return baseTotal;
	}

	public void setBaseTotal(BigDecimal baseTotal) {
		this.baseTotal = baseTotal;
	}

	public BigDecimal getTotalExTax() {
		return totalExTax;
	}

	public void setTotalExTax(BigDecimal totalExTax) {
		this.totalExTax = totalExTax;
	}

	public BigDecimal getTotalIncTax() {
		return totalIncTax;
	}

	public void setTotalIncTax(BigDecimal totalIncTax) {
		this.totalIncTax = totalIncTax;
	}

	public BigDecimal getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(BigDecimal totalTax) {
		this.totalTax = totalTax;
	}

	public BigDecimal getBaseCostPrice() {
		return baseCostPrice;
	}

	public void setBaseCostPrice(BigDecimal baseCostPrice) {
		this.baseCostPrice = baseCostPrice;
	}

	public BigDecimal getCostPriceIncTax() {
		return costPriceIncTax;
	}

	public void setCostPriceIncTax(BigDecimal costPriceIncTax) {
		this.costPriceIncTax = costPriceIncTax;
	}

	public BigDecimal getCostPriceExTax() {
		return costPriceExTax;
	}

	public void setCostPriceExTax(BigDecimal costPriceExTax) {
		this.costPriceExTax = costPriceExTax;
	}

	public BigDecimal getCostPriceTax() {
		return costPriceTax;
	}

	public void setCostPriceTax(BigDecimal costPriceTax) {
		this.costPriceTax = costPriceTax;
	}

	public boolean isRefunded() {
		return isRefunded;
	}

	public void setRefunded(boolean isRefunded) {
		this.isRefunded = isRefunded;
	}

	public int getQuantityRefunded() {
		return quantityRefunded;
	}

	public void setQuantityRefunded(int quantityRefunded) {
		this.quantityRefunded = quantityRefunded;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public int getReturnId() {
		return returnId;
	}

	public void setReturnId(int returnId) {
		this.returnId = returnId;
	}

	public String getWrappingName() {
		return wrappingName;
	}

	public void setWrappingName(String wrappingName) {
		this.wrappingName = wrappingName;
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

	public String getWrappingMessage() {
		return wrappingMessage;
	}

	public void setWrappingMessage(String wrappingMessage) {
		this.wrappingMessage = wrappingMessage;
	}

	public int getQuantityShipped() {
		return quantityShipped;
	}

	public void setQuantityShipped(int qualityShipped) {
		this.quantityShipped = qualityShipped;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public DateTime getEventDate() {
		return eventDate;
	}

	public void setEventDate(DateTime eventDate) {
		this.eventDate = eventDate;
	}

	public String getEbayItemId() {
		return ebayItemId;
	}

	public void setEbayItemId(String ebayItemId) {
		this.ebayItemId = ebayItemId;
	}

	public String getEbayTransactionId() {
		return ebayTransactionId;
	}

	public void setEbayTransactionId(String ebayTransactionId) {
		this.ebayTransactionId = ebayTransactionId;
	}

	public int getOptionSetId() {
		return optionSetId;
	}

	public void setOptionSetId(int optionSetId) {
		this.optionSetId = optionSetId;
	}

	public int getParentOrderProductId() {
		return parentOrderProductId;
	}

	public void setParentOrderProductId(int parentOrderProductId) {
		this.parentOrderProductId = parentOrderProductId;
	}

	public boolean isBundledProduct() {
		return isBundledProduct;
	}

	public void setBundledProduct(boolean isBundledProduct) {
		this.isBundledProduct = isBundledProduct;
	}

	public String getBinPickingNumber() {
		return binPickingNumber;
	}

	public void setBinPickingNumber(String binPickingNumber) {
		this.binPickingNumber = binPickingNumber;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

}
