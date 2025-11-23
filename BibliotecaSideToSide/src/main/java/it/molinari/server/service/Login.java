package it.molinari.server.service;

import it.molinari.server.model.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.molinari.server.enums.ActionType;

public class Login 
{
	private String username, password, token;
	private String actionType=ActionType.LOGIN.getAzione();
	private List<User> users = new ArrayList<User>();
	private ObjectMapper mapper = new ObjectMapper();
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
	
	public void provaLogin(String user, String pass)
	{
		users=IOJson.leggiJsonUser(1);
	}
	
}
