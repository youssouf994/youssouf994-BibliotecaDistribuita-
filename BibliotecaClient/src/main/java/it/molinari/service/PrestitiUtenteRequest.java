package it.molinari.service;

import it.molinari.utility.ActionType;

public class PrestitiUtenteRequest extends Request {

    private String username;

    public PrestitiUtenteRequest(String token, String username) {
        super(token, ActionType.GET_USER_LOANS_REQUEST);
        this.username = username;
    }

    public String getUsername() { return username; }
}
