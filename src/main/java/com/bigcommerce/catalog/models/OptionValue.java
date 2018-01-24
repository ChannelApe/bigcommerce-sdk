package com.bigcommerce.catalog.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OptionValue {
	private String optionDisplayName;
	private String label;
	private Integer id;
	private Integer optionId;

	public String getOptionDisplayName() {
		return optionDisplayName;
	}

	public void setOptionDisplayName(String optionDisplayName) {
		this.optionDisplayName = optionDisplayName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOptionId() {
		return optionId;
	}

	public void setOptionId(Integer optionId) {
		this.optionId = optionId;
	}

}
