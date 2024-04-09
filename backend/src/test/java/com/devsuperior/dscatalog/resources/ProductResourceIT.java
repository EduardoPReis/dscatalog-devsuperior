package com.devsuperior.dscatalog.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIT {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objMapper;
	
	private Long existingId;
	private Long nonExistingId;
	private Long countTotalProduct;
	private ProductDTO productDTO;
	private String jsonBody;

	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProduct = 25L;
		productDTO = Factory.createdProductDTO();
		jsonBody = objMapper.writeValueAsString(productDTO);
	}

	@Test
	public void findAllShouldReturnSortedPageWhenSortByName() throws Exception{
		
		mockMvc.perform(get("/products?page=0&size=12&sort=name,asc")
				.accept(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.totalElements").value(countTotalProduct))
	            .andExpect(jsonPath("$.content").exists())
	            .andExpect(jsonPath("$.content[0].name").value("Macbook Pro"))
	            .andExpect(jsonPath("$.content[1].name").value("PC Gamer"))
	            .andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists()throws Exception{
		
		String expectedName = productDTO.getName();
		String expectedDescription = productDTO.getDescription();
		
		mockMvc.perform(put("/products/{id}",existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$.id").value(existingId))
		        .andExpect(jsonPath("$.name").value(expectedName))
		        .andExpect(jsonPath("$.description").value(expectedDescription));	
	}
	
	@Test
	public void updateShouldReturnNotFoundOWhenIdDoesNotExists()throws Exception{
		
		String expectedName = productDTO.getName();
		String expectedDescription = productDTO.getDescription();
		
		mockMvc.perform(put("/products/{id}",nonExistingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		        .andExpect(status().isNotFound());
	}
}
