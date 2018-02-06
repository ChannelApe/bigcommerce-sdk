package com.bigcommerce.catalog.models;

import java.util.List;

public class Metafields {
	private final List<Metafield> metafields;
	private final Pagination pagination;

	public Metafields(final List<Metafield> metafields, final Pagination pagination) {
		this.metafields = metafields;
		this.pagination = pagination;
	}

	public List<Metafield> getMetafields() {
		return metafields;
	}

	public Pagination getPagination() {
		return pagination;
	}

}
