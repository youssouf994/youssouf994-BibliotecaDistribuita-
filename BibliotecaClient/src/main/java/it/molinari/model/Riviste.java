package it.molinari.model;

public class Riviste extends Items {

	private String id;
	private String tipo;

	public Riviste(String nome, String autore, String anno, String id) {
		super(nome, autore, anno);
		this.setid(id);
		if (tipo == null) {
			tipo = "Rivista";
		}
	}
	public Riviste(String nome, String autore, String anno, String id, int quantita) {
		super(nome, autore, anno, quantita);
		this.setid(id);
		if (tipo == null) {
			tipo = "Rivista";
		}
	}
	public Riviste(String tipo, String nome, String autore, String anno, String id) {
		super(nome, autore, anno);
		this.setid(id);
		this.tipo = tipo;
	}

	public void setid(String id) {
		this.id = id;
	}
	
	@Override
    public String toString() {
        return String.format("[%s] %s - %s (Anno:%s) Codice a barre:%s, Quantita:%d", tipo, nome, autore, anno, id, quantita);
    }
	
	@Override
	public String getCodice() {
		return id;
	}
}
