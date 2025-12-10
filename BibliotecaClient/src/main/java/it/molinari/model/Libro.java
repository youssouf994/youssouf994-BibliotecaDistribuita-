package it.molinari.model;

public class Libro extends Item {

    private String isbn;
    private String genere;

    public Libro() {
        this.tipologia = "Libro";
    }

    public Libro(String nome, String autore, String isbn, String genere, int quanti) {
        this.nome = nome;
        this.autore = autore;
        this.isbn = isbn;
        this.genere = genere;
        this.tipologia = "Libro";
        this.quanti = quanti;
    }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getGenere() { return genere; }
    public void setGenere(String genere) { this.genere = genere; }
}
