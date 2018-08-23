package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Camisetas")
public class Camiseta {
	    @Id
	    private int id;
	    private String tipo;
	    private double precio;
	    private String talla;
	    private String anime;
	    private int cantidad;
	    private String imagen_camiseta;
	    
	    
	    public Camiseta(int id, String tipo, double precio, String talla, String anime, int cantidad,
				String imagen_camiseta) {
			super();
			this.id = id;
			this.tipo = tipo;
			this.precio = precio;
			this.talla = talla;
			this.anime = anime;
			this.cantidad = cantidad;
			this.imagen_camiseta = imagen_camiseta;
		}


		public Camiseta() {
	        super();
	    }


		public String getImagen_camiseta() {
			return imagen_camiseta;
		}

		public void setImagen_camiseta(String imagen_camiseta) {
			this.imagen_camiseta = imagen_camiseta;
		}

	    public String getTipo() {
	        return tipo;
	    }

	    public void setTipo(String tipo) {
	        this.tipo = tipo;
	    }

	    public double getPrecio() {
	        return precio;
	    }

	    public void setPrecio(double precio) {
	        this.precio = precio;
	    }

	    public String getTalla() {
	        return talla;
	    }

	    public void setTalla(String talla) {
	        this.talla = talla;
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


		public int getId() {
			return id;
		}


		public void setId(int id) {
			this.id = id;
		}
}
