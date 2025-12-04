package it.molinari.service;

import java.util.ArrayList;
import it.molinari.model.User;
import it.molinari.utility.ActionType;

public class LoginRequest extends Request {

	public LoginRequest(String username, String password) {
		super("", ActionType.LOGIN_REQUEST, new ArrayList<>());
		
		User user = new User(
				0, //ID
				"", //nome
				"", //cognome
				username,
				password,
				new ArrayList<>(), //prestiti
				false); //admin o non
		
		this.dati.add(user);
	}
}


