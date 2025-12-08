package it.molinari.server.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.molinari.server.model.Item;
import it.molinari.server.model.ItemPrestato;
import it.molinari.server.response.Response;

public class GestorePrestiti extends Response
{
	protected Scanner cin = new Scanner(System.in);
	protected GestioneJson streamFile= new GestioneJson();
	protected List<Item> listaCollezione = new ArrayList<Item>();
	protected ItemPrestato itemPrestato= new ItemPrestato();
	protected List<ItemPrestato> listaPrestiti = new ArrayList<ItemPrestato>();
	protected ObjectMapper mapper = new ObjectMapper();
	
	public GestorePrestiti()
	{
		super();
	}
	
	public String segnalaRitardo()
	{
		this.listaPrestiti=streamFile.leggiJson(4, ItemPrestato.class);
		String dataAttuale = LocalDate.now().toString();
		
		try
		{
	        for (ItemPrestato e : this.listaPrestiti) 
	        {
	            if (e.getFinePrestito().equals(dataAttuale)) 
	            {
            		return mapper.writeValueAsString(e);
	            }	            
	        }
		}
		catch(JacksonException e)
		{
			return e.toString();
		}
		return "";
	}

	public boolean daiInPrestito(ItemPrestato itemPrestato)
	{
	    this.listaCollezione = streamFile.leggiJson(0, Item.class);
	    this.listaPrestiti   = streamFile.leggiJson(4, ItemPrestato.class);

	    if (this.listaCollezione.isEmpty())
	    {
	        return false;
	    }

	    for (Item e : this.listaCollezione)
	    {
	    	if (e.getId() == itemPrestato.getItem().getId() && e.getQuanti() >= itemPrestato.getQuanti())
	        {
	            itemPrestato.setInizioPrestito(LocalDate.now().toString());
	            itemPrestato.setFinePrestito(LocalDate.now().plusDays(15).toString());
	            
	            this.listaPrestiti.add(itemPrestato);// aggiungi alla lista prestiti

	            e.setQuanti(e.getQuanti() - itemPrestato.getQuanti());// aggiorna la quantità dell'item

	            // Riscrivi i file aggiornati
	            this.streamFile.scriviJson(0, this.listaCollezione);
	            this.streamFile.scriviJson(4, this.listaPrestiti);

	            return true;
	        }
	    	else
	    	{
	    		return false;
	    	}
	    }

	    return false; 
	}

	
	public boolean ritornaPrestito(ItemPrestato itemPrestato) {
	    this.listaCollezione = streamFile.leggiJson(0, Item.class);
	    this.listaPrestiti   = streamFile.leggiJson(4, ItemPrestato.class);

	    if (this.listaPrestiti.isEmpty()) {
	        return false;
	    }

	    // Trova il prestito effettivo
	    ItemPrestato prestitoDaRimuovere = null;
	    for (ItemPrestato ip : this.listaPrestiti) {
	        if (ip.getId() == itemPrestato.getId() &&
	            ip.getItem().getId() == itemPrestato.getItem().getId()) {
	            prestitoDaRimuovere = ip;
	            break;
	        }
	    }

	    if (prestitoDaRimuovere != null) {
	        // Aggiorna quantità nell'archivio
	        for (Item e : this.listaCollezione) {
	            if (e.getId() == itemPrestato.getItem().getId()) {
	                e.setQuanti(e.getQuanti() + prestitoDaRimuovere.getQuanti());
	                break;
	            }
	        }

	        // Rimuovi dalla lista prestiti
	        this.listaPrestiti.remove(prestitoDaRimuovere);

	        // Riscrivi i file aggiornati
	        this.streamFile.scriviJson(0, this.listaCollezione);
	        this.streamFile.scriviJson(4, this.listaPrestiti);

	        return true;
	    }

	    return false;
	}


	public List<Object> prestitiUtente(String username)
	{
	    List<Object> listaDati = new ArrayList<>();
	    this.listaPrestiti = streamFile.leggiJson(4, ItemPrestato.class);

	    if (this.listaPrestiti != null) {
	        for (ItemPrestato app : this.listaPrestiti) {
	            if (app.getNome() != null && app.getNome().equals(username)) {
	                listaDati.add(app);
	            }
	        }
	    }

	    return listaDati; // lista vuota se non ci sono prestiti
	}

}

