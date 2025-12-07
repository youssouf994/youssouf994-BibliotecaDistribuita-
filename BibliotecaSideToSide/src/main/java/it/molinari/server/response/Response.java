package it.molinari.server.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.molinari.server.enums.ActionType;

public abstract class Response 
{
	protected int codice;
	protected String token, messaggio;
	protected ActionType actionType;
	@JsonProperty("listaData") 
	protected String json;
	
	public Response()
	{
		
	}
	
	public void setResponse(int codice, String messaggio, String token, ActionType action, String json)
	{
		this.codice=codice;
		this.messaggio=messaggio;
		this.token=token;
		this.actionType=action;
		this.json=json;
	}
	
	public String getResponse()
	{
		return String.valueOf(this.codice)+"\t"+this.messaggio+"\t"+this.token+"\t"+this.actionType+"\t"+this.json;
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

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	
	
	
}
