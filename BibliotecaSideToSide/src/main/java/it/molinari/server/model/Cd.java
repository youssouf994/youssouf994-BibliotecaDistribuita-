package it.molinari.server.model;

import java.util.Scanner;


public class Cd extends Item
{
	public int numTracce, durata;
	private int id;

	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Cd()
	{
		
	}
	
	public Cd(String nome, String autore, int numTracce, int durata,  String tipologia,  int quanti)
	{
		super(nome, autore, tipologia, quanti);
		this.numTracce=numTracce;
		this.durata=durata;
		this.quanti=quanti;
	}
	
	@Override
	public String toString() 
	{
	    return super.toString() +
	           "  Tracce         : " + numTracce + "\n" +
	           "  Durata         : " + durata + " minuti\n"+
	           "  Quantità:		 : "+quanti + "pezzi";	
	}


	 public static Cd compilaItem(Scanner sc, int id) 
	 {
		 System.out.print("Inserisci nome: ");
		    String nome = sc.nextLine();
		    System.out.print("Inserisci autore: ");
		    String autore = sc.nextLine();

		    System.out.print("Inserisci numero tracce: ");
		    int numTracce = Integer.parseInt(sc.nextLine());
		    System.out.print("Inserisci durata (in minuti): ");
		    int durata = Integer.parseInt(sc.nextLine());
		    System.out.print("Quantità inserita: ");
		    int quanti = Integer.parseInt(sc.nextLine());
		    

		    String tipologia = "Cd";

	        return new Cd(nome, autore, numTracce, durata, tipologia, quanti);
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

	public int getQuanti() {
		return quanti;
	}

	public void setQuanti(int quanti) {
		this.quanti = quanti;
	}
	 
	 

}
