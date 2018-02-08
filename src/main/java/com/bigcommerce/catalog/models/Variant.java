package com.bigcommerce.catalog.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
	public boolean equals(final Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof Variant)) {
			return false;
		}
		final Variant Variant = (Variant) object;
		final BigDecimal price = getPrice() != null ? getPrice().setScale(2, BigDecimal.ROUND_HALF_UP) : null;
		final BigDecimal otherPrice = Variant.getPrice() != null
				? Variant.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP) : null;
		return (new EqualsBuilder().append(getId(), Variant.getId()).append(getProductId(), Variant.getProductId())
				.append(getSku(), Variant.getSku()).append(price, otherPrice).append(getWeight(), Variant.getWeight())
				.append(getWeight(), Variant.getWeight()).append(getUpc(), Variant.getUpc())
				.append(getMpn(), Variant.getMpn()).append(getInventoryLevel(), Variant.getInventoryLevel())
				.isEquals());
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).append(getProductId()).append(getSku()).append(getPrice())
				.append(getUpc()).append(getMpn()).append(getInventoryLevel()).toHashCode();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
