package com.devsuperior.dscatalog.tests;

import java.time.Instant;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

public class Factory {

	public static Product createdProduct() {
		Product product = new Product(1L, "Phone","Good Phone",800.0,"http://img.com/img.png",Instant.parse("2024-03-09T03:00:00z"));
		product.getCategories().add(new Category(2L,"Electronics"));
		return product;
	}
	
	public static ProductDTO createdProductDTO() {
		Product product = createdProduct();
		return new ProductDTO(product,product.getCategories());
	}
	
	public static Category createdCategory() {
		return new Category(2L,"Electronics");
	}
}
