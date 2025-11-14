package com.eduardo.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.eduardo.dscatalog.entities.Product;
import com.eduardo.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repository;
	
	private Long ExistingId = 1L;
	private Long nonExistingId = 1000L;
	private Long countTotalProduct;
	
	@BeforeEach
	public void setUp() throws Exception {
		ExistingId = 1L;
		nonExistingId = 1000L;
		countTotalProduct = 25L;
	}
	
	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
		
		Product product = Factory.createdProduct();
		product.setId(null);
		
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProduct + 1, product.getId());
		
	}
	
	@Test
	public void deleShouldDeleteObjectWhenIdExists() {
		
		repository.deleteById(ExistingId);
		
		Optional<Product> result = repository.findById(ExistingId);
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void deleteShouldTrowEmptyResultDataAccessExceptionDoesNotExists() {		
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () ->{
			repository.deleteById(nonExistingId);
		});
		
	}
}
