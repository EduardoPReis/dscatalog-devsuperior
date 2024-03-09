package com.devsuperior.dscatalog.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;

@RestController
@RequestMapping(value = "/products")
public class ProductResource {

	// endpoint buscar todos Lista mocada
	/*
	 * @GetMapping public ResponseEntity<List<Product>> findAll(){ List<Product>
	 * list = new ArrayList<>(); list.add(new Product(1L, "Books")); list.add(new
	 * Product(2L, "Electronics"));
	 * 
	 * //Retorna a lista criada no corpo http da requisição return
	 * ResponseEntity.ok().body(list); }
	 */

	// endpoint buscar todos, usando service

	// declarando dependência service
	@Autowired
	private ProductService services;

	// buscar
	@GetMapping
	public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {
		Page<ProductDTO> list = services.findAllPaged(pageable);
		// List<ProductDTO> list = services.findAll();
		// Retorna a lista criada no corpo http da requisição
		return ResponseEntity.ok().body(list);
	}

	// buscar por ID
	@GetMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
		ProductDTO dto = services.findById(id);
		return ResponseEntity.ok().body(dto);
	}

	// inserir
	@PostMapping
	public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO dto) {
		dto = services.insert(dto);
		// passando a URI que insere no corpo da requisição um 201
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();

		return ResponseEntity.created(uri).body(dto);
	}

	// atualizar
	@PutMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO dto) {
		dto = services.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		services.delete(id);
		return ResponseEntity.noContent().build();
	}

}
