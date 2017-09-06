package com.bigcommerce;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;

import com.bigcommerce.catalog.models.Address;
import com.bigcommerce.catalog.models.CatalogSummary;
import com.bigcommerce.catalog.models.Customer;
import com.bigcommerce.catalog.models.Order;
import com.bigcommerce.catalog.models.OrderStatus;
import com.bigcommerce.catalog.models.Products;
import com.bigcommerce.catalog.models.Shipment;
import com.bigcommerce.catalog.models.ShipmentCreationRequest;
import com.bigcommerce.catalog.models.ShipmentLineItem;
import com.bigcommerce.catalog.models.ShipmentUpdateRequest;
import com.bigcommerce.catalog.models.Status;
import com.bigcommerce.catalog.models.Store;
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

	@Test
	public void givenNonZeroIdWhenRetrievingCustomerThenReturnCustomer() {
		final BigcommerceSdk bigcommerceSdk = BigcommerceSdk.newBuilder().withStoreHash(STORE_HASH)
				.withClientId(CLIENT_ID).withAccessToken(ACCESS_TOKEN).build();

		final Customer actualCustomer = bigcommerceSdk.getCustomer(1);

		assertNotNull(actualCustomer);

	}

	@Test
	public void givenValidOrderIdWhenRetrievingShippingAddressThenReturnAtLeastOneShippingAddress() {
		final BigcommerceSdk bigcommerceSdk = BigcommerceSdk.newBuilder().withStoreHash(STORE_HASH)
				.withClientId(CLIENT_ID).withAccessToken(ACCESS_TOKEN).build();

		final Address address = bigcommerceSdk.getShippingAddress(114);

		assertNotNull(address);

	}

	@Test
	public void givenValidOrderIdWhenRetrievingShipmentsThenReturnShipmentInformation() {
		final BigcommerceSdk bigcommerceSdk = BigcommerceSdk.newBuilder().withStoreHash(STORE_HASH)
				.withClientId(CLIENT_ID).withAccessToken(ACCESS_TOKEN).build();

		final List<Shipment> shipments = bigcommerceSdk.getShipments(122, 1);

		assertNotNull(shipments);
		assertTrue(shipments.size() > 0);

	}

	@Test
	public void givenValidStoreIdThenReturnStore() {
		final BigcommerceSdk bigcommerceSdk = BigcommerceSdk.newBuilder().withStoreHash(STORE_HASH)
				.withClientId(CLIENT_ID).withAccessToken(ACCESS_TOKEN).build();

		final Store store = bigcommerceSdk.getStore();

		assertNotNull(store);
		assertNotNull(store.getWeightUnits());

	}

	@Test
	public void givenShipmentRequestThenCreateShipment() {
		final BigcommerceSdk bigcommerceSdk = BigcommerceSdk.newBuilder().withStoreHash(STORE_HASH)
				.withClientId(CLIENT_ID).withAccessToken(ACCESS_TOKEN).build();

		ShipmentLineItem lineItem = new ShipmentLineItem();
		lineItem.setOrderProductId(1);
		lineItem.setQuantity(1);

		ShipmentCreationRequest shipmentCreationRequest = ShipmentCreationRequest.newBuilder()
				.withTrackingNumber("1Z0398842038").withComments("This is a fulfillment from channel ape")
				.withOrderAddressId(1).withShippingProvider("UPS").withTrackingCarrier("")
				.withShipmentLineItems(Arrays.asList(lineItem)).build();

		final Shipment shipment = bigcommerceSdk.createShipment(shipmentCreationRequest, 100);

		assertNotNull(shipment);
		assertEquals(shipment.getTrackingNumber(), shipmentCreationRequest.getRequest().getTrackingNumber());
		assertEquals(shipment.getComments(), shipmentCreationRequest.getRequest().getComments());

	}

	@Test
	public void givenShipmentRequestThenUpdateShipment() {
		final BigcommerceSdk bigcommerceSdk = BigcommerceSdk.newBuilder().withStoreHash(STORE_HASH)
				.withClientId(CLIENT_ID).withAccessToken(ACCESS_TOKEN).build();

		ShipmentUpdateRequest shipmentUpdateRequest = ShipmentUpdateRequest.newBuilder()
				.withTrackingNumber("1Z0398842038").withComments("This is a fulfillment from channel ape")
				.withOrderAddressId(1).withShippingProvider("upsonline").withTrackingCarrier("").build();

		final Shipment shipment = bigcommerceSdk.updateShipment(shipmentUpdateRequest, 100, 36);

		assertNotNull(shipment);
		assertEquals(shipment.getTrackingNumber(), shipmentUpdateRequest.getRequest().getTrackingNumber());
		assertEquals(shipment.getComments(), shipmentUpdateRequest.getRequest().getComments());
	}

	@Test
	public void givenAnOrderIdThenCloseOrder() {
		final BigcommerceSdk bigcommerceSdk = BigcommerceSdk.newBuilder().withStoreHash(STORE_HASH)
				.withClientId(CLIENT_ID).withAccessToken(ACCESS_TOKEN).build();

		Order order = bigcommerceSdk.completeOrder(100);
		assertNotNull(order);
		assertEquals(order.getStatus(), Status.COMPLETED);
	}

	@Test
	public void givenStatusNameThenGetOrderStatus() {
		final BigcommerceSdk bigcommerceSdk = BigcommerceSdk.newBuilder().withStoreHash(STORE_HASH)
				.withClientId(CLIENT_ID).withAccessToken(ACCESS_TOKEN).build();

		OrderStatus orderStatus = bigcommerceSdk.getStatus(Status.COMPLETED);
		assertNotNull(orderStatus);
		assertEquals(Status.COMPLETED.toString(), orderStatus.getName());

	}

}
