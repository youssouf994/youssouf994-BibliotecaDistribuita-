package it.molinari.server.model;

import java.time.*;
import java.util.Scanner;

public class Cd extends Item
{
	public int numTracce, durata;
	
	public Cd()
	{
		
	}
	
	public Cd(String nome, String autore, boolean isPrestato, String richiedente, int numTracce, int durata, int codice, String tipologia, LocalTime orarioPrestito)
	{
		super(nome, autore, isPrestato, richiedente, codice, tipologia, orarioPrestito);
		this.numTracce=numTracce;
		this.durata=durata;
	}
	
	@Override
	public String toString() 
	{
	    return super.toString() +
	           "  Tracce         : " + numTracce + "\n" +
	           "  Durata         : " + durata + " minuti\n";
	}


	 public static Cd compilaItem(Scanner sc, int id) 
	 {
		 System.out.print("Inserisci nome: ");
		    String nome = sc.nextLine();
		    System.out.print("Inserisci autore: ");
		    String autore = sc.nextLine();
		    boolean isPrestato = false;
		    String richiedente = null;

		    System.out.print("Inserisci numero tracce: ");
		    int numTracce = Integer.parseInt(sc.nextLine());
		    System.out.print("Inserisci durata (in minuti): ");
		    int durata = Integer.parseInt(sc.nextLine());
		    int codice = id;

		    String tipologia = "Cd";
		    LocalTime orarioPrestito = null;

	        return new Cd(nome, autore, isPrestato, richiedente,
	                      numTracce, durata, codice, tipologia, orarioPrestito);
	    }

	 public int getNumTracce() {
		 return numTracce;
	 }

	 public void setNumTracce(int numTracce) {
		 this.numTracce = numTracce;
	 }

	 public int getDurata() {
		 return durata;
	 }

	 public void setDurata(int durata) {
		 this.durata = durata;
	 }

}
