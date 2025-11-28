package it.molinari.server.app;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;


import it.molinari.server.service.GestioneConnessione;


public class App 
{
    public static void main( String[] args ) throws IOException
    {
        GestioneConnessione socket = new GestioneConnessione();
        
        try
        {
        	while(true)
        	{
        		socket.apriStreamDaClient();
        	}
        }
        
    } 
}
