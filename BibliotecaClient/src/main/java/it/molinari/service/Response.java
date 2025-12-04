package it.molinari.service;
import java.util.HashMap;

public abstract class Response {

	protected String actionType;
	protected HashMap<String, String> dati = new HashMap<>();
	
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	
}
