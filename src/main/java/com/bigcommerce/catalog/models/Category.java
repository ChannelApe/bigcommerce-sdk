package com.bigcommerce.catalog.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Category {

	private Integer id;
	private String name;
	@XmlElement(name = "parent_id")
	@JsonInclude(Include.ALWAYS)
	private Integer parentId;
	@XmlElement(name = "page_title")
	private String pageTitle;
	private String keyword;
	private String description;
	@XmlElement(name = "meta_keywords")
	List<String> metaKeywords;
	@XmlElement(name = "is_visible")
	private Boolean visible;
	@XmlElement(name = "custom_url")
	CustomUrl customUrl;
	@XmlElement(name = "meta_description")
	String metaDescription;;
	private List<Category> children;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Boolean isVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public List<Category> getChildren() {
		return children;
	}

	public void setChildren(List<Category> children) {
		this.children = children;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getMetaKeywords() {
		return metaKeywords;
	}

	public void setMetaKeywords(List<String> metaKeywords) {
		this.metaKeywords = metaKeywords;
	}

	public CustomUrl getCustomUrl() {
		return customUrl;
	}

	public void setCustomUrl(CustomUrl customUrl) {
		this.customUrl = customUrl;
	}

	public String getMetaDescription() {
		return metaDescription;
	}

	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}
}
