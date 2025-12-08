package it.molinari.server.model;


import java.util.Scanner;

public class Rivista extends Item
{
	public String edizione, periodicita;
	public String periodo[]={"giornaliero", "settimanale", "mensile"};
	public int codice;

	
	public Rivista()
	{
		
	}
	
	public Rivista(String nome, String autore, String edizione, String periodicita, String tipologia, int quanti)
	{
			super(nome, autore, tipologia, quanti);
			this.edizione=edizione;
			this.periodicita=periodicita;
			this.quanti=quanti;
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

	    System.out.print("Inserisci edizione: ");
	    String edizione = sc.nextLine();
	    System.out.print("Inserisci periodicità: ");
	    String periodicita = sc.nextLine();
	    System.out.print("quantità inserita: ");
        int quanti = Integer.parseInt(sc.nextLine());
	    

	    String tipologia = "Rivista";

        return new Rivista(nome, autore, 
                            edizione, periodicita, tipologia, quanti);
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

	public int getQuanti() {
		return quanti;
	}

	public void setQuanti(int quanti) {
		this.quanti = quanti;
	}

	
	
}
