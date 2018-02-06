package com.bigcommerce.catalog.models;

import java.util.List;

public class ProductImages {
	private final List<ProductImage> productImages;
	private final Pagination pagination;

	public ProductImages(final List<ProductImage> brands, final Pagination pagination) {
		this.productImages = brands;
		this.pagination = pagination;
	}

	public List<ProductImage> getProductImages() {
		return productImages;
	}

	public Pagination getPagination() {
		return pagination;
	}
}
