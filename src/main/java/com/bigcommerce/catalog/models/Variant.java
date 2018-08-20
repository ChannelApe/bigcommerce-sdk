package com.bigcommerce.catalog.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Variant {

	private Integer id;
	private String sku;
	private BigDecimal price;
	private String upc;
	private String mpn;
	private BigDecimal height;
	private BigDecimal width;
	private BigDecimal depth;
	private String gtin;
	private BigDecimal weight;

	@XmlElement(name = "product_id")
	private Integer productId;

	@XmlElement(required = false, name = "sku_id")
	private String skuId;

	@XmlElement(name = "inventory_level")
	private int inventoryLevel;

	@XmlElement(name = "image_url")
	private String imageUrl;

	@XmlElement(name = "option_values")
	private List<OptionValue> optionValues = new ArrayList<>();

	@XmlElement(name = "cost_price")
	private BigDecimal costPrice;

	@XmlElement(name = "retail_price")
	private BigDecimal retailPrice;

	@XmlElement(name = "sale_price")
	private BigDecimal salePrice;

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

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(final String skuId) {
		this.skuId = skuId;
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

	public BigDecimal getHeight() {
		return height;
	}

	public void setHeight(final BigDecimal height) {
		this.height = height;
	}

	public BigDecimal getWidth() {
		return width;
	}

	public void setWidth(final BigDecimal width) {
		this.width = width;
	}

	public BigDecimal getDepth() {
		return depth;
	}

	public void setDepth(final BigDecimal depth) {
		this.depth = depth;
	}

	public String getGtin() {
		return gtin;
	}

	public void setGtin(final String gtin) {
		this.gtin = gtin;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(final BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public BigDecimal getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(final BigDecimal retailPrice) {
		this.retailPrice = retailPrice;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(final BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	@Override
	public boolean equals(final Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof Variant)) {
			return false;
		}
		final Variant variant = (Variant) object;

		return (new EqualsBuilder().append(getId(), variant.getId()).append(getProductId(), variant.getProductId())
				.append(getSku(), variant.getSku()).append(getUpc(), variant.getUpc())
				.append(getMpn(), variant.getMpn()).append(getInventoryLevel(), variant.getInventoryLevel())
				.append(getGtin(), variant.getGtin()).isEquals())
				&& (isScaledDeimalValueSame(getPrice(), variant.getPrice()))
				&& (isScaledDeimalValueSame(getWeight(), variant.getWeight()))
				&& (isScaledDeimalValueSame(getWidth(), variant.getWidth()))
				&& (isScaledDeimalValueSame(getHeight(), variant.getHeight()))
				&& (isScaledDeimalValueSame(getDepth(), variant.getDepth()))
				&& (isScaledDeimalValueSame(getRetailPrice(), variant.getRetailPrice()))
				&& (isScaledDeimalValueSame(getSalePrice(), variant.getSalePrice()))
				&& (isScaledDeimalValueSame(getCostPrice(), variant.getCostPrice()));

	}

	@Override
	public int hashCode() {
		final BigDecimal determinedWidth = getWidth() == null ? null : getWidth().setScale(10, RoundingMode.HALF_UP);
		final BigDecimal determinedHeight = getHeight() == null ? null : getHeight().setScale(10, RoundingMode.HALF_UP);
		final BigDecimal determinedDepth = getDepth() == null ? null : getDepth().setScale(10, RoundingMode.HALF_UP);
		final BigDecimal determindedWeight = getWeight() == null ? null
				: getWeight().setScale(10, RoundingMode.HALF_UP);
		final BigDecimal determinedPrice = getPrice() == null ? null : getPrice().setScale(10, RoundingMode.HALF_UP);
		final BigDecimal determinedRetailPrice = getRetailPrice() == null ? null
				: getRetailPrice().setScale(10, RoundingMode.HALF_UP);
		final BigDecimal determinedCostPrice = getCostPrice() == null ? null
				: getCostPrice().setScale(10, RoundingMode.HALF_UP);
		final BigDecimal determinedSalePrice = getSalePrice() == null ? null
				: getSalePrice().setScale(10, RoundingMode.HALF_UP);

		return new HashCodeBuilder().append(getId()).append(getProductId()).append(getSku()).append(determinedPrice)
				.append(determindedWeight).append(getUpc()).append(getMpn()).append(getInventoryLevel())
				.append(determinedHeight).append(determinedWidth).append(determinedDepth).append(determinedRetailPrice)
				.append(determinedSalePrice).append(determinedCostPrice).toHashCode();
	}

	private boolean isScaledDeimalValueSame(final BigDecimal leftValue, final BigDecimal rightValue) {
		if (leftValue == null && rightValue == null) {
			return true;
		} else if (leftValue == null || rightValue == null) {
			return false;
		}
		return leftValue.compareTo(rightValue) == 0;
	}
}
