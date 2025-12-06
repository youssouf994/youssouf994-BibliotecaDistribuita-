package it.molinari.model;

abstract public class Items {
	
	protected String nome;
	protected String autore;
	protected String anno;
	protected int quantita = 1;
	
	public Items(String nome, String autore, String anno) {
		this.nome = nome;
		this.autore = autore;
		this.anno = anno;
	}
	public Items(String nome, String autore, String anno, int quantita) {
		this.nome = nome;
		this.autore = autore;
		this.anno = anno;
		this.quantita = quantita;
	}
	
	public int getQuantita() {
		return quantita;
	}
	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}
	
	public abstract String getCodice();
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getAutore() {
		return autore;
	}
	public void setAutore(String autore) {
		this.autore = autore;
	}
	public String getAnno() {
		return anno;
	}
	public void setAnno(String anno) {
		this.anno = anno;
	}
	
	@Override
	public String toString() {
		return String.format("%s - %s (%d)", nome, autore, anno);
    }
}