package it.molinari.server.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


import it.molinari.server.enums.ActionType;



public class Request 
{
	protected String token;
	protected ActionType actionType;
	@JsonProperty("listaData") 
	protected List<Object> listaData;
	
	public Request()
	{
		
	}
	
	public String getToken() 
	{ 
    	return token; 
	}

	public ActionType getActionType() 
	{ 
    	return actionType; 
	}

	public List<Object> getDati() 
	{ 
    	return listaData; 
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	public void setDati(List<Object> dati) {
		this.listaData = dati;
	}
	
	

}
