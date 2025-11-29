package it.molinari.server.model;

import java.time.*;
import java.util.Scanner;

public class Libro extends Item
{
	public String isin, genere;
	public int numPag;

	
	public Libro()
	{
		
	}
	
	public Libro(String nome, String autore, boolean isPrestato, String richiedente, String isin, String genere, int numPag, int codice, String tipologia, LocalTime orarioPrestito)
	{
		super(nome, autore, isPrestato, richiedente, codice, tipologia, orarioPrestito);
		this.isin=isin;
		this.genere=genere;
		this.numPag=numPag;
	}
	
	@Override
	public String toString() 
	{
	    return super.toString() +
	           "  ISIN           : " + isin + "\n" +
	           "  Genere         : " + genere + "\n" +
	           "  Pagine         : " + numPag + "\n";
	}


	 public static Libro compilaItem(Scanner sc, int id) {
	        System.out.print("Inserisci nome: ");
	        String nome = sc.nextLine();
	        System.out.print("Inserisci autore: ");
	        String autore = sc.nextLine();
	        boolean isPrestato = false;
	        String richiedente = null;
	        System.out.print("Inserisci ISIN: ");
	        String isin = sc.nextLine();
	        System.out.print("Inserisci genere: ");
	        String genere = sc.nextLine();
	        System.out.print("Inserisci numero pagine: ");
	        int numPag = Integer.parseInt(sc.nextLine());
	        int codice = id;
	        String tipologia ="Libro";
	        LocalTime orarioPrestito = null;

	        return new Libro(nome, autore, isPrestato, richiedente,
	                         isin, genere, numPag, codice, tipologia, orarioPrestito);
	    }

	 public String getIsin() {
		 return isin;
	 }

	 public void setIsin(String isin) {
		 this.isin = isin;
	 }

	 public String getGenere() {
		 return genere;
	 }

	 public void setGenere(String genere) {
		 this.genere = genere;
	 }

	 public int getNumPag() {
		 return numPag;
	 }

	 public void setNumPag(int numPag) {
		 this.numPag = numPag;
	 }

	 
	 
	

}
