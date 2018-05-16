package com.bigcommerce;

import static com.github.restdriver.clientdriver.RestClientDriver.giveEmptyResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.giveResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.bigcommerce.catalog.models.*;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Rule;
import org.junit.Test;

import com.bigcommerce.exceptions.BigcommerceErrorResponseException;
import com.bigcommerce.exceptions.BigcommerceException;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.restdriver.clientdriver.ClientDriverRequest.Method;
import com.github.restdriver.clientdriver.ClientDriverRule;
import com.github.restdriver.clientdriver.capture.JsonBodyCapture;

public class BigcommerceSdkTest {

	private static final String SOME_ORDER_ID = "100";
	private static final String SOME_TRACKING_CARRIER = "123";
	private static final String SOME_COMMENTS = "This is a fulfillment from channel ape";
	private static final String SOME_TRACKING_NUMBER = "1Z0398842038";
	private static final String SOME_SHIPPING_PROVIDER = "UPS";
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
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V3).append(FORWARD_SLASH)
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
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.GET),
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
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V3).append(FORWARD_SLASH)
				.append("catalog/summary").toString();

		final Status expectedStatus = Status.UNAUTHORIZED;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.GET),
				giveEmptyResponse().withStatus(expectedStatusCode));

		bigcommerceSdk.getCatalogSummary();
	}

	@Test
	public void givenSomePageWhenRetrievingProductsThenReturnProducts() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V3).append(FORWARD_SLASH)
				.append("catalog/products").toString();

		final ProductsResponse productsResponse = new ProductsResponse();
		final Product firstExpectedProduct = new Product();
		firstExpectedProduct.setId(112);
		firstExpectedProduct.setName("Quest Nutrition Quest Natural Protein Bar");
		final Product secondExpectedProduct = new Product();
		secondExpectedProduct.setId(113);
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
				.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withParam("include", "variants")
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

	@Test
	public void givenSomeProductWhenCreatingProductsThenCreateProduct() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V3).append(FORWARD_SLASH)
				.append("catalog/products").toString();

		final ProductResponse productsResponse = new ProductResponse();
		final Product expectedProduct = new Product();
		expectedProduct.setId(112);
		expectedProduct.setName("Quest Nutrition Quest Natural Protein Bar");
		expectedProduct.setCondition("NEW");
		expectedProduct.setCustomFields(Collections.emptyList());
		expectedProduct.setDescription("Quest Nutrition Bar");
		expectedProduct.setInventoryTracking("variant");
		expectedProduct.setMetaKeywords(Collections.emptyList());
		expectedProduct.setBrandId(45);
		expectedProduct.setCategories(Arrays.asList(1, 3, 55));

		final Variant firstExpectedVariant = new Variant();
		firstExpectedVariant.setSku("SKU-1234");
		firstExpectedVariant.setUpc("UPC1");
		firstExpectedVariant.setPrice(new BigDecimal(99.99));
		firstExpectedVariant.setWeight(new BigDecimal(4.323));
		firstExpectedVariant.setImageUrl("https://s3.aws.com/someimage-1.png");
		firstExpectedVariant.setOptionValues(Collections.emptyList());

		final Variant secondExpectedVariant = new Variant();
		secondExpectedVariant.setSku("SKU-4568");
		secondExpectedVariant.setUpc("UPC2");
		secondExpectedVariant.setPrice(new BigDecimal(47.99));
		secondExpectedVariant.setWeight(new BigDecimal(2.511));
		secondExpectedVariant.setImageUrl("https://s3.aws.com/someimage-2.png");
		secondExpectedVariant.setOptionValues(Collections.emptyList());

		expectedProduct.setVariants(Arrays.asList(firstExpectedVariant, secondExpectedVariant));

		productsResponse.setData(expectedProduct);

		final JAXBContext jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { ProductResponse.class }, null);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(productsResponse, stringWriter);
		final String expectedResponseBodyString = stringWriter.toString();

		final Status expectedStatus = Status.OK;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.POST),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		final Product actualProduct = bigcommerceSdk.createProduct(expectedProduct);

		assertEquals(expectedProduct.getId(), actualProduct.getId());
		assertEquals(expectedProduct.getName(), actualProduct.getName());
		assertEquals(expectedProduct.getCondition(), actualProduct.getCondition());
		assertEquals(expectedProduct.getCustomFields(), actualProduct.getCustomFields());
		assertEquals(expectedProduct.getDescription(), actualProduct.getDescription());
		assertEquals(expectedProduct.getMetaKeywords(), actualProduct.getMetaKeywords());
		assertEquals(expectedProduct.getBrandId(), actualProduct.getBrandId());
		assertEquals(expectedProduct.getCategories(), actualProduct.getCategories());

		assertEquals(expectedProduct.getVariants().size(), actualProduct.getVariants().size());
		assertEquals(expectedProduct.getVariants().get(0).getImageUrl(),
				actualProduct.getVariants().get(0).getImageUrl());
		assertEquals(expectedProduct.getVariants().get(0).getInventoryLevel(),
				actualProduct.getVariants().get(0).getInventoryLevel());
		assertEquals(expectedProduct.getVariants().get(0).getPrice(), actualProduct.getVariants().get(0).getPrice());
		assertEquals(expectedProduct.getVariants().get(0).getWeight(), actualProduct.getVariants().get(0).getWeight());
		assertEquals(expectedProduct.getVariants().get(0).getSku(), actualProduct.getVariants().get(0).getSku());
		assertEquals(expectedProduct.getVariants().get(0).getUpc(), actualProduct.getVariants().get(0).getUpc());
		assertEquals(expectedProduct.getVariants().get(0).getOptionValues(),
				actualProduct.getVariants().get(0).getOptionValues());

		assertEquals(expectedProduct.getVariants().get(1).getImageUrl(),
				actualProduct.getVariants().get(1).getImageUrl());
		assertEquals(expectedProduct.getVariants().get(1).getInventoryLevel(),
				actualProduct.getVariants().get(1).getInventoryLevel());
		assertEquals(expectedProduct.getVariants().get(1).getPrice(), actualProduct.getVariants().get(1).getPrice());
		assertEquals(expectedProduct.getVariants().get(1).getWeight(), actualProduct.getVariants().get(1).getWeight());
		assertEquals(expectedProduct.getVariants().get(1).getSku(), actualProduct.getVariants().get(1).getSku());
		assertEquals(expectedProduct.getVariants().get(1).getUpc(), actualProduct.getVariants().get(1).getUpc());
		assertEquals(expectedProduct.getVariants().get(1).getOptionValues(),
				actualProduct.getVariants().get(1).getOptionValues());

	}

	@Test
	public void givenSomeProductWhenUpdatingProductsThenUpdateProduct() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final Integer someProductId = 112;
		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V3).append(FORWARD_SLASH)
				.append("catalog/products/").append(someProductId).toString();

		final ProductResponse productsResponse = new ProductResponse();
		final Product expectedProduct = new Product();
		expectedProduct.setId(someProductId);
		expectedProduct.setName("Quest Nutrition Quest Natural Protein Bar");
		expectedProduct.setCondition("NEW");
		expectedProduct.setCustomFields(Collections.emptyList());
		expectedProduct.setDescription("Quest Nutrition Bar");
		expectedProduct.setInventoryTracking("variant");
		expectedProduct.setMetaKeywords(Collections.emptyList());
		expectedProduct.setBrandId(45);
		expectedProduct.setCategories(Arrays.asList(1, 3, 55));

		final Variant firstExpectedVariant = new Variant();
		firstExpectedVariant.setSku("SKU-1234");
		firstExpectedVariant.setUpc("UPC1");
		firstExpectedVariant.setPrice(new BigDecimal(99.99));
		firstExpectedVariant.setWeight(new BigDecimal(4.323));
		firstExpectedVariant.setImageUrl("https://s3.aws.com/someimage-1.png");
		firstExpectedVariant.setOptionValues(Collections.emptyList());

		final Variant secondExpectedVariant = new Variant();
		secondExpectedVariant.setSku("SKU-4568");
		secondExpectedVariant.setUpc("UPC2");
		secondExpectedVariant.setPrice(new BigDecimal(47.99));
		secondExpectedVariant.setWeight(new BigDecimal(2.511));
		secondExpectedVariant.setImageUrl("https://s3.aws.com/someimage-2.png");
		secondExpectedVariant.setOptionValues(Collections.emptyList());

		expectedProduct.setVariants(Arrays.asList(firstExpectedVariant, secondExpectedVariant));

		productsResponse.setData(expectedProduct);

		final JAXBContext jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { ProductResponse.class }, null);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(productsResponse, stringWriter);
		final String expectedResponseBodyString = stringWriter.toString();

		final Status expectedStatus = Status.OK;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.PUT),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		final Product actualProduct = bigcommerceSdk.updateProduct(expectedProduct);

		assertEquals(expectedProduct.getId(), actualProduct.getId());
		assertEquals(expectedProduct.getName(), actualProduct.getName());
		assertEquals(expectedProduct.getCondition(), actualProduct.getCondition());
		assertEquals(expectedProduct.getCustomFields(), actualProduct.getCustomFields());
		assertEquals(expectedProduct.getDescription(), actualProduct.getDescription());
		assertEquals(expectedProduct.getMetaKeywords(), actualProduct.getMetaKeywords());
		assertEquals(expectedProduct.getBrandId(), actualProduct.getBrandId());
		assertEquals(expectedProduct.getCategories(), actualProduct.getCategories());

		assertEquals(2, expectedProduct.getVariants().size());
		assertEquals(expectedProduct.getVariants().get(0).getImageUrl(),
				actualProduct.getVariants().get(0).getImageUrl());
		assertEquals(expectedProduct.getVariants().get(0).getInventoryLevel(),
				actualProduct.getVariants().get(0).getInventoryLevel());
		assertEquals(expectedProduct.getVariants().get(0).getPrice(), actualProduct.getVariants().get(0).getPrice());
		assertEquals(expectedProduct.getVariants().get(0).getWeight(), actualProduct.getVariants().get(0).getWeight());
		assertEquals(expectedProduct.getVariants().get(0).getSku(), actualProduct.getVariants().get(0).getSku());
		assertEquals(expectedProduct.getVariants().get(0).getUpc(), actualProduct.getVariants().get(0).getUpc());
		assertEquals(expectedProduct.getVariants().get(0).getOptionValues(),
				actualProduct.getVariants().get(0).getOptionValues());

		assertEquals(expectedProduct.getVariants().get(1).getImageUrl(),
				actualProduct.getVariants().get(1).getImageUrl());
		assertEquals(expectedProduct.getVariants().get(1).getInventoryLevel(),
				actualProduct.getVariants().get(1).getInventoryLevel());
		assertEquals(expectedProduct.getVariants().get(1).getPrice(), actualProduct.getVariants().get(1).getPrice());
		assertEquals(expectedProduct.getVariants().get(1).getWeight(), actualProduct.getVariants().get(1).getWeight());
		assertEquals(expectedProduct.getVariants().get(1).getSku(), actualProduct.getVariants().get(1).getSku());
		assertEquals(expectedProduct.getVariants().get(1).getUpc(), actualProduct.getVariants().get(1).getUpc());
		assertEquals(expectedProduct.getVariants().get(1).getOptionValues(),
				actualProduct.getVariants().get(1).getOptionValues());

	}

	@Test
	public void givenSomeProductIdWhenDeletingProductThenDeleteProduct() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final Integer expectedProductId = 112;

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V3).append(FORWARD_SLASH)
				.append("catalog/products/").append(expectedProductId).toString();

		final Status expectedStatus = Status.NO_CONTENT;
		final int expectedStatusCode = expectedStatus.getStatusCode();

		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.DELETE),
				giveResponse(null, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		bigcommerceSdk.deleteProduct(expectedProductId);

	}

	@Test
	public void givenSomeProductImageWhenCreatingProductImageThenCreateProductImage() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V3).append(FORWARD_SLASH)
				.append("catalog/products/").append(1).append("/images").toString();

		final ProductImageResponse productImageResponse = new ProductImageResponse();
		final ProductImage expectedProductImage = new ProductImage();
		expectedProductImage.setImageUrl("https://aws.s3.com/testingimage-1.png");
		expectedProductImage.setIsThumbnail(true);
		expectedProductImage.setProductId(1);
		productImageResponse.setData(expectedProductImage);

		final JAXBContext jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { ProductImageResponse.class }, null);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(productImageResponse, stringWriter);
		final String expectedResponseBodyString = stringWriter.toString();

		final Status expectedStatus = Status.OK;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.POST),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		final ProductImage actualProductImage = bigcommerceSdk.createProductImage(expectedProductImage);
		assertEquals(expectedProductImage.getImageUrl(), actualProductImage.getImageUrl());
		assertEquals(expectedProductImage.getIsThumbnail(), actualProductImage.getIsThumbnail());
	}

	@Test
	public void givenSomeProductImageWhenUpdatingProductImageThenUpdateProductImage() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final Integer someProductId = 1;
		final Integer someImageId = 1;
		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V3).append(FORWARD_SLASH)
				.append("catalog/products/").append(someProductId).append("/images/").append(someImageId).toString();

		final ProductImageResponse productImageResponse = new ProductImageResponse();
		final ProductImage expectedProductImage = new ProductImage();
		expectedProductImage.setImageUrl("https://aws.s3.com/testingimage-1.png");
		expectedProductImage.setIsThumbnail(true);
		expectedProductImage.setProductId(someProductId);
		expectedProductImage.setId(someImageId);
		productImageResponse.setData(expectedProductImage);

		final JAXBContext jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { ProductImageResponse.class }, null);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(productImageResponse, stringWriter);
		final String expectedResponseBodyString = stringWriter.toString();

		final Status expectedStatus = Status.OK;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.PUT),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		final ProductImage actualProductImage = bigcommerceSdk.updateProductImage(expectedProductImage);
		assertEquals(expectedProductImage.getImageUrl(), actualProductImage.getImageUrl());
		assertEquals(expectedProductImage.getIsThumbnail(), actualProductImage.getIsThumbnail());
		assertEquals(expectedProductImage.getId(), actualProductImage.getId());
	}

	@Test(expected = BigcommerceException.class)
	public void givenSomePageAndBigCommerceReturnsInternalServerErrorWhenRetrievingProductsThenRetryAndEventuallyThrowNewBigcommerceException() {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V3).append(FORWARD_SLASH)
				.append("catalog/products").toString();

		final Status expectedStatus = Status.INTERNAL_SERVER_ERROR;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		driver.addExpectation(onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
				.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withParam("include", "variants")
				.withParam("page", 1).withParam("limit", 250).withMethod(Method.GET),
				giveEmptyResponse().withStatus(expectedStatusCode)).anyTimes();

		bigcommerceSdk.getProducts(1);
	}

	@Test(expected = BigcommerceException.class)
	public void givenSomePageAndBigCommerceReturnsTooManyRequestsWhenRetrievingProductsThenRetryAndEventuallyThrowNewBigcommerceException() {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V3).append(FORWARD_SLASH)
				.append("catalog/products").toString();

		final int expectedStatusCode = BigcommerceSdk.TOO_MANY_REQUESTS_STATUS_CODE;
		driver.addExpectation(onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
				.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withParam("include", "variants")
				.withParam("page", 1).withParam("limit", 250).withMethod(Method.GET),
				giveEmptyResponse().withStatus(expectedStatusCode)
						.withHeader(BigcommerceSdk.RATE_LIMIT_TIME_RESET_HEADER, "5"))
				.anyTimes();

		bigcommerceSdk.getProducts(1);
	}

	@Test
	public void givenSomeVariantWhenCreatingVariantThenCreateVariant() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final Variant variant = new Variant();
		variant.setProductId(112);
		final BigDecimal price = BigDecimal.valueOf(22.56);
		variant.setPrice(price);
		variant.setInventoryLevel(3);
		variant.setSku("WOOP33");

		final VariantResponse variantResponse = new VariantResponse();
		final Variant expectedVariant = new Variant();

		expectedVariant.setProductId(112);
		expectedVariant.setSku("WOOP34");
		expectedVariant.setImageUrl("https://google.com/images.png");
		expectedVariant.setMpn("test");
		expectedVariant.setUpc("SOME_UPC");
		expectedVariant.setWeight(new BigDecimal(32.99));
		expectedVariant.setPrice(new BigDecimal(99.00));
		expectedVariant.setOptionValues(Collections.emptyList());
		expectedVariant.setInventoryLevel(12);
		variantResponse.setData(expectedVariant);

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V3).append(FORWARD_SLASH)
				.append("catalog/products/").append(112).append("/variants").toString();

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
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.POST)
						.capturingBodyIn(actualRequestBody),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		final Variant actualVariant = bigcommerceSdk.createVariant(variant);

		assertEquals(price, BigDecimal.valueOf(actualRequestBody.getContent().get("price").asDouble()));

		assertEquals(new Integer(112), actualVariant.getProductId());
		assertEquals("WOOP34", actualVariant.getSku());
		assertEquals(expectedVariant.getImageUrl(), actualVariant.getImageUrl());
		assertEquals(expectedVariant.getInventoryLevel(), actualVariant.getInventoryLevel());
		assertEquals(expectedVariant.getUpc(), actualVariant.getUpc());
		assertEquals(expectedVariant.getWeight(), actualVariant.getWeight());
		assertEquals(expectedVariant.getPrice(), actualVariant.getPrice());
		assertEquals(expectedVariant.getMpn(), actualVariant.getMpn());
		assertEquals(expectedVariant.getOptionValues(), actualVariant.getOptionValues());
	}

	@Test
	public void givenSomeVariantWhenUpdatingVariantThenUpdateVariant() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final Variant variant = new Variant();
		variant.setId(78);
		variant.setProductId(112);
		final BigDecimal price = BigDecimal.valueOf(22.56);
		variant.setPrice(price);
		variant.setInventoryLevel(3);
		variant.setSku("WOOP33");

		final VariantResponse variantResponse = new VariantResponse();
		final Variant expectedVariant = new Variant();
		expectedVariant.setId(78);
		expectedVariant.setProductId(112);
		expectedVariant.setSku("WOOP34");
		variantResponse.setData(expectedVariant);

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V3).append(FORWARD_SLASH)
				.append("catalog/products/").append(112).append("/variants/").append(78).toString();

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
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.PUT)
						.capturingBodyIn(actualRequestBody),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		final Variant actualVariant = bigcommerceSdk.updateVariant(variant);

		assertEquals(price, BigDecimal.valueOf(actualRequestBody.getContent().get("price").asDouble()));
		assertEquals(new Integer(78), actualVariant.getId());
		assertEquals(new Integer(112), actualVariant.getProductId());
		assertEquals("WOOP34", actualVariant.getSku());
	}

	@Test
	public void givenSomeProductIdAndSomeVariantIdWhenDeletingVariantThenDeleteVariant() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final Integer expectedProductId = 112;
		final Integer expectedVariantId = 450;

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V3).append(FORWARD_SLASH)
				.append("catalog/products/").append(expectedProductId).append("/variants/").append(expectedVariantId)
				.toString();

		final Status expectedStatus = Status.NO_CONTENT;
		final int expectedStatusCode = expectedStatus.getStatusCode();

		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.DELETE),
				giveResponse(null, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		bigcommerceSdk.deleteVariant(expectedProductId, expectedVariantId);

	}

	@Test(expected = BigcommerceErrorResponseException.class)
	public void givenSomeVariantWithInvalidIdWhenUpdatingVariantThenThrowNewBigcommerceErrorResponseException() {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final Variant variant = new Variant();
		final Integer variantId = 78;
		variant.setId(variantId);
		final Integer productId = 112;
		variant.setProductId(productId);
		final BigDecimal price = BigDecimal.valueOf(22.56);
		variant.setPrice(price);
		variant.setInventoryLevel(3);
		final String sku = "WOOP33";
		variant.setSku(sku);

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V3).append(FORWARD_SLASH)
				.append("catalog/products/").append(productId).append("/variants/").append(variantId).toString();

		final Status expectedStatus = Status.NOT_FOUND;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.PUT),
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
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withParam("page", 1)
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
				.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withParam("page", 1)
				.withParam("limit", 250).withParam("min_date_created", futureDate.toString()).withMethod(Method.GET),
				giveEmptyResponse().withStatus(expectedStatusCode)).anyTimes();

		final List<Order> actualOrders = bigcommerceSdk.getOrders(1, futureDate);
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
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withParam("page", 1)
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
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.GET),
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
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.GET),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		final Address actualShippingAddress = bigcommerceSdk.getShippingAddress(100);
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

		final List<Shipment> expectedShipments = Arrays.asList(firstExpectedShipment, secondExpectedShipment,
				thirdExpectedShipment);

		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(expectedShipments, stringWriter);

		final String expectedResponseBodyString = stringWriter.toString();

		final Status expectedStatus = Status.OK;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withParam("page", 1)
						.withParam("limit", 250).withMethod(Method.GET),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		final List<Shipment> actualShipments = bigcommerceSdk.getShipments(100, 1);
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

		final LineItemsResponse expectedLineItems = new LineItemsResponse();
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
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withParam("page", 1)
						.withParam("limit", 250).withMethod(Method.GET),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		final List<LineItem> actualLineItems = bigcommerceSdk.getLineItems(100, 1);
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
	public void givenAShipmentThenCreateAShipment() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final ShipmentLineItem expectedShipmentLineItem1 = new ShipmentLineItem();
		expectedShipmentLineItem1.setOrderProductId(1);
		expectedShipmentLineItem1.setQuantity(2);
		final List<ShipmentLineItem> expectedShipmentLineItems = Arrays.asList(expectedShipmentLineItem1);

		final ShipmentCreationRequest shipmentCreationRequest = ShipmentCreationRequest.newBuilder()
				.withTrackingNumber(SOME_TRACKING_NUMBER).withComments(SOME_COMMENTS).withOrderAddressId(1)
				.withShippingProvider(SOME_SHIPPING_PROVIDER).withTrackingCarrier(SOME_TRACKING_CARRIER)
				.withShipmentLineItems(expectedShipmentLineItems).build();

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V2).append(FORWARD_SLASH)
				.append("orders/100/shipments").toString();

		final JAXBContext jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { Shipment.class }, null);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(shipmentCreationRequest.getRequest(), stringWriter);
		final String expectedResponseBodyString = stringWriter.toString();

		final Status expectedStatus = Status.OK;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		final JsonBodyCapture actualRequestBody = new JsonBodyCapture();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.POST)
						.capturingBodyIn(actualRequestBody),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		final Shipment actualShipment = bigcommerceSdk.createShipment(shipmentCreationRequest, 100);
		assertEquals(actualShipment.getComments(), shipmentCreationRequest.getRequest().getComments());
		assertEquals(actualShipment.getTrackingCarrier(), shipmentCreationRequest.getRequest().getTrackingCarrier());
		assertEquals(actualShipment.getShippingProvider(), shipmentCreationRequest.getRequest().getShippingProvider());
		assertEquals(actualShipment.getOrderAddressId(), shipmentCreationRequest.getRequest().getOrderAddressId());
		assertEquals(actualShipment.getItems().get(0).getOrderProductId(),
				shipmentCreationRequest.getRequest().getOrderAddressId());

		assertEquals(shipmentCreationRequest.getRequest().getComments(),
				actualRequestBody.getContent().get("comments").asText());
		assertEquals(shipmentCreationRequest.getRequest().getShippingProvider(),
				actualRequestBody.getContent().get("shipping_provider").asText());
		assertEquals(shipmentCreationRequest.getRequest().getTrackingCarrier(),
				actualRequestBody.getContent().get("tracking_carrier").asText());

	}

	@Test
	public void givenAShipmentThenUpdateAShipment() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final ShipmentUpdateRequest shipmentUpdateRequest = ShipmentUpdateRequest.newBuilder()
				.withTrackingNumber(SOME_TRACKING_NUMBER).withComments(SOME_COMMENTS).withOrderAddressId(1)
				.withShippingProvider(SOME_SHIPPING_PROVIDER).withTrackingCarrier(SOME_TRACKING_CARRIER).build();

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V2).append(FORWARD_SLASH)
				.append("orders/100/shipments/35").toString();

		final JAXBContext jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { Shipment.class }, null);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(shipmentUpdateRequest.getRequest(), stringWriter);
		final String expectedResponseBodyString = stringWriter.toString();

		final Status expectedStatus = Status.OK;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		final JsonBodyCapture actualRequestBody = new JsonBodyCapture();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.PUT)
						.capturingBodyIn(actualRequestBody),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		final Shipment actualShipment = bigcommerceSdk.updateShipment(shipmentUpdateRequest, 100, 35);
		assertEquals(actualShipment.getComments(), shipmentUpdateRequest.getRequest().getComments());
		assertEquals(actualShipment.getTrackingCarrier(), shipmentUpdateRequest.getRequest().getTrackingCarrier());
		assertEquals(actualShipment.getShippingProvider(), shipmentUpdateRequest.getRequest().getShippingProvider());
		assertEquals(actualShipment.getOrderAddressId(), shipmentUpdateRequest.getRequest().getOrderAddressId());

		assertEquals(shipmentUpdateRequest.getRequest().getComments(),
				actualRequestBody.getContent().get("comments").asText());
		assertEquals(shipmentUpdateRequest.getRequest().getShippingProvider(),
				actualRequestBody.getContent().get("shipping_provider").asText());
		assertEquals(shipmentUpdateRequest.getRequest().getTrackingCarrier(),
				actualRequestBody.getContent().get("tracking_carrier").asText());

	}

	@Test
	public void givenCorrectlyConfiguredBigcommerceSdkThenReturnStore() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V2).append(FORWARD_SLASH).append("store")
				.toString();

		final JAXBContext jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { Store.class }, null);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final Store expectedStore = new Store();
		expectedStore.setWeightUnits(WeightUnits.OUNCES);

		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(expectedStore, stringWriter);

		final String expectedResponseBodyString = stringWriter.toString();

		final Status expectedStatus = Status.OK;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.GET),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		final Store actualStore = bigcommerceSdk.getStore();
		assertEquals(actualStore.getWeightUnits(), expectedStore.getWeightUnits());

	}

	@Test
	public void givenBigcommerceSdkAndStatusNameThenReturnOrderStatus() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V2).append(FORWARD_SLASH)
				.append("order_statuses").toString();

		final JAXBContext jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { OrderStatus[].class }, null);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final OrderStatus expectedOrderStatus1 = new OrderStatus();
		expectedOrderStatus1.setName("Pending");
		expectedOrderStatus1.setId(1);
		expectedOrderStatus1.setOrder(1);

		final OrderStatus expectedOrderStatus2 = new OrderStatus();
		expectedOrderStatus2.setName("Completed");
		expectedOrderStatus2.setId(2);
		expectedOrderStatus2.setOrder(2);

		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(Arrays.asList(expectedOrderStatus1, expectedOrderStatus2), stringWriter);

		final String expectedResponseBodyString = stringWriter.toString();

		final Status expectedStatus = Status.OK;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.GET),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		final OrderStatus actualOrderStatus = bigcommerceSdk.getStatus(com.bigcommerce.catalog.models.Status.COMPLETED);
		assertEquals(expectedOrderStatus2.getName(), actualOrderStatus.getName());
		assertEquals(expectedOrderStatus2.getId(), actualOrderStatus.getId());

		assertFalse(expectedOrderStatus1.getName().equals(actualOrderStatus.getName()));
		assertFalse(expectedOrderStatus1.getId().equals(actualOrderStatus.getId()));
	}

	@Test
	public void givenAnOrderWhenCompletingOrderThenSetOrderStatusToComplete() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();
		final String expectedPathCompleteOrder = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V2).append(FORWARD_SLASH).append("orders")
				.append(FORWARD_SLASH).append(SOME_ORDER_ID).toString();

		final String expectedPathOrderStatuses = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V2).append(FORWARD_SLASH)
				.append("order_statuses").toString();

		final JAXBContext jaxbContextOrderStatus = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { OrderStatus[].class }, null);
		final Marshaller orderStatusMarshaller = jaxbContextOrderStatus.createMarshaller();
		orderStatusMarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		orderStatusMarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final OrderStatus completeOrderStatus = new OrderStatus();
		completeOrderStatus.setName(com.bigcommerce.catalog.models.Status.COMPLETED.toString());
		completeOrderStatus.setId(10);

		final List<OrderStatus> orderStatuses = Arrays.asList(completeOrderStatus);

		final StringWriter stringWriter = new StringWriter();
		orderStatusMarshaller.marshal(orderStatuses, stringWriter);

		final String expectedOrderStatusResponseBodyString = stringWriter.toString();

		final Status expectedOrderStatus = Status.OK;
		final int expectedCompleteOrderStatusCode = expectedOrderStatus.getStatusCode();

		driver.addExpectation(
				onRequestTo(expectedPathOrderStatuses).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.GET),
				giveResponse(expectedOrderStatusResponseBodyString, MediaType.APPLICATION_JSON)
						.withStatus(expectedCompleteOrderStatusCode));

		final JAXBContext jaxbContextCompleteOrder = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { Order.class }, null);
		final Marshaller completeOrderMarshaller = jaxbContextCompleteOrder.createMarshaller();
		completeOrderMarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		completeOrderMarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final Order expectedOrder = new Order();
		expectedOrder.setId(Integer.valueOf(SOME_ORDER_ID));
		expectedOrder.setStatus(com.bigcommerce.catalog.models.Status.COMPLETED);
		expectedOrder.setStatusId(10);

		final StringWriter completeOrderStringWriter = new StringWriter();
		completeOrderMarshaller.marshal(expectedOrder, completeOrderStringWriter);

		final String expectedCompleteOrderResponseBodyString = completeOrderStringWriter.toString();

		final int expectedCompleteStatusCode = Status.OK.getStatusCode();

		driver.addExpectation(
				onRequestTo(expectedPathCompleteOrder).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.PUT),
				giveResponse(expectedCompleteOrderResponseBodyString, MediaType.APPLICATION_JSON)
						.withStatus(expectedCompleteStatusCode));

		final Order actualOrder = bigcommerceSdk.completeOrder(Integer.valueOf(SOME_ORDER_ID));

		assertEquals(SOME_ORDER_ID, String.valueOf(actualOrder.getId()));
		assertEquals(com.bigcommerce.catalog.models.Status.COMPLETED, actualOrder.getStatus());
	}

	@Test
	public void givenAnOrderWhenCancellingAnOrderThenSetOrderStatusToCancel() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();
		final String expectedPathCancelOrder = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V2).append(FORWARD_SLASH).append("orders")
				.append(FORWARD_SLASH).append(SOME_ORDER_ID).toString();

		final String expectedPathOrderStatuses = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V2).append(FORWARD_SLASH)
				.append("order_statuses").toString();

		final JAXBContext jaxbContextOrderStatus = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { OrderStatus[].class }, null);
		final Marshaller orderStatusMarshaller = jaxbContextOrderStatus.createMarshaller();
		orderStatusMarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		orderStatusMarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final OrderStatus cancelledOrderStatus = new OrderStatus();
		cancelledOrderStatus.setName(com.bigcommerce.catalog.models.Status.CANCELLED.toString());
		cancelledOrderStatus.setId(10);

		final List<OrderStatus> orderStatuses = Arrays.asList(cancelledOrderStatus);

		final StringWriter stringWriter = new StringWriter();
		orderStatusMarshaller.marshal(orderStatuses, stringWriter);

		final String expectedOrderStatusResponseBodyString = stringWriter.toString();

		final Status expectedOrderStatus = Status.OK;
		final int expectedCancelledOrderStatusCode = expectedOrderStatus.getStatusCode();

		driver.addExpectation(
				onRequestTo(expectedPathOrderStatuses).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.GET),
				giveResponse(expectedOrderStatusResponseBodyString, MediaType.APPLICATION_JSON)
						.withStatus(expectedCancelledOrderStatusCode));

		final JAXBContext jaxbContextCompleteOrder = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { Order.class }, null);
		final Marshaller cancelOrderMarshaller = jaxbContextCompleteOrder.createMarshaller();
		cancelOrderMarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		cancelOrderMarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final Order expectedOrder = new Order();
		expectedOrder.setId(Integer.valueOf(SOME_ORDER_ID));
		expectedOrder.setStatus(com.bigcommerce.catalog.models.Status.CANCELLED);
		expectedOrder.setStatusId(10);

		final StringWriter cancelledOrderStringWriter = new StringWriter();
		cancelOrderMarshaller.marshal(expectedOrder, cancelledOrderStringWriter);

		final String expectedCompleteOrderResponseBodyString = cancelledOrderStringWriter.toString();

		final int expectedCancelledStatusCode = Status.OK.getStatusCode();

		driver.addExpectation(
				onRequestTo(expectedPathCancelOrder).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.PUT),
				giveResponse(expectedCompleteOrderResponseBodyString, MediaType.APPLICATION_JSON)
						.withStatus(expectedCancelledStatusCode));

		final Order actualOrder = bigcommerceSdk.cancelOrder(Integer.valueOf(SOME_ORDER_ID));

		assertEquals(SOME_ORDER_ID, String.valueOf(actualOrder.getId()));
		assertEquals(com.bigcommerce.catalog.models.Status.CANCELLED, actualOrder.getStatus());
	}

	@Test
	public void givenCatalogWithBrandsThenReturnBrands() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V3).append(FORWARD_SLASH)
				.append("catalog/brands").toString();

		final JAXBContext jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { BrandsResponse.class }, null);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final Brand firstExpectedBrand = buildBrand(1);
		final Brand secondExpectedBrand = buildBrand(2);
		final Brand thirdExpectedBrand = buildBrand(3);

		final List<Brand> expectedBrands = Arrays.asList(firstExpectedBrand, secondExpectedBrand, thirdExpectedBrand);
		final BrandsResponse response = new BrandsResponse();
		response.setData(expectedBrands);
		final Meta meta = new Meta();
		final Pagination expectedPagination = new Pagination();
		expectedPagination.setCurrentPage(1);
		expectedPagination.setPerPage(250);
		expectedPagination.setTotal(10);
		expectedPagination.setTotalPages(5);
		meta.setPagination(expectedPagination);
		response.setMeta(meta);

		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(response, stringWriter);

		final String expectedResponseBodyString = stringWriter.toString();

		final Status expectedStatus = Status.OK;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withParam("page", 1)
						.withParam("limit", 250).withMethod(Method.GET),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		final Brands actualBrands = bigcommerceSdk.getBrands(1, 250);
		assertEquals(expectedBrands.get(0).getName(), actualBrands.getBrands().get(0).getName());
		assertEquals(expectedBrands.get(0).getId(), actualBrands.getBrands().get(0).getId());
		assertEquals(expectedBrands.get(0).getImageUrl(), actualBrands.getBrands().get(0).getImageUrl());
		assertEquals(expectedBrands.get(0).getPageTitle(), actualBrands.getBrands().get(0).getPageTitle());
		assertEquals(expectedBrands.get(0).getSearchKeywords(), actualBrands.getBrands().get(0).getSearchKeywords());
		assertEquals(expectedBrands.get(0).getMetaDescription(), actualBrands.getBrands().get(0).getMetaDescription());
		assertEquals(expectedBrands.get(0).getMetaKeywords(), actualBrands.getBrands().get(0).getMetaKeywords());
		assertEquals(expectedBrands.get(0).getCustomUrl().getUrl(), actualBrands.getBrands().get(0).getCustomUrl().getUrl());
		assertEquals(expectedBrands.get(0).getCustomUrl().isCustomized(), actualBrands.getBrands().get(0).getCustomUrl().isCustomized());
		assertEquals(expectedBrands.get(0).hashCode(), actualBrands.getBrands().get(0).hashCode());
		assertEquals(expectedBrands.get(0), actualBrands.getBrands().get(0));
		assertTrue(expectedBrands.get(0).equals(actualBrands.getBrands().get(0)));

		assertEquals(meta.getPagination().getTotal(), actualBrands.getPagination().getTotal());
		assertEquals(meta.getPagination().getTotalPages(), actualBrands.getPagination().getTotalPages());
		assertEquals(meta.getPagination().getCurrentPage(), actualBrands.getPagination().getCurrentPage());
		assertEquals(meta.getPagination().getPerPage(), actualBrands.getPagination().getPerPage());

	}

	@Test
	public void givenSomeBrandWhenCreatingBrandThenCreateBrand() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final Brand expectedBrand = new Brand();

		expectedBrand.setId(3);
		expectedBrand.setImageUrl("https://aws.s3.com/someimage-url.png");
		expectedBrand.setMetaDescription("Some Brand Description");
		expectedBrand.setMetaKeywords(Arrays.asList("Shoes", "Apparel"));
		expectedBrand.setName("Nike");
		expectedBrand.setPageTitle("Nike Shoes");
		expectedBrand.setSearchKeywords("Shoes");

		final BrandResponse brandResponse = new BrandResponse();

		brandResponse.setData(expectedBrand);

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V3).append(FORWARD_SLASH)
				.append("catalog/brands").toString();

		final JAXBContext jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { BrandResponse.class }, null);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(brandResponse, stringWriter);
		final String expectedResponseBodyString = stringWriter.toString();

		final Status expectedStatus = Status.OK;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		final JsonBodyCapture actualRequestBody = new JsonBodyCapture();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.POST)
						.capturingBodyIn(actualRequestBody),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		final Brand actualBrand = bigcommerceSdk.createBrand(expectedBrand);

		assertEquals(expectedBrand.getId(), actualBrand.getId());
		assertEquals(expectedBrand.getImageUrl(), actualBrand.getImageUrl());
		assertEquals(expectedBrand.getMetaDescription(), actualBrand.getMetaDescription());
		assertEquals(expectedBrand.getMetaKeywords(), actualBrand.getMetaKeywords());
		assertEquals(expectedBrand.getName(), actualBrand.getName());
		assertEquals(expectedBrand.getPageTitle(), actualBrand.getPageTitle());
	}

	@Test
	public void givenSomeProductMetafiedAndProductIdWhenCreatingProductMetafieldThenCreateProductMetafield()
			throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final String expectedKey = UUID.randomUUID().toString();
		final String expectedValue = UUID.randomUUID().toString();
		final Metafield expectedProductMetafield = new Metafield();
		final Integer expectedProductId = 112;
		final String expectedNamespace = "ChannelApe";
		final String expectedPermissionSet = "write";
		final String expectedDescription = "Testing Description";
		final Integer expectedId = 1;

		expectedProductMetafield.setResourceId(expectedProductId);
		expectedProductMetafield.setKey(expectedKey);
		expectedProductMetafield.setValue(expectedValue);
		expectedProductMetafield.setNamespace(expectedNamespace);
		expectedProductMetafield.setPermissionSet(expectedPermissionSet);
		expectedProductMetafield.setDescription(expectedDescription);
		expectedProductMetafield.setId(expectedId);

		final MetafieldResponse metafieldResponse = new MetafieldResponse();

		metafieldResponse.setData(expectedProductMetafield);

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V3).append(FORWARD_SLASH)
				.append("catalog/products/").append(expectedProductId).append("/metafields").toString();

		final JAXBContext jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { MetafieldResponse.class }, null);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(metafieldResponse, stringWriter);
		final String expectedResponseBodyString = stringWriter.toString();

		final Status expectedStatus = Status.OK;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		final JsonBodyCapture actualRequestBody = new JsonBodyCapture();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.POST)
						.capturingBodyIn(actualRequestBody),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		final Metafield actualMetafield = bigcommerceSdk.createProductMetafield(expectedProductId,
				expectedProductMetafield);

		assertEquals(expectedProductMetafield.getId(), actualMetafield.getId());
		assertEquals(expectedProductMetafield.getDescription(), actualMetafield.getDescription());
		assertEquals(expectedProductMetafield.getResourceId(), actualMetafield.getResourceId());
		assertEquals(expectedProductMetafield.getKey(), actualMetafield.getKey());
		assertEquals(expectedProductMetafield.getValue(), actualMetafield.getValue());
		assertEquals(expectedProductMetafield.getPermissionSet(), actualMetafield.getPermissionSet());
	}

	@Test
	public void givenSomeProductMetafiedAndProductIdAndMetafieldIdWhenUpdatingProductMetafieldsThenUpdateAndReturnProductMetafield()
			throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final String expectedKey = UUID.randomUUID().toString();
		final String expectedValue = UUID.randomUUID().toString();
		final Metafield expectedProductMetafield = new Metafield();
		final Integer expectedProductId = 112;
		final String expectedNamespace = "ChannelApe";
		final String expectedPermissionSet = "write";
		final String expectedDescription = "Testing Description";
		final Integer expectedId = 1;

		expectedProductMetafield.setResourceId(expectedProductId);
		expectedProductMetafield.setKey(expectedKey);
		expectedProductMetafield.setValue(expectedValue);
		expectedProductMetafield.setNamespace(expectedNamespace);
		expectedProductMetafield.setPermissionSet(expectedPermissionSet);
		expectedProductMetafield.setDescription(expectedDescription);
		expectedProductMetafield.setId(expectedId);

		final MetafieldResponse metafieldResponse = new MetafieldResponse();

		metafieldResponse.setData(expectedProductMetafield);

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V3).append(FORWARD_SLASH)
				.append("catalog/products/").append(expectedProductId).append("/metafields/").append(expectedId)
				.toString();

		final JAXBContext jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { MetafieldResponse.class }, null);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(metafieldResponse, stringWriter);
		final String expectedResponseBodyString = stringWriter.toString();

		final Status expectedStatus = Status.OK;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		final JsonBodyCapture actualRequestBody = new JsonBodyCapture();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.PUT)
						.capturingBodyIn(actualRequestBody),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		final Metafield actualMetafield = bigcommerceSdk.updateProductMetafield(expectedProductId, expectedId,
				expectedProductMetafield);

		assertEquals(expectedProductMetafield.getId(), actualMetafield.getId());
		assertEquals(expectedProductMetafield.getDescription(), actualMetafield.getDescription());
		assertEquals(expectedProductMetafield.getResourceId(), actualMetafield.getResourceId());
		assertEquals(expectedProductMetafield.getKey(), actualMetafield.getKey());
		assertEquals(expectedProductMetafield.getValue(), actualMetafield.getValue());
		assertEquals(expectedProductMetafield.getPermissionSet(), actualMetafield.getPermissionSet());
	}

	@Test
	public void givenSomeProductIdWhenRetrievingMetafieldsThenReturnMetafields() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final Integer expectedProductId = 112;

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V3).append(FORWARD_SLASH)
				.append("catalog/products/").append(expectedProductId).append("/metafields").toString();

		final JAXBContext jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { MetafieldsResponse.class }, null);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final Metafield firstExpectedMetafield = buildMetafield(1, "productId", UUID.randomUUID().toString(),
				"channelape", "some description1", expectedProductId);
		final Metafield secondExpectedMetafield = buildMetafield(2, "imageUrl", UUID.randomUUID().toString(),
				"channelape", "some description2", expectedProductId);
		final Metafield thirdExpectedMetafield = buildMetafield(3, "testField", UUID.randomUUID().toString(),
				"channelape", "some description3", expectedProductId);

		final List<Metafield> expectedMetafields = Arrays.asList(firstExpectedMetafield, secondExpectedMetafield,
				thirdExpectedMetafield);
		final MetafieldsResponse response = new MetafieldsResponse();
		response.setData(expectedMetafields);
		final Meta meta = new Meta();
		final Pagination expectedPagination = new Pagination();
		expectedPagination.setCurrentPage(1);
		expectedPagination.setPerPage(250);
		expectedPagination.setTotal(10);
		expectedPagination.setTotalPages(5);
		meta.setPagination(expectedPagination);
		response.setMeta(meta);

		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(response, stringWriter);

		final String expectedResponseBodyString = stringWriter.toString();

		final Status expectedStatus = Status.OK;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withParam("page", 1)
						.withParam("limit", 250).withMethod(Method.GET),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		final Metafields actualMetafields = bigcommerceSdk.getProductMetafields(expectedProductId, 1, 250);
		assertEquals(expectedMetafields.get(0).getResourceId(),
				actualMetafields.getMetafields().get(0).getResourceId());
		assertEquals(expectedMetafields.get(0).getId(), actualMetafields.getMetafields().get(0).getId());
		assertEquals(expectedMetafields.get(0).getKey(), actualMetafields.getMetafields().get(0).getKey());
		assertEquals(expectedMetafields.get(0).getValue(), actualMetafields.getMetafields().get(0).getValue());
		assertEquals(expectedMetafields.get(0).getDescription(),
				actualMetafields.getMetafields().get(0).getDescription());
		assertEquals(expectedMetafields.get(0).getNamespace(), actualMetafields.getMetafields().get(0).getNamespace());

		assertEquals(meta.getPagination().getTotal(), actualMetafields.getPagination().getTotal());
		assertEquals(meta.getPagination().getTotalPages(), actualMetafields.getPagination().getTotalPages());
		assertEquals(meta.getPagination().getCurrentPage(), actualMetafields.getPagination().getCurrentPage());
		assertEquals(meta.getPagination().getPerPage(), actualMetafields.getPagination().getPerPage());

	}

	@Test
	public void givenSomeProductIdAndSomePageWhenRetrievingProductImagesThenReturnProductImages() throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final Integer expectedProductId = 112;
		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V3).append(FORWARD_SLASH)
				.append("catalog/products/").append(expectedProductId).append("/images").toString();

		final JAXBContext jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
				.createContext(new Class[] { ProductImagesResponse.class }, null);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

		final ProductImage firstExpectedProductImage = new ProductImage();
		firstExpectedProductImage.setId(1);
		firstExpectedProductImage.setImageUrl("https://aws.s3.com/someimage/somewhere-on-s3.png");
		firstExpectedProductImage.setProductId(expectedProductId);
		firstExpectedProductImage.setIsThumbnail(true);

		final ProductImage secondExpectedProductImage = new ProductImage();
		secondExpectedProductImage.setId(2);
		secondExpectedProductImage.setImageUrl("https://aws.s3.com/someimage/somewhere-on-s3.jpg");
		secondExpectedProductImage.setProductId(expectedProductId);
		secondExpectedProductImage.setIsThumbnail(false);

		final ProductImage thirdExpectedProductImage = new ProductImage();
		thirdExpectedProductImage.setId(3);
		thirdExpectedProductImage.setImageUrl("https://aws.s3.com/someimage/somewhere-on-s3.GIF");
		thirdExpectedProductImage.setProductId(expectedProductId);
		thirdExpectedProductImage.setIsThumbnail(false);

		final List<ProductImage> expectedProductImages = Arrays.asList(firstExpectedProductImage,
				secondExpectedProductImage, thirdExpectedProductImage);
		final ProductImagesResponse response = new ProductImagesResponse();
		response.setData(expectedProductImages);
		final Meta meta = new Meta();
		final Pagination expectedPagination = new Pagination();
		expectedPagination.setCurrentPage(1);
		expectedPagination.setPerPage(250);
		expectedPagination.setTotal(10);
		expectedPagination.setTotalPages(5);
		meta.setPagination(expectedPagination);
		response.setMeta(meta);

		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(response, stringWriter);

		final String expectedResponseBodyString = stringWriter.toString();

		final Status expectedStatus = Status.OK;
		final int expectedStatusCode = expectedStatus.getStatusCode();
		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withParam("page", 1)
						.withParam("limit", 250).withMethod(Method.GET),
				giveResponse(expectedResponseBodyString, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		final ProductImages actualProductImages = bigcommerceSdk.getProductImages(expectedProductId, 1);
		assertEquals(expectedProductImages.get(0).getId(), actualProductImages.getProductImages().get(0).getId());
		assertEquals(expectedProductImages.get(0).getIsThumbnail(),
				actualProductImages.getProductImages().get(0).getIsThumbnail());
		assertEquals(expectedProductImages.get(0).getImageUrl(),
				actualProductImages.getProductImages().get(0).getImageUrl());
		assertEquals(expectedProductImages.get(0).getProductId(),
				actualProductImages.getProductImages().get(0).getProductId());

	}

	@Test
	public void givenSomeProductIdAndSomeProductImageIdWhenDeletingProductImagesThenDeleteProductImage()
			throws JAXBException {
		final BigcommerceSdk bigcommerceSdk = buildBigcommerceSdk();

		final Integer expectedProductId = 112;

		final Integer expectedId = 1;

		final String expectedPath = new StringBuilder().append(FORWARD_SLASH).append(SOME_STORE_HASH)
				.append(FORWARD_SLASH).append(BigcommerceSdk.API_VERSION_V3).append(FORWARD_SLASH)
				.append("catalog/products/").append(expectedProductId).append("/images/").append(expectedId).toString();

		final Status expectedStatus = Status.NO_CONTENT;
		final int expectedStatusCode = expectedStatus.getStatusCode();

		driver.addExpectation(
				onRequestTo(expectedPath).withHeader(BigcommerceSdk.CLIENT_ID_HEADER, SOME_CLIENT_ID)
						.withHeader(BigcommerceSdk.ACCESS_TOKEN_HEADER, SOME_ACCESS_TOKEN).withMethod(Method.DELETE),
				giveResponse(null, MediaType.APPLICATION_JSON).withStatus(expectedStatusCode));

		bigcommerceSdk.deleteProductImage(expectedProductId, expectedId);

	}

	private Metafield buildMetafield(final Integer id, final String key, final String value, final String namespace,
			final String description, final Integer resourceId) {
		final Metafield metafield = new Metafield();
		metafield.setId(id);
		metafield.setKey(key);
		metafield.setValue(value);
		metafield.setNamespace(namespace);
		metafield.setDescription(description);
		metafield.setResourceId(resourceId);
		return metafield;

	}

	private Brand buildBrand(final Integer id) {
		final Brand brand = new Brand();
		brand.setId(id);
		brand.setImageUrl("https://aws.s3/image1.png");
		brand.setMetaKeywords(Collections.emptyList());
		brand.setMetaDescription("NIKE BRAND DESCRIPTION");
		brand.setName("NIKE");
		brand.setPageTitle("Nike Brands");
		brand.setSearchKeywords("SHOES");
		CustomUrl customUrl = new CustomUrl();
		customUrl.setUrl("/url_" + id);
		customUrl.setCustomized(true);
		brand.setCustomUrl(customUrl);
		return brand;
	}

	private Shipment buildShipment() {
		final Shipment shipment = new Shipment();
		final ShipmentLineItem shipmentLineItem1 = new ShipmentLineItem();
		shipmentLineItem1.setProductId(SOME_ID);
		shipmentLineItem1.setQuantity(12);
		shipmentLineItem1.setOrderProductId(SOME_ID);

		shipment.setItems(Arrays.asList(shipmentLineItem1));
		return shipment;
	}

	private Order buildOrder(final DateTimeFormatter formatter) {
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
		final LineItem lineItem = new LineItem();
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
		assertEquals(0, firstDate.compareTo(secondDate));
	}

	private BigcommerceSdk buildBigcommerceSdk() {
		final String apiUrl = driver.getBaseUrl();
		return BigcommerceSdk.newSandboxBuilder().withApiUrl(apiUrl).withRequestRetryTimeout(20, TimeUnit.MILLISECONDS)
				.withStoreHash(SOME_STORE_HASH).withClientId(SOME_CLIENT_ID).withAccessToken(SOME_ACCESS_TOKEN).build();
	}
}
