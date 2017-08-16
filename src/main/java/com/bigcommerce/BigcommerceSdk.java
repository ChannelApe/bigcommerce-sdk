package com.bigcommerce;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bigcommerce.catalog.models.CatalogSummary;
import com.bigcommerce.catalog.models.CatalogSummaryResponse;
import com.bigcommerce.catalog.models.Order;
import com.bigcommerce.catalog.models.Pagination;
import com.bigcommerce.catalog.models.Product;
import com.bigcommerce.catalog.models.Products;
import com.bigcommerce.catalog.models.ProductsResponse;
import com.bigcommerce.catalog.models.Variant;
import com.bigcommerce.catalog.models.VariantResponse;
import com.bigcommerce.exceptions.BigcommerceErrorResponseException;
import com.bigcommerce.exceptions.BigcommerceException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.github.rholder.retry.Attempt;
import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.github.rholder.retry.WaitStrategy;

public class BigcommerceSdk {

	static final String API_VERSION_V2 = "v2";
	static final String API_VERSION = "v3";
	static final String CLIENT_ID_HEADER = "X-Auth-Client";
	static final String ACESS_TOKEN_HEADER = "X-Auth-Token";
	static final String RATE_LIMIT_TIME_RESET_HEADER = "X-Rate-Limit-Time-Reset-Ms";

	static final int TOO_MANY_REQUESTS_STATUS_CODE = 429;

	private static final String CATALOG = "catalog";
	private static final String SUMMARY = "summary";
	private static final String PRODUCTS = "products";
	private static final String ORDERS = "orders";
	private static final String LIMIT = "limit";
	private static final String PAGE = "page";
	private static final String INCLUDE = "include";
	private static final String VARIANTS = "variants";
	private static final int MAX_LIMIT = 250;
	private static final String MEDIA_TYPE = MediaType.APPLICATION_JSON + ";charset=UTF-8";
	private static final String RETRY_FAILED_MESSAGE = "Request retry has failed.";

	private static final Client CLIENT = ClientBuilder.newClient().register(JacksonFeature.class)
			.property(ClientProperties.CONNECT_TIMEOUT, 60000).property(ClientProperties.READ_TIMEOUT, 600000).register(
					new JacksonJaxbJsonProvider().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));

	private static final Logger LOGGER = LoggerFactory.getLogger(BigcommerceSdk.class);

	private final WebTarget baseWebTarget;
	/*
	 * Bigcommerce API has some differences between V2 & V3 and currently some
	 * API endpoints only exist in V2. The SDK uses V2 where applicable.
	 */
	private final WebTarget baseWebTargetV2;
	private final String storeHash;
	private final String clientId;
	private final String accessToken;
	private final long requestRetryTimeoutDuration;
	private final TimeUnit requestRetryTimeoutUnit;
	public static final String RFC_822_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss Z";

	public static interface ApiUrlStep {
		RequestRetryTimeoutStep withApiUrl(final String apiUrl);
	}

	public static interface RequestRetryTimeoutStep {
		StoreHashStep withRequestRetryTimeout(final long requestRetryTimeoutDuration,
				final TimeUnit requestRetryTimeoutUnit);
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
		final WebTarget webTarget = baseWebTarget.path(CATALOG).path(SUMMARY);
		final CatalogSummaryResponse catalogSummaryResponse = get(webTarget, CatalogSummaryResponse.class);
		return catalogSummaryResponse.getData();

	}

	public Products getProducts(final int page) {
		return getProducts(page, MAX_LIMIT);
	}

	public Products getProducts(final int page, final int limit) {
		final WebTarget webTarget = baseWebTarget.path(CATALOG).path(PRODUCTS).queryParam(INCLUDE, VARIANTS)
				.queryParam(LIMIT, limit).queryParam(PAGE, page);
		final ProductsResponse productsResponse = get(webTarget, ProductsResponse.class);
		final List<Product> products = productsResponse.getData();
		final Pagination pagination = productsResponse.getMeta().getPagination();
		return new Products(products, pagination);
	}

	public List<Order> getOrders(final int page) {
		return getOrders(page, MAX_LIMIT);
	}

	public List<Order> getOrders(final int page, final int limit) {
		final WebTarget webTarget = baseWebTargetV2.path(ORDERS).queryParam(LIMIT, limit).queryParam(PAGE, page);

		final List<Order> orders = getList(webTarget, Order.class);
		return orders;
	}

	public Variant updateVariant(final Variant variant) {
		final WebTarget webTarget = baseWebTarget.path(CATALOG).path(PRODUCTS).path(variant.getProductId())
				.path(VARIANTS).path(variant.getId());
		final VariantResponse variantResponse = put(webTarget, variant, VariantResponse.class);
		return variantResponse.getData();
	}

	private BigcommerceSdk(final Steps steps) {
		this.baseWebTarget = CLIENT.target(steps.apiUrl).path(steps.storeHash).path(API_VERSION);
		this.baseWebTargetV2 = CLIENT.target(steps.apiUrl).path(steps.storeHash).path(API_VERSION_V2);
		this.storeHash = steps.storeHash;
		this.clientId = steps.clientId;
		this.accessToken = steps.accessToken;
		this.requestRetryTimeoutDuration = steps.requestRetryTimeoutDuration;
		this.requestRetryTimeoutUnit = steps.requestRetryTimeoutUnit;
	}

	private static class Steps
			implements ApiUrlStep, RequestRetryTimeoutStep, StoreHashStep, ClientIdStep, AccessTokenStep, BuildStep {

		private String apiUrl = "https://api.bigcommerce.com/stores";
		private long requestRetryTimeoutDuration = 5;
		private TimeUnit requestRetryTimeoutUnit = TimeUnit.MINUTES;
		private String storeHash;
		private String clientId;
		private String accessToken;

		@Override
		public RequestRetryTimeoutStep withApiUrl(final String apiUrl) {
			this.apiUrl = apiUrl;
			return this;
		}

		@Override
		public StoreHashStep withRequestRetryTimeout(final long requestRetryTimeoutDuration,
				final TimeUnit requestRetryTimeoutUnit) {
			this.requestRetryTimeoutDuration = requestRetryTimeoutDuration;
			this.requestRetryTimeoutUnit = requestRetryTimeoutUnit;
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

	private <T> T get(final WebTarget webTarget, final Class<T> entityType) {
		final Callable<Response> responseCallable = new Callable<Response>() {
			@Override
			public Response call() throws Exception {
				return webTarget.request().header(CLIENT_ID_HEADER, getClientId())
						.header(ACESS_TOKEN_HEADER, getAccessToken()).header("Accept", "application/json").get();
			}
		};
		final Response response = invokeResponseCallable(responseCallable);
		return handleResponse(response, entityType, Status.OK);
	}

	private <T> List<T> getList(final WebTarget webTarget, final Class<T> entityType) {
		final Callable<Response> responseCallable = new Callable<Response>() {
			@Override
			public Response call() throws Exception {
				return webTarget.request().header(CLIENT_ID_HEADER, getClientId())
						.header(ACESS_TOKEN_HEADER, getAccessToken()).header("Accept", "application/json")
						.get(Response.class);
			}
		};
		final Response response = invokeResponseCallable(responseCallable);
		return handleResponseGeneric(response, entityType, Status.OK);

	}

	private <T, V> V put(final WebTarget webTarget, final T object, final Class<V> entityType) {
		final Callable<Response> responseCallable = new Callable<Response>() {
			@Override
			public Response call() throws Exception {
				final Entity<T> entity = Entity.entity(object, MEDIA_TYPE);
				return webTarget.request().header(CLIENT_ID_HEADER, getClientId())
						.header(ACESS_TOKEN_HEADER, getAccessToken()).put(entity);
			}
		};
		final Response response = invokeResponseCallable(responseCallable);
		return handleResponse(response, entityType, Status.OK);
	}

	private Response invokeResponseCallable(final Callable<Response> responseCallable) {
		final Retryer<Response> retryer = buildResponseRetyer();
		try {
			return retryer.call(responseCallable);
		} catch (ExecutionException | RetryException e) {
			throw new BigcommerceException(RETRY_FAILED_MESSAGE, e);
		}
	}

	private Retryer<Response> buildResponseRetyer() {
		return RetryerBuilder.<Response>newBuilder().retryIfResult(this::shouldRetryResponse)
				.withWaitStrategy(new ResponseWaitStrategy())
				.withStopStrategy(StopStrategies.stopAfterDelay(requestRetryTimeoutDuration, requestRetryTimeoutUnit))
				.build();
	}

	private boolean shouldRetryResponse(final Response response) {
		return (Status.Family.SERVER_ERROR == Status.Family.familyOf(response.getStatus()))
				|| (TOO_MANY_REQUESTS_STATUS_CODE == response.getStatus());
	}

	private <T> T handleResponse(final Response response, final Class<T> entityType, final Status expectedStatus) {
		if (expectedStatus.getStatusCode() == response.getStatus()) {
			return response.readEntity(entityType);
		}
		throw new BigcommerceErrorResponseException(response);
	}

	private <T> List<T> handleResponseGeneric(final Response response, final Class<T> entityType,
			final Status expectedStatus) {

		if (expectedStatus.getStatusCode() == response.getStatus()) {
			ParameterizedType parameterizedGenericType = new ParameterizedType() {
				@Override
				public Type[] getActualTypeArguments() {
					return new Type[] { entityType };
				}

				@Override
				public Type getRawType() {
					return List.class;
				}

				@Override
				public Type getOwnerType() {
					return List.class;
				}
			};
			GenericType<List<T>> genericType = new GenericType<List<T>>(parameterizedGenericType) {
			};

			List<T> list = response.readEntity(genericType);
			return list;
		}

		throw new BigcommerceErrorResponseException(response);
	}

	private class ResponseWaitStrategy implements WaitStrategy {
		private static final String SLEEPING_BEFORE_RETRY_DUE_TO_RATE_LIMIT_MESSAGE = "Sleeping %s before trying request again. Exceeded rate limit.";
		private static final String SLEEPING_BEFORE_RETRY_MESSAGE = "Sleeping %s before trying request again. Received unexpected status code of %s and body of:\n%s";

		private final WaitStrategy fallbackWaitStrategy = WaitStrategies.fibonacciWait();

		@Override
		public long computeSleepTime(@SuppressWarnings("rawtypes") final Attempt failedAttempt) {
			if (failedAttempt.hasResult()) {
				if (failedAttempt.getResult() instanceof Response) {
					final Response response = (Response) failedAttempt.getResult();
					final String rateLimitTimeResetString = response.getHeaderString(RATE_LIMIT_TIME_RESET_HEADER);
					if (TOO_MANY_REQUESTS_STATUS_CODE == response.getStatus() && rateLimitTimeResetString != null) {
						final long sleepTime = Long.valueOf(rateLimitTimeResetString);
						LOGGER.warn(String.format(SLEEPING_BEFORE_RETRY_DUE_TO_RATE_LIMIT_MESSAGE,
								Duration.ofMillis(sleepTime).toString()));
						return sleepTime;
					}
					response.bufferEntity();
					final long sleepTime = fallbackWaitStrategy.computeSleepTime(failedAttempt);
					LOGGER.warn(String.format(SLEEPING_BEFORE_RETRY_MESSAGE, Duration.ofMillis(sleepTime).toString(),
							response.getStatus(), response.readEntity(String.class)));
					return sleepTime;
				}
			}
			return 0;
		}
	}
}
