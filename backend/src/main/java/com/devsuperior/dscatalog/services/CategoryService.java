package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;


@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;
	
	/*
	@Transactional(readOnly = true)
	//conversa com o banco e pede recuperar por todos
	public List<Category> findAll(){
		return repository.findAll();
	}*/
	
	@Transactional(readOnly = true)
	//conversa com o banco e pede recuperar por todos usando a dto
	public List<CategoryDTO> findAll(){
		//repository comunica-se com entidade, neste caso crio uma lista que pega da entitade e tranforma para ser enviado ao DTO
		List<Category> list =  repository.findAll();
		
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
		
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
	 Category entity = new Category();
	 entity.setName(dto.getName());
	 entity = repository.save(entity);
	 return new CategoryDTO(entity);
	}
	
	
	
}
