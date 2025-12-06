package it.molinari.service;
import it.molinari.utility.ActionType;
import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Request {

	@JsonIgnore
	protected String token;
	@JsonIgnore
	protected ActionType actionType;
	
	public Request(String token, ActionType actionType) {
        this.token = token;
        this.actionType = actionType;
    }

    public String getToken() { 
    	return token; 
    	}
    public ActionType getActionType() { 
    	return actionType; 
    	}
}
