package com.eduardo.dscatalog.tests;

import java.time.Instant;

import com.eduardo.dscatalog.dto.ProductDTO;
import com.eduardo.dscatalog.entities.Category;
import com.eduardo.dscatalog.entities.Product;

public class Factory {

	public static Product createdProduct() {
		Product product = new Product(1L,"Phone","Good Phone",800.0,"http://google.com",Instant.parse("2025-10-31T03:00:00Z"));
		product.getCategories().add(new Category(2L,"Eletronics"));
		return product;
	}
	
	public static ProductDTO createProductDto() {
		Product product =  createdProduct();
		return new ProductDTO(product, product.getCategories());
	}
}

