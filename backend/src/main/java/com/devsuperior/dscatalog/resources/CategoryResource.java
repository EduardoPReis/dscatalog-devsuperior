package com.devsuperior.dscatalog.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.services.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

	// endpoint buscar todos Lista mocada
	/*
	 * @GetMapping public ResponseEntity<List<Category>> findAll(){ List<Category>
	 * list = new ArrayList<>(); list.add(new Category(1L, "Books")); list.add(new
	 * Category(2L, "Electronics"));
	 * 
	 * //Retorna a lista criada no corpo http da requisição return
	 * ResponseEntity.ok().body(list); }
	 */

	// endpoint buscar todos, usando service

	// declarando dependência service
	@Autowired
	private CategoryService services;

	@GetMapping
	public ResponseEntity<List<Category>> findAll() {
		List<Category> list = services.findAll();
		// Retorna a lista criada no corpo http da requisição
		return ResponseEntity.ok().body(list);
	}
}
