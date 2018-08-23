package com.example.demo.controller;

import java.net.UnknownHostException;
import java.sql.Array;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.Figura_Repository;
import com.example.demo.model.Figura;
import com.mongodb.DB;


import com.mongodb.MongoClient;
import com.mongodb.MongoException;

@RestController
@RequestMapping("/Figura")
public class Controller_Figura {
	
	@Autowired
	Figura_Repository figura_Repository;
	
	
	
	
	/*@RequestMapping("/Index")
	public String index(HttpServletRequest request) {
		try{
		     MongoClient mongo = new MongoClient();
		     @SuppressWarnings("deprecation")
			DB db = mongo.getDB("Tienda");
		     Set<String> collectionNames= db.getCollectionNames();
		        for(String collection: collectionNames) {
		        	request.setAttribute("collection", collectionNames);
					System.out.println(collection);
				    }
		        } catch (MongoException e) {
				e.printStackTrace();
			    }  
		  return "/Index";
	
	}*/
	@RequestMapping(method = RequestMethod.POST, consumes= MediaType.APPLICATION_JSON_VALUE)
	public void createFigura(@RequestBody Figura figura) {
	     figura_Repository.save(figura);
		
		
	}
	@RequestMapping(value="/findAll", method = RequestMethod.GET, consumes= MediaType.ALL_VALUE)
	public List<Figura> findAll(){
		return figura_Repository.findAll();
	}
	@RequestMapping(value="anime/{anime}", method = RequestMethod.GET)
	public List<Figura> findAnime(@PathVariable String anime) {
		return figura_Repository.findByAnimeQuery(anime);
	}
	@RequestMapping(value="nombre/{nombre_personaje}", method = RequestMethod.GET)
	public List<Figura> findTalla(@PathVariable String nombre_personaje) {
		return figura_Repository.findByNombrePersonajeQuery(nombre_personaje);
	}
	
}

