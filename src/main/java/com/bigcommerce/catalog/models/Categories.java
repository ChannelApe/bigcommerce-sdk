package com.bigcommerce.catalog.models;

import java.util.List;

public class Categories implements PaginatedModel<Category>{

	private final List<Category> categories;
	private final Pagination pagination;

	public Categories(final List<Category> categories, final Pagination pagination) {
		this.categories = categories;
		this.pagination = pagination;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public Pagination getPagination() {
		return pagination;
	}

    @Override
    public List<Category> getItems() {
        return getCategories();
    }

}
