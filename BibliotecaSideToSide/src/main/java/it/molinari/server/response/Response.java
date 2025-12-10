package it.molinari.server.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.molinari.server.enums.ActionType;

public abstract class Response 
{
	protected int codice;
	protected String token, messaggio;
	protected ActionType actionType;
	
	
	public Response()
	{
		
	}
	
	public void setResponse(int codice, String messaggio, String token, ActionType action)
	{
		this.codice=codice;
		this.messaggio=messaggio;
		this.token=token;
		this.actionType=action;

	}
	

	
	public int getCodice() {
		return codice;
	}


	public void setCodice(int codice) {
		this.codice = codice;
	}


	public String getMessaggio() {
		return messaggio;
	}


	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}
	

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}
	
	
}
