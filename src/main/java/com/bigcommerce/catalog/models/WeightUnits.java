package com.bigcommerce.catalog.models;

public enum WeightUnits {

	TONNES("Tonnes"), KILOGRAMS("KGS"), GRAMS("Grams"), OUNCES("Ounces"), POUNDS("LBS");

	static final String NO_MATCHING_ENUMS_ERROR_MESSAGE = "No matching enum found for weight unit: %s";
	private final String value;

	private WeightUnits(final String value) {
		this.value = value;
	}

	public static WeightUnits toEnum(final String value) {
		if (TONNES.toString().equals(value)) {
			return WeightUnits.TONNES;
		} else if (KILOGRAMS.toString().equals(value)) {
			return WeightUnits.KILOGRAMS;
		} else if (GRAMS.toString().equals(value)) {
			return WeightUnits.GRAMS;
		} else if (OUNCES.toString().equals(value)) {
			return WeightUnits.OUNCES;
		} else if (POUNDS.toString().equals(value)) {
			return WeightUnits.POUNDS;
		}

		throw new IllegalArgumentException(String.format(NO_MATCHING_ENUMS_ERROR_MESSAGE, value));
	}

	@Override
	public String toString() {
		return value;
	}
}
