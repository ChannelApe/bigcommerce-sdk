package com.bigcommerce;

import static com.github.restdriver.clientdriver.RestClientDriver.giveEmptyResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.giveResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;
import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.junit.Rule;
import org.junit.Test;

import com.bigcommerce.catalog.models.CatalogSummary;
import com.bigcommerce.catalog.models.CatalogSummaryResponse;
import com.bigcommerce.catalog.models.Meta;
import com.bigcommerce.catalog.models.Pagination;
import com.bigcommerce.catalog.models.Product;
import com.bigcommerce.catalog.models.Products;
import com.bigcommerce.catalog.models.ProductsResponse;
import com.bigcommerce.catalog.models.Variant;
import com.bigcommerce.catalog.models.VariantResponse;
import com.bigcommerce.exceptions.BigcommerceErrorResponseException;
import com.bigcommerce.exceptions.BigcommerceException;
import com.github.restdriver.clientdriver.ClientDriverRequest.Method;
import com.github.restdriver.clientdriver.ClientDriverRule;
import com.github.restdriver.clientdriver.capture.JsonBodyCapture;

public class BigcommerceSdkTest {

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

	private BigcommerceSdk buildBigcommerceSdk() {
		final String apiUrl = driver.getBaseUrl();
		return BigcommerceSdk.newSandboxBuilder().withApiUrl(apiUrl).withRequestRetryTimeout(20, TimeUnit.MILLISECONDS)
				.withStoreHash(SOME_STORE_HASH).withClientId(SOME_CLIENT_ID).withAccessToken(SOME_ACCESS_TOKEN).build();
	}
}
