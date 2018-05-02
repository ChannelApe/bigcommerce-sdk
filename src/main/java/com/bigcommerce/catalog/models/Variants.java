package com.bigcommerce.catalog.models;

import java.util.List;

public class Variants {

	private final List<Variant> variants;
	private final Pagination pagination;

	public Variants(final List<Variant> products, final Pagination pagination) {
		this.variants = products;
		this.pagination = pagination;
	}

	public List<Variant> getVariants() {
		return variants;
	}

	public Pagination getPagination() {
		return pagination;
	}

}
