package com.bigcommerce.catalog.models;

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
public class Category {

	private Integer id;
	private String name;
	private String keyword;
	private String description;
	private List<Category> children;

	@XmlElement(name = "parent_id")
	@JsonInclude(Include.ALWAYS)
	private Integer parentId;

	@XmlElement(name = "page_title")
	private String pageTitle;

	@XmlElement(name = "meta_keywords")
	List<String> metaKeywords;

	@XmlElement(name = "is_visible")
	private Boolean visible;

	@XmlElement(name = "custom_url")
	CustomUrl customUrl;

	@XmlElement(name = "meta_description")
	String metaDescription;

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

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(final Integer parentId) {
		this.parentId = parentId;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(final String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(final String keyword) {
		this.keyword = keyword;
	}

	public Boolean isVisible() {
		return visible;
	}

	public void setVisible(final Boolean visible) {
		this.visible = visible;
	}

	public List<Category> getChildren() {
		return children;
	}

	public void setChildren(final List<Category> children) {
		this.children = children;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public List<String> getMetaKeywords() {
		return metaKeywords;
	}

	public void setMetaKeywords(final List<String> metaKeywords) {
		this.metaKeywords = metaKeywords;
	}

	public CustomUrl getCustomUrl() {
		return customUrl;
	}

	public void setCustomUrl(final CustomUrl customUrl) {
		this.customUrl = customUrl;
	}

	public String getMetaDescription() {
		return metaDescription;
	}

	public void setMetaDescription(final String metaDescription) {
		this.metaDescription = metaDescription;
	}
}
