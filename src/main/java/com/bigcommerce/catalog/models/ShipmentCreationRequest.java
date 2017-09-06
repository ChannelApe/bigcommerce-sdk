package com.bigcommerce.catalog.models;

import java.util.List;

public class ShipmentCreationRequest {

	private final Shipment request;

	public static TrackingNumberStep newBuilder() {
		return new Steps();
	}

	public static interface TrackingNumberStep {
		CommentsStep withTrackingNumber(final String trackingNumber);
	}

	public static interface CommentsStep {
		OrderAddressIdStep withComments(final String comments);
	}

	public static interface OrderAddressIdStep {
		ShippingProviderStep withOrderAddressId(final Integer orderAddressId);
	}

	public static interface ShippingProviderStep {
		TrackingCarrierStep withShippingProvider(final String shippingProvider);
	}

	public static interface TrackingCarrierStep {
		ShipmentLineItemsStep withTrackingCarrier(final String trackingCarrier);
	}

	public static interface ShipmentLineItemsStep {
		BuildStep withShipmentLineItems(final List<ShipmentLineItem> shipmentLineItems);
	}

	private ShipmentCreationRequest(final Shipment shipment) {
		this.request = shipment;
	}

	public static interface BuildStep {
		ShipmentCreationRequest build();
	}

	public Shipment getRequest() {
		return request;
	}

	private static class Steps implements TrackingNumberStep, CommentsStep, OrderAddressIdStep, ShippingProviderStep,
			ShipmentLineItemsStep, TrackingCarrierStep, BuildStep {

		private final Shipment request = new Shipment();

		@Override
		public ShipmentCreationRequest build() {
			return new ShipmentCreationRequest(request);
		}

		@Override
		public BuildStep withShipmentLineItems(List<ShipmentLineItem> shipmentLineItems) {
			request.setItems(shipmentLineItems);
			return this;
		}

		@Override
		public TrackingCarrierStep withShippingProvider(String shippingProvider) {
			request.setShippingProvider(shippingProvider);
			return this;
		}

		@Override
		public ShippingProviderStep withOrderAddressId(Integer orderAddressId) {
			request.setOrderAddressId(orderAddressId);
			return this;
		}

		@Override
		public OrderAddressIdStep withComments(String comments) {
			request.setComments(comments);
			return this;
		}

		@Override
		public CommentsStep withTrackingNumber(String trackingNumber) {
			request.setTrackingNumber(trackingNumber);
			return this;
		}

		@Override
		public ShipmentLineItemsStep withTrackingCarrier(String trackingCarrier) {
			request.setTrackingCarrier(trackingCarrier);
			return this;
		}

	}

}
