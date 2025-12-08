package it.molinari.server.app;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;


import it.molinari.server.service.GestioneConnessione;
import it.molinari.server.service.Payload;


public class App 
{
    public static void main( String[] args ) throws IOException
    {
        
    	GestioneConnessione socket = new GestioneConnessione();
    	Payload payload = new Payload();
        try
        {
        	while(true)
        	{
        		payload.payload();
        		break;
        	}
        }
        catch(JacksonException e)
        {
        	System.out.println(e);
        	socket.chiudiStreams();
        }
        
    } 
}
