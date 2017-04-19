package com.bigcommerce;

public class BigcommerceSdk {

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
