package com.bigcommerce.catalog.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class Brand implements Serializable {

	@XmlElement(name = "name")
	private String name;

	@XmlElement(name = "page_title")
	private String pageTitle;

	@XmlElement(name = "meta_keywords")
	private List<String> metaKeywords = new ArrayList<>();

	@XmlElement(name = "meta_description")
	private String metaDescription;

	@XmlElement(name = "search_keywords")
	private String searchKeywords;

	@XmlElement(name = "image_url")
	private String imageUrl;

	@XmlElement(name = "id")
	private Integer id;

	@XmlElement(name = "custom_url")
	private CustomUrl customUrl;

	public CustomUrl getCustomUrl() {
		return customUrl;
	}

	public void setCustomUrl(CustomUrl customUrl) {
		this.customUrl = customUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(final String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public List<String> getMetaKeywords() {
		return metaKeywords;
	}

	public void setMetaKeywords(final List<String> metaKeywords) {
		this.metaKeywords = metaKeywords;
	}

	public String getMetaDescription() {
		return metaDescription;
	}

	public void setMetaDescription(final String metaDescription) {
		this.metaDescription = metaDescription;
	}

	public String getSearchKeywords() {
		return searchKeywords;
	}

	public void setSearchKeywords(final String searchKeywords) {
		this.searchKeywords = searchKeywords;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(final String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Brand brand = (Brand) o;
		return Objects.equals(getName(), brand.getName()) &&
				Objects.equals(getPageTitle(), brand.getPageTitle()) &&
				Objects.equals(getMetaKeywords(), brand.getMetaKeywords()) &&
				Objects.equals(getMetaDescription(), brand.getMetaDescription()) &&
				Objects.equals(getSearchKeywords(), brand.getSearchKeywords()) &&
				Objects.equals(getImageUrl(), brand.getImageUrl()) &&
				Objects.equals(getId(), brand.getId()) &&
				Objects.equals(getCustomUrl(), brand.getCustomUrl());
	}

	@Override
	public int hashCode() {

		return Objects.hash(getName(), getPageTitle(), getMetaKeywords(), getMetaDescription(), getSearchKeywords(), getImageUrl(), getId(), getCustomUrl());
	}
}
