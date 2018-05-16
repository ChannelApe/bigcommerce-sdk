package com.bigcommerce.catalog.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(NON_NULL)
public class CustomUrl implements Serializable {

	@XmlElement(name = "url")
	private String url;

	@XmlElement(name = "is_customized")
	private boolean isCustomized;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isCustomized() {
		return isCustomized;
	}

	public void setCustomized(boolean customized) {
		isCustomized = customized;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CustomUrl customUrl = (CustomUrl) o;
		return isCustomized() == customUrl.isCustomized() &&
				Objects.equals(getUrl(), customUrl.getUrl());
	}

	@Override
	public int hashCode() {

		return Objects.hash(getUrl(), isCustomized());
	}
}
