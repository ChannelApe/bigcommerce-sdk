package com.bigcommerce.catalog.models;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CatalogSummary {

	@XmlElement(name = "inventory_count")
	private long inventoryCount;
	@XmlElement(name = "variant_count")
	private long variantCount;
	@XmlElement(name = "inventory_value")
	private BigDecimal inventoryValue;
	@XmlElement(name = "highest_variant_price")
	private BigDecimal highestVariantPrice;
	@XmlElement(name = "average_variant_price")
	private BigDecimal averageVariantPrice;

	public long getInventoryCount() {
		return inventoryCount;
	}

	public void setInventoryCount(long inventoryCount) {
		this.inventoryCount = inventoryCount;
	}

	public long getVariantCount() {
		return variantCount;
	}

	public void setVariantCount(long variantCount) {
		this.variantCount = variantCount;
	}

	public BigDecimal getInventoryValue() {
		return inventoryValue;
	}

	public void setInventoryValue(BigDecimal inventoryValue) {
		this.inventoryValue = inventoryValue;
	}

	public BigDecimal getHighestVariantPrice() {
		return highestVariantPrice;
	}

	public void setHighestVariantPrice(BigDecimal highestVariantPrice) {
		this.highestVariantPrice = highestVariantPrice;
	}

	public BigDecimal getAverageVariantPrice() {
		return averageVariantPrice;
	}

	public void setAverageVariantPrice(BigDecimal averageVariantPrice) {
		this.averageVariantPrice = averageVariantPrice;
	}

}
