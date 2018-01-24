package com.bigcommerce.catalog.models;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Product {

	private String id;
	private String name;
	private String type;
	private String sku;
	private String description;
	private List<Variant> variants = new LinkedList<>();
	private BigDecimal weight;
	private Integer isVisible;
	private List<Integer> categories;
	private Integer brandId;
	private Boolean isConditionKnown;
	private List<String> metaKeywords;
	private String inventoryTracking;
	private String condition;
	private BigDecimal price;
	private List<CustomField> customFields;

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(final String sku) {
		this.sku = sku;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public List<Variant> getVariants() {
		return variants;
	}

	public void setVariants(final List<Variant> variants) {
		this.variants = variants;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(final BigDecimal weight) {
		this.weight = weight;
	}

	public Integer getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(final Integer isVisible) {
		this.isVisible = isVisible;
	}

	public List<Integer> getCategories() {
		return categories;
	}

	public void setCategories(final List<Integer> categories) {
		this.categories = categories;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(final Integer brandId) {
		this.brandId = brandId;
	}

	public Boolean getIsConditionKnown() {
		return isConditionKnown;
	}

	public void setIsConditionKnown(final Boolean isConditionKnown) {
		this.isConditionKnown = isConditionKnown;
	}

	public List<String> getMetaKeywords() {
		return metaKeywords;
	}

	public void setMetaKeywords(final List<String> metaKeywords) {
		this.metaKeywords = metaKeywords;
	}

	public List<CustomField> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(final List<CustomField> customFields) {
		this.customFields = customFields;
	}

	public String getInventoryTracking() {
		return inventoryTracking;
	}

	public void setInventoryTracking(final String inventoryTracking) {
		this.inventoryTracking = inventoryTracking;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(final String condition) {
		this.condition = condition;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(final BigDecimal price) {
		this.price = price;
	}

}
