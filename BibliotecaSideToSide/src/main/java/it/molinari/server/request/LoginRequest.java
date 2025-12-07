package it.molinari.server.request;

import java.util.ArrayList;
import java.util.List;

import it.molinari.server.model.User;
import it.molinari.server.service.GestioneJson;

public class LoginRequest extends Request
{
	private GestioneJson IOJson = new GestioneJson();
	private List<User> users = new ArrayList<User>();
	private List<Object> listaData= new ArrayList<Object>();
	
	public LoginRequest()
	{
		super();
	}
	
	public boolean provaLogin(User user)//errore mettere una funzione nel dto
	{
		this.users=IOJson.leggiJson(1, User.class);
		boolean check= false;
		
		// Controllo username duplicato PRIMA di assegnare token
	    for (User u : this.users) 
	    {
	        if (u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword()))// utente già registrato
	        {       
	            check=true;
	            break;
	        }
	        else
        	{
	        	check=false;
        	}
	    }
	    
	    return check;
	}

	public GestioneJson getIOJson() {
		return IOJson;
	}

	public void setIOJson(GestioneJson iOJson) {
		IOJson = iOJson;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Object> getListaData() {
		return listaData;
	}

	public void setListaData(List<Object> listaData) {
		this.listaData = listaData;
	}
	
	
}
