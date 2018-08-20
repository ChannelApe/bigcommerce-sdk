package com.bigcommerce.catalog.models;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.io.Serializable;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonInclude;

@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(NON_NULL)
public class CustomUrl implements Serializable {

	static final long serialVersionUID = 26372357L;

	private String url;

	@XmlElement(name = "is_customized")
	private Boolean customized;

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public Boolean isCustomized() {
		return customized;
	}

	public void setCustomized(final Boolean customized) {
		this.customized = customized;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final CustomUrl customUrl = (CustomUrl) o;
		return isCustomized() == customUrl.isCustomized() && Objects.equals(getUrl(), customUrl.getUrl());
	}

	@Override
	public int hashCode() {

		return Objects.hash(getUrl(), isCustomized());
	}
}
