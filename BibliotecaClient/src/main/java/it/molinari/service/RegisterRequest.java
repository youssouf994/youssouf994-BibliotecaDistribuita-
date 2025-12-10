package it.molinari.service;

import it.molinari.utility.ActionType;

public class RegisterRequest extends Request {

	private String nome;
	private String cognome;
	private String username;
	private String password;
	
	public RegisterRequest(String nome, String cognome, String username, String password) {
        super("", ActionType.REGISTRATION_REQUEST);
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
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
	
}
