package com.bigcommerce;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.json.JSONObject;
import org.junit.Test;

import com.bigcommerce.catalog.models.CatalogSummary;

public class BigcommerceSdkDriver {

	private static final String STORE_HASH = System.getenv("BIGCOMMERCE_STORE_HASH");
	private static final String CLIENT_ID = System.getenv("BIGCOMMERCE_CLIENT_ID");
	private static final String ACCESS_TOKEN = System.getenv("BIGCOMMERCE_ACCESS_TOKEN");

	@Test
	public void whenRetrievingCatalogSummaryThenREturnCatalogSummary() {
		final BigcommerceSdk bigcommerceSdk = BigcommerceSdk.newBuilder().withStoreHash(STORE_HASH)
				.withClientId(CLIENT_ID).withAccessToken(ACCESS_TOKEN).build();

		final CatalogSummary actualCatalogSummary = bigcommerceSdk.getCatalogSummary();

		assertNotNull(actualCatalogSummary);
		assertEquals(7, actualCatalogSummary.getVariantCount());
		System.out.println("---- Catalog Summary ----");
		System.out.println(new JSONObject(actualCatalogSummary).toString());
	}

}
