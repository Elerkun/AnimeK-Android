package com.example.demo.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.demo.model.Camiseta;
import com.example.demo.model.Figura;

public interface Figura_Repository extends MongoRepository<Figura, Integer> {
	
	 @Query("{anime : ?0}")
	 public List<Figura> findByAnimeQuery(String animeName);
	 @Query("{nombre_personaje : ?0}")
	 public List<Figura> findByNombrePersonajeQuery(String talla);

}
