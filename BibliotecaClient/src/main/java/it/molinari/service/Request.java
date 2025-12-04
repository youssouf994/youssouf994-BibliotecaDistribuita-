package it.molinari.service;
import java.util.List;
import it.molinari.utility.ActionType;

public abstract class Request {

	protected String token;
	protected ActionType actionType;
	protected List<Object> dati;
	
	public Request(String token, ActionType actionType, List<Object> dati) {
        this.token = token;
        this.actionType = actionType;
        this.dati = dati;
    }

    public String getToken() { 
    	return token; 
    	}
    public ActionType getActionType() { 
    	return actionType; 
    	}
    public List<Object> getDati() { 
    	return dati; 
    	}
}
