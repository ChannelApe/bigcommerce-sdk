package com.bigcommerce;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientProperties;

import com.bigcommerce.catalog.models.CatalogSummary;
import com.bigcommerce.catalog.models.CatalogSummaryResponse;
import com.bigcommerce.exceptions.BigcommerceErrorResponseException;

public class BigcommerceSdk {

	private static final Client CLIENT = ClientBuilder.newClient().property(ClientProperties.CONNECT_TIMEOUT, 60000)
			.property(ClientProperties.READ_TIMEOUT, 600000);
	private static final String API_URL_PREFIX = "https://api.bigcommerce.com/stores";
	private static final String API_VERSION = "v3";
	private static final String CATALOG = "catalog";
	private static final String SUMMARY = "summary";
	private static final String CLIENT_ID_HEADER = "X-Auth-Client";
	private static final String ACESS_TOKEN_HEADER = "X-Auth-Token";

	private final String storeHash;
	private final String clientId;
	private final String accessToken;

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
		final Response response = CLIENT.target(API_URL_PREFIX).path(getStoreHash()).path(API_VERSION).path(CATALOG)
				.path(SUMMARY).request().header(CLIENT_ID_HEADER, getClientId())
				.header(ACESS_TOKEN_HEADER, getAccessToken()).get();
		if (Status.OK.getStatusCode() == response.getStatus()) {
			final CatalogSummaryResponse catalogSummaryResponse = response.readEntity(CatalogSummaryResponse.class);
			return catalogSummaryResponse.getData();
		}
		throw new BigcommerceErrorResponseException(response);
	}

	private BigcommerceSdk(final Steps steps) {
		this.storeHash = steps.storeHash;
		this.clientId = steps.clientId;
		this.accessToken = steps.accessToken;
	}

	private static class Steps implements StoreHashStep, ClientIdStep, AccessTokenStep, BuildStep {

		private String storeHash;
		private String clientId;
		private String accessToken;

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
