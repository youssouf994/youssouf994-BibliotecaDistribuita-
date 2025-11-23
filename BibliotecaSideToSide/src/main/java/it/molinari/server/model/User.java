package it.molinari.server.model;

import java.util.ArrayList;
import java.util.List;

public class User 
{
	private int id;
	private String nome, cognome, username, pass;
	private List<Item> listisInprestito = new ArrayList<>();
	private boolean ruolo;
	
	public User() 
	{
		
	}
	
	public User(int id, String nome, String cognome, String username, String password, List<Item> listIsPrestito, boolean ruolo)
	{
		this.id=id;
		this.nome=nome;
		this.cognome=cognome;
		this.username=username;
		this.pass=password;
		this.listisInprestito=listIsPrestito;
		this.ruolo=ruolo;
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

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public List<Item> getListisInprestito() {
		return listisInprestito;
	}

	public void setListisInprestito(List<Item> listisInprestito) {
		this.listisInprestito = listisInprestito;
	}

	public boolean isRuolo() {
		return ruolo;
	}

	public void setRuolo(boolean ruolo) {
		this.ruolo = ruolo;
	}

	public int getId() {
		return id;
	}
}
