package it.molinari.service;

import it.molinari.utility.ActionType;

public class LoginRequest extends Request {
	
	private String username;
	private String password;

	public LoginRequest(String username, String password, String token) {
        super(token, ActionType.LOGIN_REQUEST);
        this.username = username;
        this.password = password;
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


