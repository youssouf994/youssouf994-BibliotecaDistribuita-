package it.molinari.server.service;

import it.molinari.server.model.*;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestioneCollezione 
{
	private Item item;
	Scanner cin = new Scanner(System.in);
	protected List<Item> listaCollezione = new ArrayList<>();
	protected int dimensioneCollezione=listaCollezione.size();
	
	public GestioneCollezione()
	{
		
	}
	
	
	public void aggiungiItem()
	{		
		if(this.listaCollezione != null)
		{
			System.out.println("Che tipo vuoi inserire? (1 = Libro, 2 = Rivista, 3 = Cd)");
			int scelta=Integer.parseInt(cin.nextLine());
			
			switch(scelta)
			{
				case 1: 
					item=Libro.compilaItem(cin, dimensioneCollezione);
					break;
					
				case 2: 
					item=Rivista.compilaItem(cin, dimensioneCollezione);
					break;
					
				case 3: 
					item=Cd.compilaItem(cin, dimensioneCollezione);
					break;
					
				default:
					System.out.println("Scelta errata");
					
			}
			
			this.listaCollezione.add(item);
			System.out.println("Hain inserito: ");
			System.out.println(item.toString());
		}
		
		
		this.dimensioneCollezione=listaCollezione.size();
	}
	
	public void cancellaItem(int codice)
	{
		int i;
		for (i=0;i<listaCollezione.size();i++)
		{
			if(listaCollezione.get(i).codice==codice)
			{
				this.listaCollezione.remove(i);
			}
		}
		
		this.dimensioneCollezione=listaCollezione.size();
	}
	
	public List<Item> getCollezione()
	{
		GestioneJson streamFile = new GestioneJson();
		listaCollezione=streamFile.leggiJson(0);
		return listaCollezione;
	}
	
	public void segnalaRitardo()
	{
		 LocalTime oraAttuale = LocalTime.now();

	        for (Item e : this.listaCollezione) 
	        {
	            if (e.isPrestato && e.orarioPrestito != null) 
	            {
	                long minutiTrascorsi = Duration.between(e.orarioPrestito, oraAttuale).toMinutes();

	                if (minutiTrascorsi > 10) 
	                {
	                    System.out.println("⚠️ ATTENZIONE: L'item \"" + e.toString() + "\" è in ritardo di " 
	                                       + minutiTrascorsi + " minuti.");
	                }
	            }
	        }
	}
	
	public void visualizzaCollezione()
	{
		if(this.listaCollezione!=null)
		{
			for (Item e : this.listaCollezione)
			{
				System.out.print(e.toString());
			}
		}
	}
	
	
	public void daiInPrestito()
	{
		if(this.dimensioneCollezione<1)
		{
			System.out.println("La collezione è vuota");
		}
		else
		{
			for( Item e: this.listaCollezione)//visualizzo la collezione per scegliere il codice daadare in prestito
			{
				if(e.isPrestato==false)
				{
					System.out.print(e.toString());
				}
			}
			
			System.out.println("Inserisci il codice dell'elemento da dare in prestito");
			int scelta=Integer.parseInt(cin.nextLine());
			
			for( Item e: this.listaCollezione)
			{
				if(e.codice==scelta)
				{
	                System.out.print("Inserisci il nome del richiedente: ");
	                String nomeRichiedente = cin.nextLine();
	
	                e.isPrestato = true;
	                e.richiedente = nomeRichiedente;
	                e.orarioPrestito = LocalTime.now();
	
	                System.out.println("✅ Prestito registrato con successo!");
	                System.out.println("Orario del prestito: " + e.orarioPrestito);
				}
			}
			 
		}
	}
	
	public void ritornaPrestito()
	{
		if(dimensioneCollezione<1)
		{
			System.out.println("La collezione è vuota");
		}
		else
		{
			for( Item e: this.listaCollezione)//visualizzo la collezione per scegliere il codice da ritornare dal prestito
			{
				if(e.isPrestato==true)
				{
					System.out.print(e.toString());
				}
			}
			
			System.out.println("Inserisci il codice dell'elemento da ritornare dal prestito");
			int scelta=Integer.parseInt(cin.nextLine());
			
			for( Item e: this.listaCollezione)
			{
				if(e.codice==scelta)
				{
	
	                e.isPrestato = false;
	                e.richiedente = "ritornato";
	                e.orarioPrestito = LocalTime.now();
	
	                System.out.println("✅ Ritornoo registrato con successo!");
	                System.out.println("Orario del Ritorno: " + e.orarioPrestito);
				}
			}
			 
		}
	}
	
	public void aggiungiItemDiTest() {
	    listaCollezione.add(new Libro(
	        "1984", "George Orwell", false, null,
	        "ISIN123", "Distopia", 328, listaCollezione.size(), "Libro", null));

	    listaCollezione.add(new Libro(
	        "Il nome della rosa", "Umberto Eco", false, null,
	        "ISIN456", "Giallo storico", 550, listaCollezione.size(), "Libro", null));

	    listaCollezione.add(new Rivista(
	        "National Geographic", "AA.VV.", false, null,
	        "Ottobre 2023", "Mensile", listaCollezione.size(), "Rivista", null));

	    listaCollezione.add(new Rivista(
	        "Focus", "AA.VV.", false, null,
	        "Settembre 2023", "Mensile", listaCollezione.size(), "Rivista", null));

	    listaCollezione.add(new Cd(
	        "Greatest Hits", "Queen", false, null,
	        17, 70, listaCollezione.size(), "Cd", null));

	    listaCollezione.add(new Cd(
	        "Abbey Road", "The Beatles", false, null,
	        16, 47, listaCollezione.size(), "Cd", null));

	    this.dimensioneCollezione = listaCollezione.size();

	    System.out.println("✅ 6 oggetti di test inseriti con successo (nessun prestito attivo).");
	}


}
