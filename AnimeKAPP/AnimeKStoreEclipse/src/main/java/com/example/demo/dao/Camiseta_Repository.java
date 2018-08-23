package com.example.demo.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.demo.model.Camiseta;

public interface Camiseta_Repository extends MongoRepository<Camiseta, String>{
	

	 @Query("{anime : ?0}")
	 public List<Camiseta> findByAnimeQuery(String animeName);
	 @Query("{talla : ?0}")
	 public List<Camiseta> findByTallaQuery(String talla);
	 
	
	
	

}
