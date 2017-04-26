package com.bigcommerce;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientProperties;

import com.bigcommerce.catalog.models.CatalogSummary;
import com.bigcommerce.catalog.models.CatalogSummaryResponse;
import com.bigcommerce.catalog.models.Pagination;
import com.bigcommerce.catalog.models.Product;
import com.bigcommerce.catalog.models.Products;
import com.bigcommerce.catalog.models.ProductsResponse;
import com.bigcommerce.catalog.models.Variant;
import com.bigcommerce.catalog.models.VariantResponse;
import com.bigcommerce.exceptions.BigcommerceErrorResponseException;

public class BigcommerceSdk {

	static final String API_VERSION = "v3";
	static final String CLIENT_ID_HEADER = "X-Auth-Client";
	static final String ACESS_TOKEN_HEADER = "X-Auth-Token";

	private static final String CATALOG = "catalog";
	private static final String SUMMARY = "summary";
	private static final String PRODUCTS = "products";
	private static final String LIMIT = "limit";
	private static final String PAGE = "page";
	private static final String INCLUDE = "include";
	private static final String VARIANTS = "variants";
	private static final int MAX_LIMIT = 250;
	private static final String MEDIA_TYPE = MediaType.APPLICATION_JSON + ";charset=UTF-8";

	private static final Client CLIENT = ClientBuilder.newClient().property(ClientProperties.CONNECT_TIMEOUT, 60000)
			.property(ClientProperties.READ_TIMEOUT, 600000);

	private final WebTarget baseWebTarget;
	private final String storeHash;
	private final String clientId;
	private final String accessToken;

	public static interface ApiUrlStep {
		StoreHashStep withApiUrl(final String apiUrl);
	}

	public static interface StoreHashStep {
		ClientIdStep withStoreHash(final String storeHash);
	}

	public static interface ClientIdStep {
		AccessTokenStep withClientId(final String clientId);
	}

	public static interface AccessTokenStep {
		BuildStep withAccessToken(final String accessToken);
	}

	public static interface BuildStep {
		BigcommerceSdk build();
	}

	public static StoreHashStep newBuilder() {
		return new Steps();
	}

	static ApiUrlStep newSandboxBuilder() {
		return new Steps();
	}

	public String getStoreHash() {
		return storeHash;
	}

	public String getClientId() {
		return clientId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public CatalogSummary getCatalogSummary() {
		final Response response = baseWebTarget.path(CATALOG).path(SUMMARY).request()
				.header(CLIENT_ID_HEADER, getClientId()).header(ACESS_TOKEN_HEADER, getAccessToken()).get();
		if (Status.OK.getStatusCode() == response.getStatus()) {
			final CatalogSummaryResponse catalogSummaryResponse = response.readEntity(CatalogSummaryResponse.class);
			return catalogSummaryResponse.getData();
		}
		throw new BigcommerceErrorResponseException(response);
	}

	public Products getProducts(final int page) {
		return getProducts(page, MAX_LIMIT);
	}

	public Products getProducts(final int page, final int limit) {
		final Response response = baseWebTarget.path(CATALOG).path(PRODUCTS).queryParam(INCLUDE, VARIANTS)
				.queryParam(LIMIT, limit).queryParam(PAGE, page).request().header(CLIENT_ID_HEADER, getClientId())
				.header(ACESS_TOKEN_HEADER, getAccessToken()).get();
		if (Status.OK.getStatusCode() == response.getStatus()) {
			final ProductsResponse productsResponse = response.readEntity(ProductsResponse.class);
			final List<Product> products = productsResponse.getData();
			final Pagination pagination = productsResponse.getMeta().getPagination();
			return new Products(products, pagination);
		}
		throw new BigcommerceErrorResponseException(response);
	}

	public void updateVariant(Variant variant) {
		final Entity<Variant> variantEntity = Entity.entity(variant, MEDIA_TYPE);
		final Response response = baseWebTarget.path(CATALOG).path(PRODUCTS).path(variant.getProductId()).path(VARIANTS)
				.path(variant.getId()).request().header(CLIENT_ID_HEADER, getClientId())
				.header(ACESS_TOKEN_HEADER, getAccessToken()).put(variantEntity);
		if (Status.OK.getStatusCode() == response.getStatus()) {
			final VariantResponse variantResponse = response.readEntity(VariantResponse.class);
			variant = variantResponse.getData();
		} else {
			throw new BigcommerceErrorResponseException(response);
		}
	}

	private BigcommerceSdk(final Steps steps) {
		this.baseWebTarget = CLIENT.target(steps.apiUrl).path(steps.storeHash).path(API_VERSION);
		this.storeHash = steps.storeHash;
		this.clientId = steps.clientId;
		this.accessToken = steps.accessToken;
	}

	private static class Steps implements ApiUrlStep, StoreHashStep, ClientIdStep, AccessTokenStep, BuildStep {

		private String apiUrl = "https://api.bigcommerce.com/stores";
		private String storeHash;
		private String clientId;
		private String accessToken;

		@Override
		public StoreHashStep withApiUrl(final String apiUrl) {
			this.apiUrl = apiUrl;
			return this;
		}

		@Override
		public BuildStep withAccessToken(final String accessToken) {
			this.accessToken = accessToken;
			return this;
		}

		@Override
		public AccessTokenStep withClientId(final String clientId) {
			this.clientId = clientId;
			return this;
		}

		@Override
		public ClientIdStep withStoreHash(final String storeHash) {
			this.storeHash = storeHash;
			return this;
		}

		@Override
		public BigcommerceSdk build() {
			return new BigcommerceSdk(this);
		}

	}
}
