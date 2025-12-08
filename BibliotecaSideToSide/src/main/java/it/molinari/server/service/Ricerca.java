package it.molinari.server.service;

import it.molinari.server.model.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;


public class Ricerca extends GestioneCollezione
{	
	private List<Item> listaCollezione = new ArrayList<Item>();
	private List<Object> appoggio = new ArrayList<Object>();
	private GestioneJson stream = new GestioneJson();
	private String dati;
	private GeneratoreJson converter = new GeneratoreJson();
	
	public Ricerca()
	{
		
	}
	
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
	
	public String cerca(String valore, int modalita) throws IOException
	{		
		switch(modalita)
		{
			case 1:
				this.dati=this.cercaTitolo(valore);
				break;
				
			case 2:
				this.dati=this.cercaTipologia(valore);
				break;
				
			case 3:
				Integer valoreCast=Integer.parseInt(valore);
				this.dati=this.cercaCodice(valoreCast);
				break;
				
			default:
				return ("codice modalià errato");
		}
		
		return this.dati;
	}
	
	private String cercaTitolo(String valore) throws IOException
	{		
		this.listaCollezione=stream.leggiJson(0, Item.class);
		
		
		for (Item elemento: this.listaCollezione )
		{
			if(elemento.getNome().equalsIgnoreCase(valore))
			{
				return elemento.toString();
			}
		}
		
		return "elemento non trovato";
	}
	
	
	private String cercaTipologia(String valore) throws IOException
	{	
		this.listaCollezione=stream.leggiJson(0, Item.class);
				
		for (Item elemento: this.listaCollezione )
		{
			if(elemento.getTipologia().equalsIgnoreCase(valore))
			{
				this.appoggio.add(elemento);
				
			}
		}
		
		if(appoggio.isEmpty())
		{
			return "elemento non trovato";
	
		}
		else
		{
			return converter.listToString(appoggio);
		}
		
	}
	
	private String cercaCodice(int valore)
	{
		
		this.listaCollezione=stream.leggiJson(0, Item.class);
				
				for (Item elemento: this.listaCollezione )
				{
					if(elemento.getId()==valore)
					{
						return elemento.toString();
					}
				}
				
				return "elemento non trovato";
	}

}
