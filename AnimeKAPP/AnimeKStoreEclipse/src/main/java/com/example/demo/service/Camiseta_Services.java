package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.Camiseta_Repository;
import com.example.demo.model.Camiseta;


@Service
public class Camiseta_Services {
	
	@Autowired
	Camiseta_Repository camiseta_Repository;
	
	public void save (Camiseta camiseta) {
		camiseta_Repository.insert(camiseta);
	}
	public List<Camiseta> findAll(){
		return camiseta_Repository.findAll();
		
		
	}
	public void update (Camiseta camiseta) {
		camiseta_Repository.insert(camiseta);
	}

}
