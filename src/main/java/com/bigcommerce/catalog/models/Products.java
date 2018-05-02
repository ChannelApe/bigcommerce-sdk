package com.bigcommerce.catalog.models;

import java.util.List;

public class Products implements PaginatedModel<Product> {

	private final List<Product> products;
	private final Pagination pagination;

	public Products(final List<Product> products, final Pagination pagination) {
		this.products = products;
		this.pagination = pagination;
	}

	public List<Product> getProducts() {
		return products;
	}

	public Pagination getPagination() {
		return pagination;
	}

    @Override
    public List<Product> getItems() {
        return getProducts();
    }

}
