package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Figuras")
public class Figura {
	
    @Id
	private int id;
    private String tipo;
    private String nombre_personaje;
    private String anime;
    private int cantidad;
    private double precio;
    private String imagen_figura;
    
    public Figura() {
		super();
	}
    
    

	public Figura(int id, String tipo, String nombre_personaje, String anime, int cantidad, double precio,
			String imagen_figura) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.nombre_personaje = nombre_personaje;
		this.anime = anime;
		this.cantidad = cantidad;
		this.precio = precio;
		this.imagen_figura = imagen_figura;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNombre_personaje() {
		return nombre_personaje;
	}

	public void setNombre_personaje(String nombre_personaje) {
		this.nombre_personaje = nombre_personaje;
	}

	public String getAnime() {
		return anime;
	}

	public void setAnime(String anime) {
		this.anime = anime;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getImagen_figura() {
		return imagen_figura;
	}

	public void setImagen_figura(String imagen_figura) {
		this.imagen_figura = imagen_figura;
	}
    
	
}