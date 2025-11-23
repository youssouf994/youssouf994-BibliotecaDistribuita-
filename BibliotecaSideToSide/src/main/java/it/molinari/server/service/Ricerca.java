package it.molinari.server.service;

import it.molinari.server.model.*;
import java.util.*;


public class Ricerca extends GestioneCollezione
{	
	private List<Item> listaCollezione = new ArrayList<>();
	
	public Ricerca(List<Item>lista)
	{
		if(lista.size()<1)
		{
			System.out.println("la collezione è vuota");
		}
		else
		{
			this.listaCollezione=lista;
		}
	}
	
	public void cerca(String valore, int modalita)
	{
		
		
		switch(modalita)
		{
			case 1:
				this.cercaTitolo(valore);
				break;
				
			case 2:
				this.cercaTipologia(valore);
				break;
				
			case 3:
				Integer valoreCast=Integer.parseInt(valore);
				this.cercaCodice(valoreCast);
				break;
				
			default:
				System.out.println("scelta errata");
		}
	}
	
	private void cercaTitolo(String valore)
	{		
		for (Item elemento: this.listaCollezione )
		{
			if(elemento.nome.equals(valore))
			{
				System.out.print(elemento.toString());
			}
		}
		
	}
	
	private void cercaTipologia(String valore)
	{	
		for (Item elemento: this.listaCollezione )
		{
			if(elemento.tipologia.equals(valore))
			{
				System.out.print(elemento.toString());
			}	
		}
		
	}
	
	private void cercaCodice(int valore)
	{
		
		for (Item elemento: this.listaCollezione )
		{
			if(elemento.codice==valore)
			{
				System.out.print(elemento.toString());
			}
		}
	}

}
