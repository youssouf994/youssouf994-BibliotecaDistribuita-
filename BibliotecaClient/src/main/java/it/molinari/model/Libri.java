package it.molinari.model;

public class Libri extends Items{
	
	private String isbn;
	private String tipo;
	
	public Libri(String nome, String autore, String anno, String isbn) {
		super(nome, autore, anno);
		this.setIsbn(isbn);
		if (tipo == null) {
			tipo = "Libro";
		}
	}
	public Libri(String nome, String autore, String anno, String isbn, int quantita) {
		super(nome, autore, anno, quantita);
		this.setIsbn(isbn);
		if (tipo == null) {
			tipo = "Libro";
		}
	}
	public Libri(String tipo, String nome, String autore, String anno, String isbn) {
		super(nome, autore, anno);
		this.setIsbn(isbn);
		this.tipo = tipo;
	}
	
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	@Override
    public String toString() {
        return String.format("[%s] %s - %s (Anno:%s) ISBN:%s, Quantita':%d", tipo, nome, autore, anno, isbn, quantita);
    }
	@Override
	public String getCodice() {
		return isbn;
	}
}