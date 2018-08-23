package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.Camiseta_Repository;
import com.example.demo.model.Camiseta;
import com.mongodb.DBCollection;



@RestController
@RequestMapping("/Camiseta")
public class Controller {
	
	
	
	@Autowired
	Camiseta_Repository camiseta_Repository;

	@RequestMapping(method = RequestMethod.POST, consumes= MediaType.APPLICATION_JSON_VALUE)
	public void createCamiseta(@RequestBody Camiseta camiseta) {
		camiseta_Repository.save(camiseta);
		
		
	}
	@RequestMapping(value="/findAll", method = RequestMethod.GET, consumes= MediaType.ALL_VALUE)
	public List<Camiseta> findAll(){
		return camiseta_Repository.findAll();
	}
	@RequestMapping( method = RequestMethod.PUT, consumes= MediaType.APPLICATION_JSON_VALUE)
	public void update (@RequestBody Camiseta camiseta) {
		camiseta_Repository.save(camiseta);
	}
	@RequestMapping(value="anime/{anime}", method = RequestMethod.GET)
	public List<Camiseta> findAnime(@PathVariable String anime) {
		return camiseta_Repository.findByAnimeQuery(anime);
	}
	@RequestMapping(value="talla/{talla}", method = RequestMethod.GET)
	public List<Camiseta> findTalla(@PathVariable String talla) {
		return camiseta_Repository.findByTallaQuery(talla);
	}
	

	
	

}
