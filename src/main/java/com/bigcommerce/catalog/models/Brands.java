package com.bigcommerce.catalog.models;

import java.util.List;

public class Brands implements PaginatedModel<Brand> {

	private final List<Brand> brands;
	private final Pagination pagination;

	public Brands(final List<Brand> brands, final Pagination pagination) {
		this.brands = brands;
		this.pagination = pagination;
	}

	public List<Brand> getBrands() {
		return brands;
	}

	public Pagination getPagination() {
		return pagination;
	}

	@Override
	public List<Brand> getItems() {
		return getBrands();
	}

}
