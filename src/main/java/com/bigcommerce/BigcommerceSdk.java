package com.bigcommerce;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.bigcommerce.catalog.models.*;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	static final String API_VERSION_V3 = "v3";
	static final String CLIENT_ID_HEADER = "X-Auth-Client";
	static final String ACCESS_TOKEN_HEADER = "X-Auth-Token";
	static final String RATE_LIMIT_TIME_RESET_HEADER = "X-Rate-Limit-Time-Reset-Ms";

	static final int TOO_MANY_REQUESTS_STATUS_CODE = 429;

	public static final String VARIANTS = "variants";
	public static final String CUSTOM_FIELDS = "custom_fields";

	private static final String CATALOG = "catalog";
	private static final String CATEGORIES= "categories";
	private static final String SUMMARY = "summary";
	private static final String PRODUCTS = "products";
	private static final String CUSTOM_FIELDS_PATH = "custom-fields";
	private static final String BRANDS = "brands";
	private static final String ORDERS = "orders";
	private static final String CUSTOMERS = "customers";
	private static final String LIMIT = "limit";
	private static final String PAGE = "page";
	private static final String INCLUDE = "include";
	private static final String SHIPPINGADDRESSES = "shipping_addresses";
	private static final String SHIPMENTS = "shipments";
	private static final String MIN_DATE_CREATED = "min_date_created";
	private static final String STORE = "store";
	private static final String METAFIELDS = "metafields";
	private static final String IMAGES = "images";
	private static final int MAX_LIMIT = 250;
	private static final String MEDIA_TYPE = MediaType.APPLICATION_JSON + ";charset=UTF-8";
	private static final String RETRY_FAILED_MESSAGE = "Request retry has failed.";
	private static final String TREE = "tree";

	private static final Logger LOGGER = LoggerFactory.getLogger(BigcommerceSdk.class);

	private static final Client CLIENT = ClientBuilder.newClient().register(JacksonFeature.class)
			.property(ClientProperties.CONNECT_TIMEOUT, 60000).property(ClientProperties.READ_TIMEOUT, 600000).register(
					new JacksonJaxbJsonProvider().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));
	private final WebTarget baseWebTargetV3;

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
		final WebTarget webTarget = baseWebTargetV3.path(CATALOG).path(SUMMARY);
		final CatalogSummaryResponse catalogSummaryResponse = get(webTarget, CatalogSummaryResponse.class);
		return catalogSummaryResponse.getData();
	}

	public Products getProducts(final int page) {
		return getProducts(page, MAX_LIMIT);
	}

	public Products getProducts(final int page, String... includes) {
		return getProducts(page, MAX_LIMIT, includes);
	}

	public Products getProducts(final int page, final int limit) {
		return getProducts(page, limit, VARIANTS);
	}

	public Products getProducts(final int page, final int limit, String... includes) {
		WebTarget webTarget = baseWebTargetV3.path(CATALOG)
			.path(PRODUCTS)
			.queryParam(LIMIT, limit)
			.queryParam(PAGE, page)
			.queryParam(INCLUDE, String.join("", includes));

		final ProductsResponse productsResponse = get(webTarget, ProductsResponse.class);
		final List<Product> products = productsResponse.getData();
		final Pagination pagination = productsResponse.getMeta().getPagination();
		return new Products(products, pagination);
	}

	public Category createCategory(final Category category) {
		final WebTarget webTarget = baseWebTargetV3.path(CATALOG).path(CATEGORIES);
		final CategoryResponse categoryResponse = post(webTarget, category, CategoryResponse.class);

		return categoryResponse.getData();
	}

	public Category updateCategory(final Category category) {
		final WebTarget webTarget = baseWebTargetV3.path(CATALOG).path(CATEGORIES).path(String.valueOf(category.getId()));
		final CategoryResponse categoryResponse = put(webTarget, category, CategoryResponse.class);

		return categoryResponse.getData();
	}

	public Categories getCategories(final int page) {
		return getCategories(page, MAX_LIMIT);
	}

	public Categories getCategories(final int page, final int limit){
		final WebTarget webTarget = baseWebTargetV3.path(CATALOG).path(CATEGORIES)
				.queryParam(LIMIT, limit).queryParam(PAGE, page);
		final CategoriesResponse categoriesResponse = get(webTarget, CategoriesResponse.class);
		final List<Category> categories= categoriesResponse.getData();
		final Pagination pagination = categoriesResponse.getMeta().getPagination();
		return new Categories(categories, pagination);
	}

	public Categories getCategoriesAsTree() {
		final WebTarget webTarget = baseWebTargetV3.path(CATALOG).path(CATEGORIES).path(TREE);
		final CategoriesResponse categoriesResponse = get(webTarget, CategoriesResponse.class);
		final List<Category> categories= categoriesResponse.getData();
		final Pagination pagination = categoriesResponse.getMeta().getPagination();
		return new Categories(categories, pagination);
	}

	public Variants getVariants(final int page) {
		return getVariants(page, MAX_LIMIT);
	}

	public Variants getVariants(final int page, final int limit) {
		final WebTarget webTarget = baseWebTargetV3.path(CATALOG).path(VARIANTS)
				.queryParam(LIMIT, limit).queryParam(PAGE, page);

		final VariantsResponse variantsResponse = get(webTarget, VariantsResponse.class);
		final List<Variant> variants = variantsResponse.getData();
		final Pagination pagination = variantsResponse.getMeta().getPagination();
		return new Variants(variants, pagination);
	}

	public Product createProduct(final Product product) {
		final WebTarget webTarget = baseWebTargetV3.path(CATALOG).path(PRODUCTS);
		final ProductResponse productResponse = post(webTarget, product, ProductResponse.class);

		return productResponse.getData();
	}

	public Product updateProduct(final Product product) {
		final WebTarget webTarget = baseWebTargetV3.path(CATALOG).path(PRODUCTS).path(String.valueOf(product.getId()));
		final ProductResponse productResponse = put(webTarget, product, ProductResponse.class);
		return productResponse.getData();
	}

	public Variant createVariant(final Variant variant) {
		final WebTarget webTarget = baseWebTargetV3.path(CATALOG).path(PRODUCTS)
				.path(String.valueOf(variant.getProductId())).path(VARIANTS);
		final VariantResponse variantResponse = post(webTarget, variant, VariantResponse.class);
		return variantResponse.getData();
	}

	public void deleteVariant(final Integer productId, final Integer id) {
		final WebTarget webTarget = baseWebTargetV3.path(CATALOG).path(PRODUCTS).path(String.valueOf(productId))
				.path(VARIANTS).path(String.valueOf(id));
		delete(webTarget, Object.class);
	}

	public ProductImage createProductImage(final ProductImage productImage) {
		final WebTarget webTarget = baseWebTargetV3.path(CATALOG).path(PRODUCTS)
				.path(String.valueOf(productImage.getProductId())).path(IMAGES);
		final ProductImageResponse productImageResponse = post(webTarget, productImage, ProductImageResponse.class);
		return productImageResponse.getData();
	}

	public ProductImage updateProductImage(final ProductImage productImage) {
		final WebTarget webTarget = baseWebTargetV3.path(CATALOG).path(PRODUCTS)
				.path(String.valueOf(productImage.getProductId())).path(IMAGES)
				.path(String.valueOf(productImage.getId()));
		final ProductImageResponse productImageResponse = put(webTarget, productImage, ProductImageResponse.class);

		return productImageResponse.getData();
	}

	public void deleteProduct(final Integer productId) {
		final WebTarget webTarget = baseWebTargetV3.path(CATALOG).path(PRODUCTS).path(String.valueOf(productId));
		delete(webTarget, Object.class);
	}

	public void deleteProductCustomField(final Integer productId, final Integer customFieldId) {
		final WebTarget webTarget = baseWebTargetV3
			.path(CATALOG)
			.path(PRODUCTS)
			.path(String.valueOf(productId))
			.path(CUSTOM_FIELDS_PATH)
			.path(String.valueOf(customFieldId));
		delete(webTarget, Object.class);
	}

	public CustomFieldResponse createProductCustomField(final Integer productId, final CustomField customField) {
		final WebTarget webTarget = baseWebTargetV3
			.path(CATALOG)
			.path(PRODUCTS)
			.path(String.valueOf(productId))
			.path(CUSTOM_FIELDS_PATH);
		return post(webTarget, customField, CustomFieldResponse.class);
	}

	public ProductImages getProductImages(final Integer productId, final int page) {
		return getProductImages(productId, page, MAX_LIMIT);
	}

	public ProductImages getProductImages(final Integer productId, final int page, final int limit) {
		final WebTarget webTarget = baseWebTargetV3.path(CATALOG).path(PRODUCTS).path(String.valueOf(productId))
				.path(IMAGES).queryParam(LIMIT, limit).queryParam(PAGE, page);
		final ProductImagesResponse productImagesResponse = get(webTarget, ProductImagesResponse.class);
		final List<ProductImage> productImages = productImagesResponse.getData();
		final Pagination pagination = productImagesResponse.getMeta().getPagination();
		return new ProductImages(productImages, pagination);
	}

	public void deleteProductImage(final Integer productId, final Integer productImageId) {
		final WebTarget webTarget = baseWebTargetV3.path(CATALOG).path(PRODUCTS).path(String.valueOf(productId))
				.path(IMAGES).path(String.valueOf(productImageId));
		delete(webTarget, Object.class);
	}

	public List<Order> getOrders(final int page, final DateTime earliestDate) {
		return getOrders(page, MAX_LIMIT, earliestDate);
	}

	public List<Order> getOrders(final int page) {
		return getOrders(page, MAX_LIMIT, null);
	}

	public List<Order> getOrders(final int page, final int limit, final DateTime earliestDate) {
		final WebTarget webTarget = baseWebTargetV2.path(ORDERS).queryParam(LIMIT, limit).queryParam(PAGE, page)
				.queryParam(MIN_DATE_CREATED, earliestDate);
		final OrdersResponse ordersResponse = get(webTarget, OrdersResponse.class);

		return (ordersResponse == null) ? Collections.emptyList() : ordersResponse;
	}

	public Order getOrder(final int orderId) {
		final WebTarget webTarget = baseWebTargetV2.path(ORDERS).path(String.valueOf(orderId));
		return get(webTarget, Order.class);

	}

	public Order completeOrder(final int orderId) {
		final WebTarget webTarget = baseWebTargetV2.path(ORDERS).path(String.valueOf(orderId));

		final Order order = new Order();

		order.setStatusId(getStatus(com.bigcommerce.catalog.models.Status.COMPLETED).getId());
		return put(webTarget, order, Order.class);
	}

	public Order cancelOrder(final int orderId) {
		final WebTarget webTarget = baseWebTargetV2.path(ORDERS).path(String.valueOf(orderId));

		final Order order = new Order();

		order.setStatusId(getStatus(com.bigcommerce.catalog.models.Status.CANCELLED).getId());
		return put(webTarget, order, Order.class);
	}

	public OrderStatus getStatus(final com.bigcommerce.catalog.models.Status statusName) {
		final WebTarget webTarget = baseWebTargetV2.path("order_statuses");

		final OrderStatusResponse statusResponse = get(webTarget, OrderStatusResponse.class);
		return statusResponse.stream().filter(status -> status.getName().equals(statusName.toString())).findFirst()
				.get();
	}

	public List<LineItem> getLineItems(final int orderId, final int page) {
		return getLineItems(orderId, page, MAX_LIMIT);
	}

	public List<LineItem> getLineItems(final int orderId, final int page, final int limit) {
		final WebTarget webTarget = baseWebTargetV2.path(ORDERS).path(String.valueOf(orderId)).path(PRODUCTS)
				.queryParam(LIMIT, limit).queryParam(PAGE, page);
		final LineItemsResponse lineItems = get(webTarget, LineItemsResponse.class);
		return (lineItems == null) ? Collections.emptyList() : lineItems;
	}

	public Address getShippingAddress(final int orderId) {
		final WebTarget webTarget = baseWebTargetV2.path(ORDERS).path(String.valueOf(orderId)).path(SHIPPINGADDRESSES);
		final AddressResponse addressResponse = get(webTarget, AddressResponse.class);
		return (addressResponse == null) ? null : addressResponse.get(0);
	}

	public List<Shipment> getShipments(final int orderId, final int page) {
		return getShipments(orderId, page, MAX_LIMIT);
	}

	public List<Shipment> getShipments(final int orderId, final int page, final int limit) {
		final WebTarget webTarget = baseWebTargetV2.path(ORDERS).path(String.valueOf(orderId)).path(SHIPMENTS)
				.queryParam(LIMIT, limit).queryParam(PAGE, page);
		final ShipmentResponse shipments = get(webTarget, ShipmentResponse.class);
		return (shipments == null) ? Collections.emptyList() : shipments;
	}

	public Store getStore() {
		final WebTarget webTarget = baseWebTargetV2.path(STORE);

		return get(webTarget, Store.class);

	}

	public Customer getCustomer(final int customerId) {
		final WebTarget webTarget = baseWebTargetV2.path(CUSTOMERS).path(String.valueOf(customerId));
		return get(webTarget, Customer.class);
	}

	public Variant updateVariant(final Variant variant) {
		final WebTarget webTarget = baseWebTargetV3.path(CATALOG).path(PRODUCTS)
				.path(String.valueOf(variant.getProductId())).path(VARIANTS).path(String.valueOf(variant.getId()));
		final VariantResponse variantResponse = put(webTarget, variant, VariantResponse.class);
		return variantResponse.getData();
	}

	public Brands getBrands(final int page) {
		return getBrands(page, MAX_LIMIT);
	}

	public Brands getBrands(final int page, final int limit) {
		final WebTarget webTarget = baseWebTargetV3.path(CATALOG).path(BRANDS).queryParam(LIMIT, limit).queryParam(PAGE,
				page);
		final BrandsResponse brandsResponse = get(webTarget, BrandsResponse.class);
		final List<Brand> brands = brandsResponse.getData();
		final Pagination pagination = brandsResponse.getMeta().getPagination();
		return new Brands(brands, pagination);
	}

	public Brand createBrand(final Brand brand) {
		final WebTarget webTarget = baseWebTargetV3.path(CATALOG).path(BRANDS);
		final BrandResponse createdBrand = post(webTarget, brand, BrandResponse.class);
		return createdBrand.getData();
	}

	public Shipment createShipment(final ShipmentCreationRequest shipmentCreationRequest, final int orderId) {
		final WebTarget webTarget = baseWebTargetV2.path(ORDERS).path(String.valueOf(orderId)).path(SHIPMENTS);

		return post(webTarget, shipmentCreationRequest.getRequest(), Shipment.class);

	}

	public Shipment updateShipment(final ShipmentUpdateRequest shipmentUpdateRequest, final int orderId,
			final int shipmentId) {
		final WebTarget webTarget = baseWebTargetV2.path(ORDERS).path(String.valueOf(orderId)).path(SHIPMENTS)
				.path(String.valueOf(shipmentId));

		return put(webTarget, shipmentUpdateRequest.getRequest(), Shipment.class);
	}

	public Metafields getProductMetafields(final Integer productId, final int page) {
		return getProductMetafields(productId, page, MAX_LIMIT);
	}

	public Metafields getProductMetafields(final Integer productId, final int page, final int limit) {
		final WebTarget webTarget = baseWebTargetV3.path(CATALOG).path(PRODUCTS).path(String.valueOf(productId))
				.path(METAFIELDS).queryParam(LIMIT, limit).queryParam(PAGE, page);
		final MetafieldsResponse metaFieldsResponse = get(webTarget, MetafieldsResponse.class);
		final List<Metafield> metafields = metaFieldsResponse.getData();
		final Pagination pagination = metaFieldsResponse.getMeta().getPagination();
		return new Metafields(metafields, pagination);
	}

	public Metafield createProductMetafield(final Integer productId, final Metafield metafield) {
		final WebTarget webTarget = baseWebTargetV3.path(CATALOG).path(PRODUCTS).path(String.valueOf(productId))
				.path(METAFIELDS);
		final MetafieldResponse metafieldResponse = post(webTarget, metafield, MetafieldResponse.class);
		return metafieldResponse.getData();
	}

	public Metafield updateProductMetafield(final Integer productId, final Integer productMetafieldId,
			final Metafield metafield) {
		final WebTarget webTarget = baseWebTargetV3.path(CATALOG).path(PRODUCTS).path(String.valueOf(productId))
				.path(METAFIELDS).path(String.valueOf(productMetafieldId));
		final MetafieldResponse metafieldResponse = put(webTarget, metafield, MetafieldResponse.class);
		return metafieldResponse.getData();
	}

	private BigcommerceSdk(final Steps steps) {
		this.baseWebTargetV3 = CLIENT.target(steps.apiUrl).path(steps.storeHash).path(API_VERSION_V3);
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
		final Callable<Response> responseCallable = () -> webTarget.request(MediaType.APPLICATION_JSON)
				.header(CLIENT_ID_HEADER, getClientId()).header(ACCESS_TOKEN_HEADER, getAccessToken()).get();
		final Response response = invokeResponseCallable(responseCallable);
		return handleResponse(response, entityType, Status.OK, Status.NO_CONTENT);
	}

	private <T> T delete(final WebTarget webTarget, final Class<T> entityType) {
		final Callable<Response> responseCallable = () -> webTarget.request(MediaType.APPLICATION_JSON)
				.header(CLIENT_ID_HEADER, getClientId()).header(ACCESS_TOKEN_HEADER, getAccessToken()).delete();
		final Response response = invokeResponseCallable(responseCallable);
		return handleResponse(response, entityType, Status.OK, Status.NO_CONTENT);
	}

	private <T, V> V put(final WebTarget webTarget, final T object, final Class<V> entityType) {
		final Callable<Response> responseCallable = () -> {
			final Entity<T> entity = Entity.entity(object, MEDIA_TYPE);
			return webTarget.request().header(CLIENT_ID_HEADER, getClientId())
					.header(ACCESS_TOKEN_HEADER, getAccessToken()).put(entity);
		};
		final Response response = invokeResponseCallable(responseCallable);
		return handleResponse(response, entityType, Status.OK);
	}

	private <T, V> V post(final WebTarget webTarget, final T object, final Class<V> entityType) {
		final Callable<Response> responseCallable = () -> {
			final Entity<T> entity = Entity.entity(object, MEDIA_TYPE);
			return webTarget.request().header(CLIENT_ID_HEADER, getClientId())
					.header(ACCESS_TOKEN_HEADER, getAccessToken()).post(entity);
		};
		final Response response = invokeResponseCallable(responseCallable);
		return handleResponse(response, entityType, Status.OK, Status.CREATED);
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

	private <T> T handleResponse(final Response response, final Class<T> entityType, final Status... expectedStatuses) {
		final List<Integer> expectedStatusCodes = Arrays.asList(expectedStatuses).stream().map(Status::getStatusCode)
				.collect(Collectors.toList());
		if (expectedStatusCodes.contains(response.getStatus())) {
			return response.readEntity(entityType);
		}
		throw new BigcommerceErrorResponseException(response);
	}

	private class ResponseWaitStrategy implements WaitStrategy {
		private static final String SLEEPING_BEFORE_RETRY_DUE_TO_RATE_LIMIT_MESSAGE = "Sleeping {} before trying request again. Exceeded rate limit.";
		private static final String SLEEPING_BEFORE_RETRY_MESSAGE = "Sleeping {} before trying request again. Received unexpected status code of {} and body of:\n{}";

		private final WaitStrategy fallbackWaitStrategy = WaitStrategies.fibonacciWait();

		@Override
		public long computeSleepTime(@SuppressWarnings("rawtypes") final Attempt failedAttempt) {
			if (failedAttempt.hasResult()) {
				if (failedAttempt.getResult() instanceof Response) {
					final Response response = (Response) failedAttempt.getResult();
					final String rateLimitTimeResetString = response.getHeaderString(RATE_LIMIT_TIME_RESET_HEADER);
					if (TOO_MANY_REQUESTS_STATUS_CODE == response.getStatus() && rateLimitTimeResetString != null) {
						final long sleepTime = Long.parseLong(rateLimitTimeResetString);
						LOGGER.warn(SLEEPING_BEFORE_RETRY_DUE_TO_RATE_LIMIT_MESSAGE,
								Duration.ofMillis(sleepTime).toString());
						return sleepTime;
					}
					response.bufferEntity();
					final long sleepTime = fallbackWaitStrategy.computeSleepTime(failedAttempt);
					LOGGER.warn(SLEEPING_BEFORE_RETRY_MESSAGE, Duration.ofMillis(sleepTime).toString(),
							response.getStatus(), response.readEntity(String.class));
					return sleepTime;
				}
			}
			return 0;
		}
	}

}
