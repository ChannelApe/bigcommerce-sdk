package com.bigcommerce.catalog.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;

public class VariantTest {

	@Test
	public void givenSameInstanceWhenTestingEqualityThenReturnTrue() {
		final Variant leftHandVariant = new Variant();
		leftHandVariant.setId(64);
		leftHandVariant.setProductId(113);

		final Variant rightHandVariant = leftHandVariant;

		assertTrue(leftHandVariant.equals(rightHandVariant));
		assertEquals(leftHandVariant.hashCode(), rightHandVariant.hashCode());
	}

	@Test
	public void givenSomeStringWhenTestingEqualityThenReturnFalse() {
		final Variant leftHandVariant = new Variant();
		leftHandVariant.setId(112);
		leftHandVariant.setProductId(80);

		final Object rightHandVariant = "test123";

		assertFalse(leftHandVariant.equals(rightHandVariant));
		assertFalse(leftHandVariant.hashCode() == rightHandVariant.hashCode());
	}

	@Test
	public void givenSameFieldsWhenTestingEqualityThenReturnTrue() {
		final Variant leftHandVariant = new Variant();
		leftHandVariant.setId(12);
		leftHandVariant.setProductId(144);
		leftHandVariant.setSku("ABC030303");
		leftHandVariant.setPrice(BigDecimal.valueOf(30.45));
		leftHandVariant.setUpc("9839537238592");
		leftHandVariant.setMpn("AFFF332");
		leftHandVariant.setInventoryLevel(76);
		leftHandVariant.setGtin("someglobaltradeitemnumber");
		leftHandVariant.setCostPrice(BigDecimal.valueOf(42.12));
		leftHandVariant.setRetailPrice(BigDecimal.valueOf(442.12));
		leftHandVariant.setSalePrice(BigDecimal.valueOf(552.12));
		final Variant rightHandVariant = new Variant();
		rightHandVariant.setId(12);
		rightHandVariant.setProductId(144);
		rightHandVariant.setSku("ABC030303");
		rightHandVariant.setPrice(BigDecimal.valueOf(30.45));
		rightHandVariant.setUpc("9839537238592");
		rightHandVariant.setMpn("AFFF332");
		rightHandVariant.setGtin("someglobaltradeitemnumber");
		rightHandVariant.setCostPrice(BigDecimal.valueOf(42.12));
		rightHandVariant.setRetailPrice(BigDecimal.valueOf(442.12));
		rightHandVariant.setSalePrice(BigDecimal.valueOf(552.12));
		rightHandVariant.setInventoryLevel(76);

		assertTrue(leftHandVariant.equals(rightHandVariant));
		assertEquals(leftHandVariant.hashCode(), rightHandVariant.hashCode());
	}

	@Test
	public void givenDifferentFieldWhenTestingEqualityThenReturnFalse() {
		final Variant leftHandVariant = new Variant();
		leftHandVariant.setId(12);
		leftHandVariant.setProductId(144);
		leftHandVariant.setSku("ABC030303");
		leftHandVariant.setPrice(BigDecimal.valueOf(30.45));
		leftHandVariant.setUpc("9839537238592");
		leftHandVariant.setMpn("AFFF332");
		leftHandVariant.setGtin("someglobaltradeitemnumber");
		leftHandVariant.setInventoryLevel(76);
		leftHandVariant.setCostPrice(BigDecimal.valueOf(42.12));
		leftHandVariant.setRetailPrice(BigDecimal.valueOf(442.12));
		leftHandVariant.setSalePrice(BigDecimal.valueOf(552.12));

		final Variant rightHandVariant = new Variant();
		rightHandVariant.setId(12);
		rightHandVariant.setProductId(144);
		rightHandVariant.setSku("ABC030303");
		rightHandVariant.setPrice(BigDecimal.valueOf(30.45));
		rightHandVariant.setUpc("9839537238592");
		rightHandVariant.setMpn("AFFF332");
		rightHandVariant.setGtin("someglobaltradeitemnumber");
		rightHandVariant.setInventoryLevel(75);
		rightHandVariant.setCostPrice(BigDecimal.valueOf(42.12));
		rightHandVariant.setRetailPrice(BigDecimal.valueOf(442.12));
		rightHandVariant.setSalePrice(BigDecimal.valueOf(552.12));

		assertFalse(leftHandVariant.equals(rightHandVariant));
		assertFalse(leftHandVariant.hashCode() == rightHandVariant.hashCode());
	}

	@Test
	public void givenSomeDifferentScaledBigDecimalWhenTestingEqualityThenReturnFalse() {
		final Variant leftHandVariant = new Variant();
		leftHandVariant.setId(12);
		leftHandVariant.setProductId(144);
		leftHandVariant.setSku("ABC030303");
		leftHandVariant.setPrice(new BigDecimal(30.4500000000000000000).setScale(6, RoundingMode.CEILING));
		leftHandVariant.setUpc("9839537238592");
		leftHandVariant.setMpn("AFFF332");
		leftHandVariant.setGtin("someglobaltradeitemnumber");
		leftHandVariant.setInventoryLevel(76);
		leftHandVariant.setCostPrice(BigDecimal.valueOf(42.12));
		leftHandVariant.setRetailPrice(BigDecimal.valueOf(442.12));
		leftHandVariant.setSalePrice(BigDecimal.valueOf(552.12));
		final Variant rightHandVariant = new Variant();
		rightHandVariant.setId(12);
		rightHandVariant.setProductId(144);
		rightHandVariant.setSku("ABC030303");
		rightHandVariant.setPrice(new BigDecimal(30.4500000000000000000).setScale(7, RoundingMode.CEILING));
		rightHandVariant.setUpc("9839537238592");
		rightHandVariant.setMpn("AFFF332");
		rightHandVariant.setGtin("someglobaltradeitemnumber");
		rightHandVariant.setInventoryLevel(76);
		rightHandVariant.setCostPrice(BigDecimal.valueOf(42.12));
		rightHandVariant.setRetailPrice(BigDecimal.valueOf(442.12));
		rightHandVariant.setSalePrice(BigDecimal.valueOf(552.12));

		assertTrue(leftHandVariant.equals(rightHandVariant));
		assertTrue(leftHandVariant.hashCode() == rightHandVariant.hashCode());
	}
}
