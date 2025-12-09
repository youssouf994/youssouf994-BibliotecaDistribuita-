package it.molinari.service;
import it.molinari.model.Item;
import it.molinari.model.ItemPrestato;
import it.molinari.model.User;
import it.molinari.utility.ActionType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Request {
	
	@JsonIgnore
	protected String token;
	@JsonIgnore
	protected ActionType actionType;
	
	//get user_loans
	//serve l'oggetto user con username 
	//(ricordati "token, actiontype, lista dati)
	
	/*
	 * Add itemRequest
	 * prima user con ruolo = true e poi item
	 * tutti i dati di item apparte id.
	 * 
	 * 
	 * removeItem: 
	 * prima user con ruolo = true e poi item
	 * tutti i dati di item apparte id.
	 * 
	 * SearchItem:
	 * Mandare ItemRicercato con 2 parametri 
	 * (valore "tipologia/codice/nome" stringa, intero se è 1 cerco per titolo, 2 per tipologia, 3 per codice)
	 * 
	 */
	
	//ritorno prestiti, mandargli l'oggetto ItemPrestat con
	//set nome e set id;
	
	public List<Object> provaRichiestaPrestiti() {
		
		List<Object> dati = new ArrayList<Object>();
		
		User user = new User();
	    user.setUsername("test");
	    user.setRuolo(false);
	    
	    Item item = new Item();
        item.setNome("pincopallo");
        item.setAutore("ciao");
        item.setQuanti(4);
        item.setId(15);
        item.setTipologia("Libro");
        ItemPrestato itemPrestato= new ItemPrestato();
        itemPrestato.setNome(user.getUsername());
        itemPrestato.setCognome(user.getCognome());
        itemPrestato.setInizioPrestito(LocalDate.now().toString());
        itemPrestato.setId(1);
        itemPrestato.setTipologia("ItemPrestato");
        itemPrestato.setQuanti(1);
        itemPrestato.setItem(item);
		
        dati.add(user);
        dati.add(item);
        
		return dati;
	}
	
	public Request(String token, ActionType actionType) {
        this.token = token;
        this.actionType = actionType;
    }

    public String getToken() { 
    	return token; 
    	}
    public ActionType getActionType() { 
    	return actionType; 
    	}
}
