package com.eduardo.dscatalog.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.eduardo.dscatalog.dto.ProductDTO;
import com.eduardo.dscatalog.entities.Product;
import com.eduardo.dscatalog.repositories.ProductRepository;
import com.eduardo.dscatalog.services.exceptions.DataBaseException;
import com.eduardo.dscatalog.services.exceptions.EntityNotFoundException;
import com.eduardo.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServicesTests {

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	private Long existingId;	
	private Long nonExistingId;
	private Long dependentId;
	private PageImpl<Product>page;
	private Product product;
	
	@BeforeEach
	public void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		product = Factory.createdProduct();
		page = new PageImpl<>(List.of(product));
		
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
        

		Mockito.doNothing().when(repository).deleteById(existingId);
		
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}
	
	
	@Test
	public void findAllPagedShouldReturnPaged() {
		
		Pageable pageable = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAllPaged(pageable);
		Assertions.assertNotNull(result);
		
		Mockito.verify(repository,Mockito.times(1)).findAll(pageable);
	}
	
	
	//DataIntegrityViolationException
	@Test
	public void deleteShouldThrowDataIntegrityViolationExceptionWhenDependentId() {
		
		Assertions.assertThrows(DataBaseException.class,()-> {
			service.delete(dependentId);
		});
		
		Mockito.verify(repository, Mockito.times(1)/*Controlo quantidade de vezes da chamada*/).deleteById(dependentId);
	}
	
	
	@Test
	public void deleteShouldThrowEntityNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(EntityNotFoundException.class,()-> {
			service.delete(nonExistingId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
	}
	
	@Test
	public void deleteShouldDoNthingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(()-> {
			service.delete(existingId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}
	
	
}
