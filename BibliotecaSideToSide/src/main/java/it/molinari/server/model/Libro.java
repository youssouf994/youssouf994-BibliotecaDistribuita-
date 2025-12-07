package it.molinari.server.model;

import java.time.*;
import java.util.Scanner;

public class Libro extends Item
{
	public String isbn, genere;
	

	
	public Libro()
	{
		
	}
	
	public Libro(String nome, String autore, String isin, String genere, String tipologia, int quanti)
	{
		super(nome, autore, tipologia, quanti);
		this.isbn=isin;
		this.genere=genere;
		this.quanti=quanti;
	}
	
	@Override
	public String toString() 
	{
	    return super.toString() +
	           "  ISIN           : " + isbn + "\n" +
	           "  Genere         : " + genere + "\n" ;
	}


	 public static Libro compilaItem(Scanner sc, int id) {
	        System.out.print("Inserisci nome: ");
	        String nome = sc.nextLine();
	        System.out.print("Inserisci autore: ");
	        String autore = sc.nextLine();
	        System.out.print("Inserisci ISIN: ");
	        String isin = sc.nextLine();
	        System.out.print("Inserisci genere: ");
	        String genere = sc.nextLine();
	        System.out.print("quantità inserita: ");
	        int quanti = Integer.parseInt(sc.nextLine());
	        String tipologia ="Libro";

	        return new Libro(nome, autore,  
	                         isin, genere, tipologia, quanti);
	    }

	 public String getIsbn() {
		 return isbn;
	 }

	 public void setIsbn(String isin) {
		 this.isbn = isin;
	 }

	 public String getGenere() {
		 return genere;
	 }

	 public void setGenere(String genere) {
		 this.genere = genere;
	 }


	public int getQuanti() {
		return quanti;
	}

	public void setQuanti(int quanti) {
		this.quanti = quanti;
	}

	 
	 
	

}
