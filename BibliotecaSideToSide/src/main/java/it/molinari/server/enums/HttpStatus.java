package it.molinari.server.httpStatus.enums;

/*
 * questo enum contiene le risposte del server
 */

public enum HttpStatus 
{
	OK(200, "Successo"),
	BAD_REQUEST(400, "Richiesta non valida"),
    NOT_FOUND(404, "Risorsa non trovata"),
    INTERNAL_ERROR(500, "Errore del server");
	
	private int codice;
	private String messaggio;
	
	HttpStatus(int codice, String messaggio)
	{
		this.codice=codice;
		this.messaggio=messaggio;
	}
	
	public int getCodice()
	{
		return this.codice;
	}
	
	public String getMessaggio()
	{
		return this.messaggio;
	}
}
