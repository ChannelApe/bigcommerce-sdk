package com.bigcommerce;

import static com.github.restdriver.clientdriver.RestClientDriver.giveEmptyResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.giveResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Rule;
import org.junit.Test;

import com.bigcommerce.catalog.models.Address;
import com.bigcommerce.catalog.models.CatalogSummary;
import com.bigcommerce.catalog.models.CatalogSummaryResponse;
import com.bigcommerce.catalog.models.Customer;
import com.bigcommerce.catalog.models.DateTimeAdapter;
import com.bigcommerce.catalog.models.LineItem;
import com.bigcommerce.catalog.models.LineItemsResponse;
import com.bigcommerce.catalog.models.Meta;
import com.bigcommerce.catalog.models.Order;
import com.bigcommerce.catalog.models.Pagination;
import com.bigcommerce.catalog.models.Product;
import com.bigcommerce.catalog.models.Products;
import com.bigcommerce.catalog.models.ProductsResponse;
import com.bigcommerce.catalog.models.Shipment;
import com.bigcommerce.catalog.models.ShipmentLineItem;
import com.bigcommerce.catalog.models.Store;
import com.bigcommerce.catalog.models.Variant;
import com.bigcommerce.catalog.models.VariantResponse;
import com.bigcommerce.exceptions.BigcommerceErrorResponseException;
import com.bigcommerce.exceptions.BigcommerceException;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.restdriver.clientdriver.ClientDriverRequest.Method;
import com.github.restdriver.clientdriver.ClientDriverRule;
import com.github.restdriver.clientdriver.capture.JsonBodyCapture;

public class BigcommerceSdkTest {

	private static final String SOME_BIN_PICKING_NUMBER = "12345";
	private static final int SOME_QUANTITY = 10;
	private static final String SOME_NAME = "SOME NAME";
	private static final String SOME_LAST_NAME = "Kazokas";
	private static final String SOME_FIRST_NAME = "Ryan";
	private static final String SOME_EMAIL = "rkazokas@channelape.com";
	private static final int SOME_ID = 1;
	private static final String SOME_CURRENCY_EXCHANGE = "Currency Exchange";
	private static final String SOME_CURRENCY_CODE = "USD";
	private static final String SOME_CREDIT_CARD_TYPE = "VISA";
	private static final String SOME_COUPON_DISCOUNT = "Coupon Discount";
	private static final String SOME_DATE_STRING = "Tue, 30 May 2017 17:14:57 +0000";
	private static final BigDecimal SOME_PRICE = new BigDecimal(0.0200);
	private static final String SOME_STORE_HASH = "adf242xsfw";
	private static final String SOME_CLIENT_ID = "asdf2423425235090141";
	private static final String SOME_ACCESS_TOKEN = "31165465441dafsdf";
	private static final char FORWARD_SLASH = '/';

	@Rule
	public ClientDriverRule driver = new ClientDriverRule();

	@Test
	public void givenStoreHashAndClientIdAndAccessTokenWhenBuildingBigcommerceSdkThenReturnBigcommerceSdkWithCorrectValues() {
		final BigcommerceSdk actualBigcommerceSdk = BigcommerceSdk.newBuilder().withStoreHash(SOME_STORE_HASH)
				.withClientId(SOME_CLIENT_ID).withAccessToken(SOME_ACCESS_TOKEN).build();

		assertEquals(SOME_STORE_HASH, actualBigcommerceSdk.getStoreHash());
		assertEquals(SOME_CLIENT_ID, actualBigcommerceSdk.getClientId());
		assertEquals(SOME_ACCESS_TOKEN, actualBigcommerceSdk.getAccessToken());
	}

	@Test
	public void givenValidCredentialsWhenRetrievingCatalogSummaryThenReturnCatalogSummary() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION).append(FORWARD_SLASH)
				.append("catalog/summary").toString();

		final CatalogSummaryResponse catalogSummaryResponse = new CatalogSummaryResponse();
		final CatalogSummary expectedCatalogSummary = new CatalogSummary();
		expectedCatalogSummary.setHighestVariantPrice(BigDecimal.valueOf(20.45));
		expectedCatalogSummary.setInventoryCount(33);
		expectedCatalogSummary.setAverageVariantPrice(BigDecimal.valueOf(44.3));
		expectedCatalogSummary.setInventoryValue(BigDecimal.valueOf(1338));
		expectedCatalogSummary.setVariantCount(99);
		catalogSummaryResponse.setData(expectedCatalogSummary);

		final JAXBContext jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { CatalogSummaryResponse.class }, null);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(catalogSummaryResponse, stringWriter);
		final String expectedResponseBodyString = stringWriter.toString();

		final Status expectedStatus = Status.OK;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.GET),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		final CatalogSummary actualCatalogSummary = bigcommerceSdk.getCatalogSummary();

		assertEquals(expectedCatalogSummary.getHighestVariantPrice(), actualCatalogSummary.getHighestVariantPrice());
		assertEquals(expectedCatalogSummary.getInventoryCount(), actualCatalogSummary.getInventoryCount());
		assertEquals(expectedCatalogSummary.getVariantCount(), actualCatalogSummary.getVariantCount());
		assertEquals(expectedCatalogSummary.getAverageVariantPrice(), actualCatalogSummary.getAverageVariantPrice());
		assertEquals(expectedCatalogSummary.getInventoryValue(), actualCatalogSummary.getInventoryValue());
	}

	@Test(expected = BigcommerceErrorResponseException.class)
	public void givenInvalidCredentialsWhenRetrievingCatalogSummaryThenThrowNewBigcommerceErrorResponseException() {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION).append(FORWARD_SLASH)
				.append("catalog/summary").toString();

		final Status expectedStatus = Status.UNAUTHORIZED;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.GET),
				giveEmptyResponse().withStatus(expectedStatusCode));

		bigcommerceSdk.getCatalogSummary();
	}

	@Test
	public void givenSomePageWhenRetrievingProductsThenReturnProducts() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION).append(FORWARD_SLASH)
				.append("catalog/products").toString();

		final ProductsResponse productsResponse = new ProductsResponse();
		final Product firstExpectedProduct = new Product();
		firstExpectedProduct.setId("112");
		firstExpectedProduct.setName("Quest Nutrition Quest Natural Protein Bar");
		final Product secondExpectedProduct = new Product();
		secondExpectedProduct.setId("113");
		secondExpectedProduct.setName("Rubber Duck");
		final List<Product> expectedProducts = Arrays.asList(firstExpectedProduct, secondExpectedProduct);
		productsResponse.setData(expectedProducts);
		final Meta meta = new Meta();
		final Pagination expectedPagination = new Pagination();
		expectedPagination.setCurrentPage(1);
		expectedPagination.setPerPage(250);
		expectedPagination.setTotal(10);
		expectedPagination.setTotalPages(5);
		meta.setPagination(expectedPagination);
		productsResponse.setMeta(meta);

		final JAXBContext jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { ProductsResponse.class }, null);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(productsResponse, stringWriter);
		final String expectedResponseBodyString = stringWriter.toString();

		final Status expectedStatus = Status.OK;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		driver.addExpectation(onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
				.withHeader(BigcommerceSdk.ACESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withParam("include", "variants")
				.withParam("page", 1).withParam("limit", 250).withMethod(Method.GET),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		final Products actualProducts = bigcommerceSdk.getProducts(1);

		assertEquals(expectedProducts.size(), actualProducts.getProducts().size());
		assertEquals(firstExpectedProduct.getId(), actualProducts.getProducts().get(0).getId());
		assertEquals(firstExpectedProduct.getName(), actualProducts.getProducts().get(0).getName());
		assertEquals(secondExpectedProduct.getName(), actualProducts.getProducts().get(1).getName());
		assertEquals(secondExpectedProduct.getId(), actualProducts.getProducts().get(1).getId());
		assertEquals(expectedPagination.getCount(), actualProducts.getPagination().getCount());
		assertEquals(expectedPagination.getCurrentPage(), actualProducts.getPagination().getCurrentPage());
		assertEquals(expectedPagination.getPerPage(), actualProducts.getPagination().getPerPage());
		assertEquals(expectedPagination.getTotal(), actualProducts.getPagination().getTotal());
		assertEquals(expectedPagination.getTotalPages(), actualProducts.getPagination().getTotalPages());
	}

	@Test(expected = BigcommerceException.class)
	public void givenSomePageAndBigCommerceReturnsInternalServerErrorWhenRetrievingProductsThenRetryAndEventuallyThrowNewBigcommerceException() {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION).append(FORWARD_SLASH)
				.append("catalog/products").toString();

		final Status expectedStatus = Status.INTERNAL_SERVER_ERROR;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		driver.addExpectation(onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
				.withHeader(BigcommerceSdk.ACESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withParam("include", "variants")
				.withParam("page", 1).withParam("limit", 250).withMethod(Method.GET),
				giveEmptyResponse().withStatus(expectedStatusCode)).anyTimes();

		bigcommerceSdk.getProducts(1);
	}

	@Test(expected = BigcommerceException.class)
	public void givenSomePageAndBigCommerceReturnsTooManyRequestsWhenRetrievingProductsThenRetryAndEventuallyThrowNewBigcommerceException() {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION).append(FORWARD_SLASH)
				.append("catalog/products").toString();

		final int expectedStatusCode = BigcommerceSdk.TOO_MANY_REQUESTS_STATUS_CODE;
		driver.addExpectation(onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
				.withHeader(BigcommerceSdk.ACESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withParam("include", "variants")
				.withParam("page", 1).withParam("limit", 250).withMethod(Method.GET),
				giveEmptyResponse().withStatus(expectedStatusCode)
						.withHeader(BigcommerceSdk.RATE_LIMIT_TIME_RESET_HEADER, "5"))
				.anyTimes();

		bigcommerceSdk.getProducts(1);
	}

	@Test
	public void givenSomeVariantWhenUpdatingVariantThenUpdateVariant() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final Variant variant = new Variant();
		final String variantId = "78";
		variant.setId(variantId);
		final String productId = "112";
		variant.setProductId(productId);
		final BigDecimal price = BigDecimal.valueOf(22.56);
		variant.setPrice(price);
		variant.setInventoryLevel(3);
		variant.setSku("WOOP33");

		final VariantResponse variantResponse = new VariantResponse();
		final Variant expectedVariant = new Variant();
		expectedVariant.setId(variantId);
		expectedVariant.setProductId(productId);
		expectedVariant.setSku("WOOP34");
		variantResponse.setData(expectedVariant);

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION).append(FORWARD_SLASH)
				.append("catalog/products/").append(productId).append("/variants/").append(variantId).toString();

		final JAXBContext jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { VariantResponse.class }, null);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(variantResponse, stringWriter);
		final String expectedResponseBodyString = stringWriter.toString();

		final Status expectedStatus = Status.OK;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		final JsonBodyCapture actualRequestBody = new JsonBodyCapture();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.PUT)
						.capturingBodyIn(actualRequestBody),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		final Variant actualVariant = bigcommerceSdk.updateVariant(variant);

		assertEquals(price, BigDecimal.valueOf(actualRequestBody.getContent().get("price").asDouble()));
		assertEquals(variantId, actualVariant.getId());
		assertEquals(productId, actualVariant.getProductId());
		assertEquals("WOOP34", actualVariant.getSku());
	}

	@Test(expected = BigcommerceErrorResponseException.class)
	public void givenSomeVariantWithInvalidIdWhenUpdatingVariantThenThrowNewBigcommerceErrorResponseException() {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final Variant variant = new Variant();
		final String variantId = "78";
		variant.setId(variantId);
		final String productId = "112";
		variant.setProductId(productId);
		final BigDecimal price = BigDecimal.valueOf(22.56);
		variant.setPrice(price);
		variant.setInventoryLevel(3);
		final String sku = "WOOP33";
		variant.setSku(sku);

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION).append(FORWARD_SLASH)
				.append("catalog/products/").append(productId).append("/variants/").append(variantId).toString();

		final Status expectedStatus = Status.NOT_FOUND;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.PUT),
				giveEmptyResponse().withStatus(expectedStatusCode));

		bigcommerceSdk.updateVariant(variant);
	}

	@Test
	public void givenSomePageWhenRetrievingOrdersThenReturnOrders()
			throws JAXBException, JsonGenerationException, JsonMappingException, IOException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V2).append(FORWARD_SLASH).append("orders")
				.toString();

		final DateTimeFormatter formatter = DateTimeFormat.forPattern(DateTimeAdapter.RFC_822_DATE_FORMAT);
		final Order firstExpectedOrder = buildOrder(formatter);

		final Order secondExpectedOrder = buildOrder(formatter);

		final List<Order> expectedOrders = Arrays.asList(firstExpectedOrder, secondExpectedOrder);

		final JAXBContext jaxbContextOrder = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { Order[].class }, null);
		final Marshaller marshallerOrders = jaxbContextOrder.createMarshaller();
		marshallerOrders.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		marshallerOrders.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final StringWriter ordersStringWriter = new StringWriter();
		marshallerOrders.marshal(expectedOrders, ordersStringWriter);

		final String expectedOrdersResponseBodyString = ordersStringWriter.toString();

		final Status expectedStatus = Status.OK;
		final int expectedStatusCode = expectedStatus.getStatusCode();

		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withParam("page", 1)
						.withParam("limit", 250).withMethod(Method.GET),
				giveResponse(expectedOrdersResponseBodyString, MediaType.APPLICATION_JSON)
						.withStatus(expectedStatusCode));

		final List<Order> actualOrders = bigcommerceSdk.getOrders(1);

		assertNotNull(actualOrders);
		assertEquals(expectedOrders.size(), actualOrders.size());
		assertEquals(expectedOrders.get(0).getId(), actualOrders.get(0).getId());
		assertEqualDates(expectedOrders.get(0).getDateCreated(), actualOrders.get(0).getDateCreated());
		assertEquals(expectedOrders.get(0).getDateModified(), actualOrders.get(0).getDateModified());
		assertEquals(expectedOrders.get(0).getSubtotalExTax(), actualOrders.get(0).getSubtotalExTax());
		assertEquals(expectedOrders.get(0).getBaseHandlingCost(), actualOrders.get(0).getBaseHandlingCost());
		assertEquals(expectedOrders.get(0).getBaseShippingCost(), actualOrders.get(0).getBaseShippingCost());
		assertEquals(expectedOrders.get(0).getBaseWrappingCost(), actualOrders.get(0).getBaseWrappingCost());
		assertEquals(expectedOrders.get(0).getCouponDiscount(), actualOrders.get(0).getCouponDiscount());
		assertEquals(expectedOrders.get(0).getCreditCardType(), actualOrders.get(0).getCreditCardType());
		assertEquals(expectedOrders.get(0).getCurrencyCode(), actualOrders.get(0).getCurrencyCode());
		assertEquals(expectedOrders.get(0).getCurrencyExchangeRate(), actualOrders.get(0).getCurrencyExchangeRate());
		assertEquals(expectedOrders.get(0).getCurrencyId(), actualOrders.get(0).getCurrencyId());

		assertEquals(expectedOrders.get(1).getId(), actualOrders.get(1).getId());
		assertEqualDates(expectedOrders.get(1).getDateCreated(), actualOrders.get(1).getDateCreated());
		assertEquals(expectedOrders.get(1).getDateModified(), actualOrders.get(1).getDateModified());
		assertEquals(expectedOrders.get(1).getSubtotalExTax(), actualOrders.get(1).getSubtotalExTax());
		assertEquals(expectedOrders.get(1).getBaseHandlingCost(), actualOrders.get(1).getBaseHandlingCost());
		assertEquals(expectedOrders.get(1).getBaseShippingCost(), actualOrders.get(1).getBaseShippingCost());
		assertEquals(expectedOrders.get(1).getBaseWrappingCost(), actualOrders.get(1).getBaseWrappingCost());
		assertEquals(expectedOrders.get(1).getCouponDiscount(), actualOrders.get(1).getCouponDiscount());
		assertEquals(expectedOrders.get(1).getCreditCardType(), actualOrders.get(1).getCreditCardType());
		assertEquals(expectedOrders.get(1).getCurrencyCode(), actualOrders.get(1).getCurrencyCode());
		assertEquals(expectedOrders.get(1).getCurrencyExchangeRate(), actualOrders.get(1).getCurrencyExchangeRate());
		assertEquals(expectedOrders.get(1).getCurrencyId(), actualOrders.get(1).getCurrencyId());

	}

	@Test
	public void givenSomePageWhenRetrievingOrdersAndDateInTheFutureThenReturnNoContent() {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V2).append(FORWARD_SLASH).append("orders")
				.toString();

		final Status expectedStatus = Status.NO_CONTENT;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		final DateTime futureDate = DateTime.parse("2018-07-18T00:00:00.000Z").toDateTime(DateTimeZone.UTC);
		driver.addExpectation(onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
				.withHeader(BigcommerceSdk.ACESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withParam("page", 1)
				.withParam("limit", 250).withParam("min_date_created", futureDate.toString()).withMethod(Method.GET),
				giveEmptyResponse().withStatus(expectedStatusCode)).anyTimes();

		List<Order> actualOrders = bigcommerceSdk.getOrders(1, futureDate);
		assertEquals(actualOrders, Collections.emptyList());
	}

	@Test(expected = BigcommerceErrorResponseException.class)
	public void givenInvalidCredentialsWhenRetrievingOrdersThrowBigCommerceException() {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V2).append(FORWARD_SLASH).append("orders")
				.toString();

		final Status expectedStatus = Status.UNAUTHORIZED;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withParam("page", 1)
						.withParam("limit", 250).withMethod(Method.GET),
				giveEmptyResponse().withStatus(expectedStatusCode)).anyTimes();

		bigcommerceSdk.getOrders(1);
	}

	@Test
	public void givenValidCustomerIdThenReturnValidCustomer() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V2).append(FORWARD_SLASH).append("customers/1")
				.toString();

		final JAXBContext jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { Customer.class }, null);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final Customer expectedCustomer = new Customer();
		expectedCustomer.setEmail(SOME_EMAIL);
		expectedCustomer.setFirstName(SOME_FIRST_NAME);
		expectedCustomer.setLastName(SOME_LAST_NAME);

		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(expectedCustomer, stringWriter);

		final String expectedResponseBodyString = stringWriter.toString();

		final Status expectedStatus = Status.OK;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.GET),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		final Customer actualCustomer = bigcommerceSdk.getCustomer(1);
		assertEquals(actualCustomer.getFirstName(), expectedCustomer.getFirstName());
		assertEquals(actualCustomer.getLastName(), expectedCustomer.getLastName());
		assertEquals(actualCustomer.getEmail(), expectedCustomer.getEmail());
	}

	@Test
	public void givenValidOrderThenReturnFirstShippingAddress() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V2).append(FORWARD_SLASH)
				.append("orders/100/shipping_addresses").toString();

		final JAXBContext jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { Address[].class }, null);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final String street1 = "1 Main Street";
		final String street2 = "Apt 2";
		final String city = "Scranton";
		final String state = "PA";
		final String zip = "18503";
		final Address expectedShippingAddress = new Address();

		expectedShippingAddress.setStreet1(street1);
		expectedShippingAddress.setStreet2(street2);
		expectedShippingAddress.setCity(city);
		expectedShippingAddress.setState(state);
		expectedShippingAddress.setZip(zip);

		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(Arrays.asList(expectedShippingAddress), stringWriter);

		final String expectedResponseBodyString = stringWriter.toString();

		final Status expectedStatus = Status.OK;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.GET),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		Address actualShippingAddress = bigcommerceSdk.getShippingAddress(100);
		assertEquals(actualShippingAddress.getStreet1(), expectedShippingAddress.getStreet1());
		assertEquals(actualShippingAddress.getStreet2(), expectedShippingAddress.getStreet2());
		assertEquals(actualShippingAddress.getCity(), expectedShippingAddress.getCity());
		assertEquals(actualShippingAddress.getState(), expectedShippingAddress.getState());
		assertEquals(actualShippingAddress.getZip(), expectedShippingAddress.getZip());

	}

	@Test
	public void givenOrderWithShipmentsThenReturnShipments() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V2).append(FORWARD_SLASH)
				.append("orders/100/shipments").toString();

		final JAXBContext jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { Shipment[].class }, null);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final Shipment firstExpectedShipment = buildShipment();
		final Shipment secondExpectedShipment = buildShipment();
		final Shipment thirdExpectedShipment = buildShipment();

		List<Shipment> expectedShipments = Arrays.asList(firstExpectedShipment, secondExpectedShipment,
				thirdExpectedShipment);

		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(expectedShipments, stringWriter);

		final String expectedResponseBodyString = stringWriter.toString();

		final Status expectedStatus = Status.OK;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withParam("page", 1)
						.withParam("limit", 250).withMethod(Method.GET),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		List<Shipment> actualShipments = bigcommerceSdk.getShipments(100, 1);
		assertEquals(expectedShipments.get(0).getShippingMethod(), actualShipments.get(0).getShippingMethod());
		assertEquals(expectedShipments.get(0).getShippingProvider(), actualShipments.get(0).getShippingProvider());
		assertEquals(expectedShipments.get(0).getTrackingNumber(), actualShipments.get(0).getTrackingNumber());

		assertEquals(expectedShipments.get(1).getShippingMethod(), actualShipments.get(1).getShippingMethod());
		assertEquals(expectedShipments.get(1).getShippingProvider(), actualShipments.get(1).getShippingProvider());
		assertEquals(expectedShipments.get(1).getTrackingNumber(), actualShipments.get(1).getTrackingNumber());

		assertEquals(expectedShipments.get(0).getShippingMethod(), actualShipments.get(2).getShippingMethod());
		assertEquals(expectedShipments.get(0).getShippingProvider(), actualShipments.get(2).getShippingProvider());
		assertEquals(expectedShipments.get(0).getTrackingNumber(), actualShipments.get(2).getTrackingNumber());

		assertEquals(expectedShipments.get(0).getItems().get(0).getOrderProductId(),
				actualShipments.get(0).getItems().get(0).getOrderProductId());
		assertEquals(expectedShipments.get(0).getItems().get(0).getProductId(),
				actualShipments.get(0).getItems().get(0).getProductId());
		assertEquals(expectedShipments.get(0).getItems().get(0).getQuantity(),
				actualShipments.get(0).getItems().get(0).getQuantity());

	}

	@Test
	public void givenValidOrderIdThenReturnLineItemsForOrder() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V2).append(FORWARD_SLASH)
				.append("orders/100/products").toString();

		final JAXBContext jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { LineItem[].class }, null);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final LineItem lineItem1 = buildLineItem();
		final LineItem lineItem2 = buildLineItem();
		final LineItem lineItem3 = buildLineItem();

		LineItemsResponse expectedLineItems = new LineItemsResponse();
		expectedLineItems.add(lineItem1);
		expectedLineItems.add(lineItem2);
		expectedLineItems.add(lineItem3);
		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(expectedLineItems, stringWriter);

		final String expectedResponseBodyString = stringWriter.toString();

		final Status expectedStatus = Status.OK;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withParam("page", 1)
						.withParam("limit", 250).withMethod(Method.GET),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		List<LineItem> actualLineItems = bigcommerceSdk.getLineItems(100, 1);
		assertEquals(actualLineItems.size(), expectedLineItems.size());
		assertNotNull(actualLineItems);
		assertEquals(SOME_PRICE, actualLineItems.get(0).getBaseCostPrice());
		assertEquals(SOME_PRICE, actualLineItems.get(0).getBaseTotal());
		assertEquals(SOME_PRICE, actualLineItems.get(0).getCostPriceExTax());
		assertEquals(SOME_PRICE, actualLineItems.get(0).getTotalExTax());
		assertEquals(SOME_NAME, actualLineItems.get(0).getName());
		assertEquals(SOME_PRICE, actualLineItems.get(0).getBasePrice());
		assertEquals(SOME_PRICE, actualLineItems.get(0).getBaseWrappingCost());
		assertEquals(SOME_BIN_PICKING_NUMBER, actualLineItems.get(0).getBinPickingNumber());
		assertEquals(SOME_PRICE, actualLineItems.get(0).getCostPriceExTax());
		assertEquals(SOME_PRICE, actualLineItems.get(0).getCostPriceIncTax());
		assertEquals(SOME_PRICE, actualLineItems.get(0).getCostPriceTax());
		assertEquals(SOME_PRICE, actualLineItems.get(0).getCostPriceExTax());
		assertEquals(SOME_QUANTITY, actualLineItems.get(0).getQuantity());

	}

	@Test
	public void givenCorrectlyConfiguredBigcommerceSdkReturnStore() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V2).append(FORWARD_SLASH).append("store")
				.toString();

		final JAXBContext jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { Store.class }, null);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		Store expectedStore = new Store();
		expectedStore.setWeightUnits("Ounces");

		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(expectedStore, stringWriter);

		final String expectedResponseBodyString = stringWriter.toString();

		final Status expectedStatus = Status.OK;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.GET),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		Store actualStore = bigcommerceSdk.getStore();
		assertEquals(actualStore.getWeightUnits(), expectedStore.getWeightUnits());

	}

	private Shipment buildShipment() {
		Shipment firstExpectedShipment = new Shipment();
		ShipmentLineItem shipmentLineItem1 = new ShipmentLineItem();
		shipmentLineItem1.setProductId(SOME_ID);
		shipmentLineItem1.setQuantity(12);
		shipmentLineItem1.setOrderProductId(SOME_ID);

		firstExpectedShipment.setItems(Arrays.asList(shipmentLineItem1));
		return firstExpectedShipment;
	}

	private Order buildOrder(DateTimeFormatter formatter) {
		final Order order = new Order();
		order.setId(100);
		order.setDateCreated(formatter.parseDateTime(SOME_DATE_STRING));
		order.setSubtotalExTax(SOME_PRICE);
		order.setBaseHandlingCost(SOME_PRICE);
		order.setBaseShippingCost(SOME_PRICE);
		order.setBaseWrappingCost(SOME_PRICE);
		order.setCouponDiscount(SOME_COUPON_DISCOUNT);
		order.setCreditCardType(SOME_CREDIT_CARD_TYPE);
		order.setCurrencyCode(SOME_CURRENCY_CODE);
		order.setCurrencyExchangeRate(SOME_CURRENCY_EXCHANGE);
		order.setCurrencyId(SOME_ID);

		return order;
	}

	private LineItem buildLineItem() {
		LineItem lineItem = new LineItem();
		lineItem.setName(SOME_NAME);
		lineItem.setBaseCostPrice(SOME_PRICE);
		lineItem.setBaseTotal(SOME_PRICE);
		lineItem.setCostPriceExTax(SOME_PRICE);
		lineItem.setTotalExTax(SOME_PRICE);
		lineItem.setBasePrice(SOME_PRICE);
		lineItem.setBaseWrappingCost(SOME_PRICE);
		lineItem.setBinPickingNumber(SOME_BIN_PICKING_NUMBER);
		lineItem.setCostPriceExTax(SOME_PRICE);
		lineItem.setCostPriceIncTax(SOME_PRICE);
		lineItem.setCostPriceTax(SOME_PRICE);
		lineItem.setCostPriceExTax(SOME_PRICE);
		lineItem.setQuantity(SOME_QUANTITY);

		return lineItem;
	}

	private void assertEqualDates(final DateTime firstDate, final DateTime secondDate) {
		assertEquals(firstDate.compareTo(secondDate), 0);
	}

	private BigcommerceSdk buildBigcommerceSdk() {
		final String apiUrl = driver.getBaseUrl();
		return BigcommerceSdk.newSandboxBuilder().withApiUrl(apiUrl).withRequestRetryTimeout(20, TimeUnit.MILLISECONDS)
				.withStoreHash(SOME_STORE_HASH).withClientId(SOME_CLIENT_ID).withAccessToken(SOME_ACCESS_TOKEN).build();
	}
}
