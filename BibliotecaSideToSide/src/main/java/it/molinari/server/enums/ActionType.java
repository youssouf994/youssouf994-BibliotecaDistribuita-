package it.molinari.server.enums;

public enum ActionType 
{
	LOGIN("LOGIN"),
	REGISTRAZIONE("REGISTRAZIONE");
	
	

	private final String azione;
	
	ActionType(String azione)
	{
		this.azione=azione;
	}
	
	public String getAzione() {
		return azione;
	}
	
	
	
}
