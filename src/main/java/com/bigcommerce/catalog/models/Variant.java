package com.bigcommerce.catalog.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class Variant {

	private Integer id;
	@XmlElement(name = "product_id")
	private Integer productId;
	private String sku;
	@XmlElement(required = false)
	private String sku_id;
	private BigDecimal price;
	private String upc;
	private String mpn;
	@XmlElement(name = "inventory_level")
	private int inventoryLevel;

	@XmlElement(name = "image_url")
	private String imageUrl;
	private BigDecimal weight;

	@XmlElement(name = "option_values")
	private List<OptionValue> optionValues = new ArrayList<>();

	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(final Integer productId) {
		this.productId = productId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(final String sku) {
		this.sku = sku;
	}

	public String getSku_id() {
		return sku_id;
	}

	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(final BigDecimal price) {
		this.price = price;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(final String upc) {
		this.upc = upc;
	}

	public String getMpn() {
		return mpn;
	}

	public void setMpn(final String mpn) {
		this.mpn = mpn;
	}

	public int getInventoryLevel() {
		return inventoryLevel;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(final String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(final BigDecimal weight) {
		this.weight = weight;
	}

	public List<OptionValue> getOptionValues() {
		return optionValues;
	}

	public void setOptionValues(final List<OptionValue> optionValues) {
		this.optionValues = optionValues;
	}

	public void setInventoryLevel(final int inventoryLevel) {
		this.inventoryLevel = inventoryLevel;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		Variant variant = (Variant) object;
		return getInventoryLevel() == variant.getInventoryLevel() &&
				Objects.equals(getId(), variant.getId()) &&
				Objects.equals(getProductId(), variant.getProductId()) &&
				Objects.equals(getSku(), variant.getSku()) &&
				Objects.equals(getSku_id(), variant.getSku_id()) &&
				Objects.equals(getPrice(), variant.getPrice()) &&
				Objects.equals(getUpc(), variant.getUpc()) &&
				Objects.equals(getMpn(), variant.getMpn()) &&
				Objects.equals(getImageUrl(), variant.getImageUrl()) &&
				Objects.equals(getWeight(), variant.getWeight()) &&
				Objects.equals(getOptionValues(), variant.getOptionValues());
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).append(getProductId()).append(getSku()).append(getSku_id())
				.append(getPrice()).append(getWeight()).append(getUpc()).append(getMpn()).append(getInventoryLevel())
				.toHashCode();
	}
}
