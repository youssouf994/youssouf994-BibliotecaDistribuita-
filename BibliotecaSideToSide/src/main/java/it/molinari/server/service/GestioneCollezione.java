package it.molinari.server.service;

import it.molinari.server.model.*;
import it.molinari.server.response.Response;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestioneCollezione extends Response
{
	private Item item;
	Scanner cin = new Scanner(System.in);
	protected List<Item> listaCollezione = new ArrayList<>();
	protected int dimensioneCollezione=listaCollezione.size();
	GestioneJson streamFile = new GestioneJson();
	
	public GestioneCollezione()
	{
		super();
	}
	
	
	public boolean aggiungiItem(Item item)
	{		
	    listaCollezione = streamFile.leggiJson(0, Item.class);

	    // Controllo campi obbligatori
	    if (item.nome == null || item.nome.isEmpty()) return false;
	    if (item.autore == null || item.autore.isEmpty()) return false;
	    if (item.tipologia == null || item.tipologia.isEmpty()) return false;
	    if (item.id < 0) return false;
	    if (item.quanti < 0) return false;

	    boolean trovato = false;

	    // Controllo se l'item esiste già per ID
	    for (Item collezioneItem : listaCollezione) {
	        if (collezioneItem.getId() == item.getId()) {
	            // Incrementa solo il numero di copie
	            collezioneItem.setQuanti(collezioneItem.getQuanti() + item.getQuanti());
	            trovato = true;
	            break;
	        }
	    }

	    // Se non trovato, aggiungi nuovo item
	    if (!trovato) {
	        listaCollezione.add(item);
	    }

	    // Salva su JSON
	    streamFile.scriviJson(0, listaCollezione);
	    
	    return true;
	}

	
	public boolean cancellaItem(Item item)
	{
		listaCollezione=streamFile.leggiJson(0, Item.class);
		int i;
		for (i=0;i<listaCollezione.size();i++)
		{
			if(listaCollezione.get(i).getId()==item.getId())
			{
				if(listaCollezione.get(i).getQuanti()>=item.getQuanti())
				{
					this.listaCollezione.get(i).setQuanti(listaCollezione.get(i).getQuanti()-item.getQuanti());
				}
				else
				{
					return false;
				}
			}
		}
		
		streamFile.scriviJson(0, this.listaCollezione);
		
		return true;
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
