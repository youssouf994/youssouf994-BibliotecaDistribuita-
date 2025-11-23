package it.molinari.server.service;

import it.molinari.server.enums.*;
import it.molinari.server.model.*;

public class GeneratoreJson 
{
	private String token;
    private ActionType actionType;
    private Object data;

    public GeneratoreJson()
    {
    	
    }
    
    public GeneratoreJson(String token, ActionType actionType, User data) {
        this.token = token;
        this.actionType = actionType;
        this.data = data;
    }

    // getter e setter
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public ActionType getActionType() { return actionType; }
    public void setActionType(ActionType actionType) { this.actionType = actionType; }

    public Object getData() { return data; }
    public void setData(User data) { this.data = data; }
}
