package com.bigcommerce;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;

import com.bigcommerce.catalog.models.CatalogSummary;
import com.bigcommerce.catalog.models.Order;
import com.bigcommerce.catalog.models.Products;
import com.bigcommerce.catalog.models.Variant;

public class BigcommerceSdkDriver {

	private static final String STORE_HASH = System.getenv("BIGCOMMERCE_STORE_HASH");
	private static final String CLIENT_ID = System.getenv("BIGCOMMERCE_CLIENT_ID");
	private static final String ACCESS_TOKEN = System.getenv("BIGCOMMERCE_ACCESS_TOKEN");

	@Test
	@Ignore
	public void whenRetrievingCatalogSummaryThenReturnCatalogSummary() {
		final BigcommerceSdk bigcommerceSdk = BigcommerceSdk.newBuilder().withStoreHash(STORE_HASH)
				.withClientId(CLIENT_ID).withAccessToken(ACCESS_TOKEN).build();

		final CatalogSummary actualCatalogSummary = bigcommerceSdk.getCatalogSummary();

		assertNotNull(actualCatalogSummary);
		assertEquals(7, actualCatalogSummary.getVariantCount());
		System.out.println("---- Catalog Summary ----");
		System.out.println(new JSONObject(actualCatalogSummary).toString());
	}

	@Test
	public void givenPage1WhenRetrievingProductsThenReturnProducts() {
		final BigcommerceSdk bigcommerceSdk = BigcommerceSdk.newBuilder().withStoreHash(STORE_HASH)
				.withClientId(CLIENT_ID).withAccessToken(ACCESS_TOKEN).build();

		final Products actualProducts = bigcommerceSdk.getProducts(1);

		assertNotNull(actualProducts);
		assertFalse(actualProducts.getProducts().isEmpty());
		System.out.println("---- Products 1st Page ----");
		System.out.println(new JSONObject(actualProducts).toString());
	}

	@Test
	@Ignore
	public void givenVariantWhenUpdatingVariantThenUpdateVariant() {
		final BigcommerceSdk bigcommerceSdk = BigcommerceSdk.newBuilder().withStoreHash(STORE_HASH)
				.withClientId(CLIENT_ID).withAccessToken(ACCESS_TOKEN).build();

		final Variant variant = new Variant();
		variant.setId("85");
		variant.setProductId("113");
		variant.setInventoryLevel(3);
		variant.setPrice(BigDecimal.valueOf(12.99));

		bigcommerceSdk.updateVariant(variant);

		assertNotNull(variant);
		assertEquals(3, variant.getInventoryLevel());
		System.out.println("---- Updated Variant ----");
		System.out.println(new JSONObject(variant).toString());
	}

	@Test
	public void givenPage1WhenRetrievingOrdersThenReturnOrders() {
		final BigcommerceSdk bigcommerceSdk = BigcommerceSdk.newBuilder().withStoreHash(STORE_HASH)
				.withClientId(CLIENT_ID).withAccessToken(ACCESS_TOKEN).build();

		final List<Order> actualOrders = bigcommerceSdk.getOrders(1);

		assertNotNull(actualOrders);
		assertFalse(actualOrders.isEmpty());

	}

}
