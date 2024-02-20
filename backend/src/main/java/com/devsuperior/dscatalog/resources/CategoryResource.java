package com.devsuperior.dscatalog.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscatalog.dto.CategoryDTO;
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
	
	//pegar
	@GetMapping
	public ResponseEntity<List<CategoryDTO>> findAll() {
		List<CategoryDTO> list = services.findAll();
		// Retorna a lista criada no corpo http da requisição
		return ResponseEntity.ok().body(list);
	}
	
	//inserir
	@PostMapping
	public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto){
		dto = services.insert(dto);
		//passando a URI que insere no corpo da requisição um 201
		URI uri= ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		
		return ResponseEntity.created(uri).body(dto);
	}
}
