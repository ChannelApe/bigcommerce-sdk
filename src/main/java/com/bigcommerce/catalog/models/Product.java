package com.bigcommerce.catalog.models;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

	private Integer id;
	private String name;
	private String type;
	private String sku;
	private String description;
	private List<Variant> variants = new LinkedList<>();
	private BigDecimal weight;

	@XmlElement(name = "is_visible")
	private Boolean isVisible;
	private List<Integer> categories;

	@XmlElement(name = "brand_id")
	private Integer brandId;

	@XmlElement(name = "is_condition_shown")
	private Boolean isConditionShown;

	@XmlElement(name = "meta_keywords")
	private List<String> metaKeywords = new LinkedList<>();

	@XmlElement(name = "search_keywords ")
	private String searchKeywords;

	@XmlElement(name = "inventory_tracking")
	private String inventoryTracking;
	private String condition;
	private BigDecimal price;

	@XmlElement(name = "custom_fields")
	private List<CustomField> customFields = new LinkedList<>();

	@XmlElement(name = "sort_order")
	private Integer sortOrder;

	@XmlElement(name = "custom_url ")
	private CustomUrl productUrl;

	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
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

	public Boolean getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(final Boolean isVisible) {
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

	public Boolean getIsConditionShown() {
		return isConditionShown;
	}

	public void setIsConditionShown(final Boolean isConditionShown) {
		this.isConditionShown = isConditionShown;
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

	public Integer getSortOrder() {
		return sortOrder;
	}

	public String getSearchKeywords() {
		return searchKeywords;
	}

	public void setSearchKeywords(String searchKeywords) {
		this.searchKeywords = searchKeywords;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public CustomUrl getProductUrl() {
		return productUrl;
	}

	public void setProductUrl(CustomUrl productUrl) {
		this.productUrl = productUrl;
	}
}

