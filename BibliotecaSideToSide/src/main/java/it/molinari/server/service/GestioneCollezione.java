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
	GestioneJson streamFile = new GestioneJson();
	
	public GestioneCollezione()
	{
		
	}
	
	
	public void aggiungiItem()
	{		
		listaCollezione=streamFile.leggiJson(0, Item.class);
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
		listaCollezione=streamFile.leggiJson(0, Item.class);
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
		listaCollezione=streamFile.leggiJson(0, Item.class);
		return listaCollezione;
	}
	
	public List<Item> getCollezioneLibri()
	{
		listaCollezione=streamFile.leggiJson(0, Item.class);
		List<Item> listaappoggio= new ArrayList<Item>();
		
		for(Item app : listaCollezione)
		{
			if(app.getTipologia().equalsIgnoreCase("libro"))
			{
				listaappoggio.add(app);
			}
		}
		return listaappoggio;
	}
	
	
	
	public void visualizzaCollezione()
	{
		listaCollezione=streamFile.leggiJson(0, Item.class);
		if(this.listaCollezione!=null)
		{
			for (Item e : this.listaCollezione)
			{
				System.out.print(e.toString());
			}
		}
	}
	
	


}
