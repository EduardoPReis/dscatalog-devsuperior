package com.eduardo.dscatalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.eduardo.dscatalog.dto.ProductDTO;
import com.eduardo.dscatalog.services.ProductService;
import com.eduardo.dscatalog.services.exceptions.EntityNotFoundException;
import com.eduardo.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(ProductResource.class)
public class ProdcutResourceTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService service;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private ProductDTO productDto;
	private PageImpl<ProductDTO>page;
	private Long existingId;	
	private Long nonExistingId;
	private Long dependentId;
	
	
	@BeforeEach
	public void setUp() throws Exception {
		
		existingId = 1L;	
		nonExistingId = 2L;
		dependentId = 3L;
		
		productDto = Factory.createProductDto();
		page = new PageImpl<>(List.of(productDto));
		
		when(service.findAllPaged(any())).thenReturn(page);
		
		when(service.findById(existingId)).thenReturn(productDto);
		when(service.findById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		when(service.update(eq(existingId), any())).thenReturn(productDto);
		when(service.update(eq(nonExistingId), any())).thenThrow(EntityNotFoundException.class);
		
		doNothing().when(service).delete(existingId);
		doThrow(EmptyResultDataAccessException.class).when(service).delete(nonExistingId);
		doThrow(DataIntegrityViolationException.class).when(service).delete(dependentId);
		
	}
	
	@Test
	public void findAllShouldReturnPage() throws Exception{
		mockMvc.perform(get("/products")).andExpect(status().isOk());
	}
	
	@Test
	public void findByIdReturnProductWhenIdExists() throws Exception{
		mockMvc.perform(get("/products/{id}",existingId))
		.andExpect(status()
				.isOk())
	            .andExpect(jsonPath("$.id").exists())
	            .andExpect(jsonPath("$.name").exists())
	            .andExpect(jsonPath("$.description").exists());
		
	}

	@Test
	public void findByIdReturnEntityNotFoundExceptionWhenIdDoesNotExist() throws Exception{
		mockMvc.perform(get("/products/{id}",nonExistingId))
		.andExpect(status()
				.isNotFound());
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception {

	    String jsonBody = objectMapper.writeValueAsString(productDto);

	    mockMvc.perform(
	            put("/products/{id}", existingId)
	                    .content(jsonBody)
	                    .contentType(MediaType.APPLICATION_JSON)
	                    .accept(MediaType.APPLICATION_JSON)
	        )
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").exists())
            .andExpect(jsonPath("$.description").exists());
	}

	
	@Test
	public void updateShouldReturnDTONotFoundExceptionWhenIdDoesNotExist() throws Exception{
	    String jsonBody = objectMapper.writeValueAsString(productDto);
	    
	    mockMvc.perform(
	            put("/products/{id}", nonExistingId)
	                    .content(jsonBody)
	                    .contentType(MediaType.APPLICATION_JSON)
	                    .accept(MediaType.APPLICATION_JSON)
	        )
	        .andExpect(status().isNotFound());
	}
}
