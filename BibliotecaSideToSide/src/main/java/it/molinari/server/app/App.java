package it.molinari.server.app;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;


import it.molinari.server.service.GestioneConnessione;
import it.molinari.server.service.Payload;


public class App 
{
    public static void main( String[] args ) throws IOException
    {
        
    	GestioneConnessione socket = new GestioneConnessione(1050);
    	Payload payload = new Payload(socket);
        try
        {
        	while(true)
        	{
        		payload.payload();
        		break;
        	}
        }
        catch(Exception e)
        {
        	System.out.println(e);
        	socket.chiudiStreams();
        }
        
    } 
}
