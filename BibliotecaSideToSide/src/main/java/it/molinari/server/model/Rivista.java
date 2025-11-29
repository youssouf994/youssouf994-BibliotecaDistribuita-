package it.molinari.server.model;

import java.time.*;
import java.util.Scanner;

public class Rivista extends Item
{
	public String edizione, periodicita;
	public String periodo[]={"giornaliero", "settimanale", "mensile"};
	public int codice;

	
	public Rivista()
	{
		
	}
	
	public Rivista(String nome, String autore, boolean isPrestato, String richiedente, String edizione, String periodicita, int codice, String tipologia, LocalTime orarioPrestito)
	{
			super(nome, autore, isPrestato, richiedente, codice, tipologia, orarioPrestito);
			this.edizione=edizione;
			this.periodicita=periodicita;
	}
	
	@Override
	public String toString() 
	{
	    return super.toString() +
	           "  Edizione       : " + edizione + "\n" +
	           "  Periodicità    : " + periodicita + "\n";
	}


	public static Rivista compilaItem(Scanner sc, int id) 
	{
		System.out.print("Inserisci nome: ");
	    String nome = sc.nextLine();
	    System.out.print("Inserisci autore: ");
	    String autore = sc.nextLine();
	    boolean isPrestato = false;
	    String richiedente = null;

	    System.out.print("Inserisci edizione: ");
	    String edizione = sc.nextLine();
	    System.out.print("Inserisci periodicità: ");
	    String periodicita = sc.nextLine();
	    int codice = id;

	    String tipologia = "Rivista";
	    LocalTime orarioPrestito = null;

        return new Rivista(nome, autore, isPrestato, richiedente,
                            edizione, periodicita, codice, tipologia, orarioPrestito);
    }

	public String getEdizione() {
		return edizione;
	}

	public void setEdizione(String edizione) {
		this.edizione = edizione;
	}

	public String getPeriodicita() {
		return periodicita;
	}

	public void setPeriodicita(String periodicita) {
		this.periodicita = periodicita;
	}

	public String[] getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String[] periodo) {
		this.periodo = periodo;
	}

	public int getCodice() {
		return codice;
	}

	public void setCodice(int codice) {
		this.codice = codice;
	}

	
	
}
