package com.bigcommerce.catalog.models;

import java.util.List;

public interface PaginatedModel<T> {
	Pagination getPagination();

	List<T> getItems();
}
