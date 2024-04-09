package com.devsuperior.dscatalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService service;
	
	@Autowired
	private ObjectMapper objMapper;
	
	private Long existingId;
	private Long nonExistingId;
	private Long dependenceId;
	private ProductDTO productDTO;
	private PageImpl<ProductDTO> page;
	private String jsonBody;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 2L;
		dependenceId = 3L;
		
		productDTO = Factory.createdProductDTO();
		page = new PageImpl<>(List.of(productDTO));
		jsonBody = objMapper.writeValueAsString(productDTO);
		
		when(service.findAllPaged(any())).thenReturn(page);
		when(service.findById(existingId)).thenReturn(productDTO);
		when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		when(service.insert(any())).thenReturn(productDTO);
		
		when(service.update(eq(existingId), any())).thenReturn(productDTO);
		when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
		
		
		doNothing().when(service).delete(existingId);
		doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
		doThrow(DatabaseException.class).when(service).delete(dependenceId);
		
	}
	
	@Test
	public void findAllShouldReturnPage() throws Exception{
		mockMvc.perform(get("/products")).andExpect(status().isOk());
	}
	
	@Test
	public void findByIdShouldReturnProductWhenIdExist()  throws Exception{
		mockMvc.perform(get("/products/{id}",existingId))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").exists());		
	}
	
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdNonExist() throws Exception{
		mockMvc.perform(get("/products/{id}",nonExistingId))
		.andExpect(status().isNotFound());
		
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists()throws Exception{
		
		mockMvc.perform(put("/products/{id}",existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$.id").exists())
		        .andExpect(jsonPath("$.name").exists())
		        .andExpect(jsonPath("$.description").exists());
		
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{
		
		mockMvc.perform(put("/products/{id}",nonExistingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		        .andExpect(status().isNotFound());
		
	}
	
	@Test
	public void insertShouldReturnProductDTOCreated() throws Exception{
		
		mockMvc.perform(post("/products")
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		        .andExpect(status().isCreated())
		        .andExpect(jsonPath("$.id").exists())
		        .andExpect(jsonPath("$.name").exists())
		        .andExpect(jsonPath("$.description").exists());
	}
	
	@Test
	public void deleteShouldReturnNoContentSholdIdExist()  throws Exception{
		
		mockMvc.perform(delete("/products/{id}",existingId)
				.accept(MediaType.APPLICATION_JSON))
		        .andExpect(status().isNoContent());  
	}
	
	@Test
	public void deleteShouldReturnNotFoundSholdIdDoesNotExist()  throws Exception{
		mockMvc.perform(delete("/products/{id}",nonExistingId))
		        .andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenIdIsDependent() throws Exception{
	 
		mockMvc.perform(delete("/products/{id}", dependenceId))
		.andExpect(status().isBadRequest());
	}

}
