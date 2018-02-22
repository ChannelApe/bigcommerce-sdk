package com.bigcommerce.catalog.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Store {

	@XmlElement(name = "weight_units")
	@XmlJavaTypeAdapter(WeightUnitsAdapter.class)
	private WeightUnits weightUnits;

	public WeightUnits getWeightUnits() {
		return weightUnits;
	}

	public void setWeightUnits(final WeightUnits weightUnits) {
		this.weightUnits = weightUnits;
	}
}
