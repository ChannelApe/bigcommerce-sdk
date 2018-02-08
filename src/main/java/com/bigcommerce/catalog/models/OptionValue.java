package com.bigcommerce.catalog.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class OptionValue {

	@XmlElement(name = "option_display_name")
	private String optionDisplayName;
	private String label;
	private Integer id;

	@XmlElement(name = "option_id")
	private Integer optionId;

	public String getOptionDisplayName() {
		return optionDisplayName;
	}

	public void setOptionDisplayName(final String optionDisplayName) {
		this.optionDisplayName = optionDisplayName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public Integer getOptionId() {
		return optionId;
	}

	public void setOptionId(final Integer optionId) {
		this.optionId = optionId;
	}

}
