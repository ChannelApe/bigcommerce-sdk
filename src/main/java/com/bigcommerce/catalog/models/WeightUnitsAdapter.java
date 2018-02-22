package com.bigcommerce.catalog.models;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class WeightUnitsAdapter extends XmlAdapter<String, WeightUnits> {

	@Override
	public WeightUnits unmarshal(final String weightUnit) throws Exception {
		return WeightUnits.toEnum(weightUnit);
	}

	@Override
	public String marshal(final WeightUnits weightUnit) throws Exception {
		return weightUnit.toString();
	}

}
