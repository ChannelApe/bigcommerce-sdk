package com.bigcommerce.catalog.models;

public class ShipmentUpdateRequest {

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
		ShippingProviderStep withOrderAddressId(final int orderAddressId);
	}

	public static interface ShippingProviderStep {
		TrackingCarrierStep withShippingProvider(final String shippingProvider);
	}

	public static interface TrackingCarrierStep {
		BuildStep withTrackingCarrier(final String trackingCarrier);
	}

	private ShipmentUpdateRequest(final Shipment shipment) {
		this.request = shipment;
	}

	public static interface BuildStep {
		ShipmentUpdateRequest build();
	}

	public Shipment getRequest() {
		return request;
	}

	private static class Steps implements TrackingNumberStep, CommentsStep, OrderAddressIdStep, ShippingProviderStep,
			TrackingCarrierStep, BuildStep {

		private final Shipment request = new Shipment();

		@Override
		public ShipmentUpdateRequest build() {
			return new ShipmentUpdateRequest(request);
		}

		@Override
		public TrackingCarrierStep withShippingProvider(String shippingProvider) {
			request.setShippingProvider(shippingProvider);
			return this;
		}

		@Override
		public ShippingProviderStep withOrderAddressId(int orderAddressId) {
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
		public BuildStep withTrackingCarrier(String trackingCarrier) {
			request.setTrackingCarrier(trackingCarrier);
			return this;
		}

	}

}
