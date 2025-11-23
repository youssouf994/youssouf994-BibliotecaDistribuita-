package it.molinari.server.service;

import it.molinari.server.enums.ActionType;
import it.molinari.server.enums.HttpStatus;
import it.molinari.server.model.User;
import it.molinari.server.model.*;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;



public class Registrazione 
{
	private String nome, cognome, username, password;
	private int id;
	private boolean ruolo;
	private String actionType=ActionType.REGISTRAZIONE.getAzione();
	private List<User> users = new ArrayList<User>();
	private ObjectMapper mapper = new ObjectMapper();
	private GestioneJson IOJson = new GestioneJson();
	private List<Item> items = new ArrayList<Item>();
	private String response;
	
	public Registrazione()
	{
		
	}
	
	public Registrazione(int id, String nome, String cognome, String username, String password, boolean ruolo)
	{
		this.id=id;
		this.nome=nome;
		this.cognome=cognome;
		this.username=username;
		this.password=password;
		this.ruolo=ruolo;
	}
	
	public void registra(int id, String nome, String cognome, String username, String password, boolean ruolo)
	{
		users=IOJson.leggiJsonUser(1);
		
		for (User user : users)
		{
			if(user.getCognome()==cognome || users ==null)
			{
				break;
			}
			else
			{
				this.id=id;
				this.nome=nome;
				this.cognome=cognome;
				this.username=username;
				this.password=password;
				this.ruolo=ruolo;
			}
		}
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRuolo() {
		return ruolo;
	}

	public void setRuolo(boolean ruolo) {
		this.ruolo = ruolo;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	
}
