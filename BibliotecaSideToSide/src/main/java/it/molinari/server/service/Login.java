package it.molinari.server.service;

import it.molinari.server.model.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.molinari.server.enums.ActionType;

public class Login 
{
	private String username, password, token;
	private ActionType actionType=ActionType.LOGIN_RESPONSE;
	private List<User> users = new ArrayList<User>();
	private GestioneJson IOJson = new GestioneJson();
	
	
	Login()
	{
		
	}
	
	Login(String token, ActionType actionType, User user)
	{
		this.token=token;
		this.actionType=actionType;
		this.username=user.getUsername();
		this.password=user.getPass();
	}
	
	public boolean provaLogin(User user)
	{
		users=IOJson.leggiJsonUser(1);
		boolean check= false;
		
		// Controllo username duplicato PRIMA di assegnare ID
	    for (User u : users) 
	    {
	        if (u.getUsername().equals(user.getUsername()) && u.getPass().equals(user.getPass()))// utente già registrato
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
	
}
