package com.bigcommerce.catalog.models;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Variant {

	private String id;
	@XmlElement(name = "product_id")
	private String productId;
	private String sku;
	private BigDecimal price;
	private String upc;
	private String mpn;
	@XmlElement(name = "inventory_level")
	private int inventoryLevel;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	public String getMpn() {
		return mpn;
	}

	public void setMpn(String mpn) {
		this.mpn = mpn;
	}

	public int getInventoryLevel() {
		return inventoryLevel;
	}

	public void setInventoryLevel(int inventoryLevel) {
		this.inventoryLevel = inventoryLevel;
	}

	@Override
	public boolean equals(final Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof Variant)) {
			return false;
		}
		final Variant Variant = (Variant) object;
		return new EqualsBuilder().append(getId(), Variant.getId()).append(getProductId(), Variant.getProductId())
				.append(getSku(), Variant.getSku()).append(getPrice(), Variant.getPrice())
				.append(getUpc(), Variant.getUpc()).append(getMpn(), Variant.getMpn())
				.append(getInventoryLevel(), Variant.getInventoryLevel()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).append(getProductId()).append(getSku()).append(getPrice())
				.append(getUpc()).append(getMpn()).append(getInventoryLevel()).toHashCode();
	}

}
