package com.bigcommerce.catalog.models;

public enum Status {
	PENDING("Pending"), AWAITING_PAYMENT("Awaiting Payment"), DISPUTED("Disputed"), REFUNDED(
			"Refunded"), VERIFICATION_REQUIRED("Verification Required"), COMPLETED("Completed"), CANCELLED(
					"Cancelled"), DECLINED("Declined"), AWAITING_FULFILLMENT("Awaiting Fulfillment"), AWAITING_SHIPMENT(
							"Awaiting Shipment"), AWAITING_PICKUP(
									"Awaiting Pickup"), PARTIALLY_SHIPPED("Partially Shipped"), SHIPPED("Shipped");

	static final String NO_MATCHING_ENUMS_ERROR_MESSAGE = "No matching enum found for %s";
	private final String value;

	private Status(final String value) {
		this.value = value;
	}

	public static Status toEnum(String value) {
		if (PENDING.toString().equals(value)) {
			return Status.PENDING;
		} else if (AWAITING_PAYMENT.toString().equals(value)) {
			return Status.AWAITING_PAYMENT;
		} else if (DISPUTED.toString().equals(value)) {
			return Status.DISPUTED;
		} else if (REFUNDED.toString().equals(value)) {
			return Status.REFUNDED;
		} else if (VERIFICATION_REQUIRED.toString().equals(value)) {
			return Status.VERIFICATION_REQUIRED;
		} else if (COMPLETED.toString().equals(value)) {
			return Status.COMPLETED;
		} else if (CANCELLED.toString().equals(value)) {
			return Status.CANCELLED;
		} else if (DECLINED.toString().equals(value)) {
			return Status.DECLINED;
		} else if (AWAITING_FULFILLMENT.toString().equals(value)) {
			return Status.AWAITING_FULFILLMENT;
		} else if (AWAITING_SHIPMENT.toString().equals(value)) {
			return Status.AWAITING_SHIPMENT;
		} else if (AWAITING_PICKUP.toString().equals(value)) {
			return Status.AWAITING_PICKUP;
		} else if (PARTIALLY_SHIPPED.toString().equals(value)) {
			return Status.PARTIALLY_SHIPPED;
		} else if (SHIPPED.toString().equals(value)) {
			return Status.SHIPPED;
		}
		throw new IllegalArgumentException(String.format(NO_MATCHING_ENUMS_ERROR_MESSAGE, value));
	}

	@Override
	public String toString() {
		return value;
	}
}
