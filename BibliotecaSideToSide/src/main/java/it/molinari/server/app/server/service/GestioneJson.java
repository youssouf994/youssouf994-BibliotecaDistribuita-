package it.molinari.server.app.server.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import it.molinari.server.app.server.model.*;

import com.fasterxml.jackson.databind.SerializationFeature;

public class GestioneJson 
{
	private static final String PERCORSO_FILE="archivio.json";
	private ObjectMapper mapper = new ObjectMapper();
	private File streamFile;

	
	
	public GestioneJson()
	{
		this.streamFile = new File(PERCORSO_FILE);
		
		/*
		 	* di natura jackson e json non supportano il tipo local time usato nella classe item, quindi devo aggiungere il 
		 	* modulo javaTimeModule attraverso la funzione registerModule
		*/
        this.mapper.registerModule(new JavaTimeModule());
        
        /*
         	*senza disable jackson stamperebbe i secondi trascorsi dalla mezzanotte, invece così compila il json
         	*usando il formato leggibile dall'uomo	 
        */
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);        
	}
	
	
	public void aggiornaJson(List<Item> lista)
	{
		try
		{		
			//utililzzo il metodowriteValue invece che writeValueAsString che prende come parametro 
			//il riferimento al file oltre all'oggetto wda trasformare in json
			this.mapper.writeValue(this.streamFile, lista);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public List<Item> leggiJson()
	{
		List<Item> lista= new ArrayList<Item>();
		
		try
		{
			if(this.streamFile.length()==0)
			{
				return lista;
			}
			/*
			 	*siccome List è un tipo generico e java in fase di compilazione non specifica subito il tipo di un oggetto generico,
			 	*devo usare typerreference che va a coprire questa mancanza 
			 * */
			return this.mapper.readValue(this.streamFile, new TypeReference<List<Item>>() {});
		}
		catch(Exception e)
		{
			System.out.println(e);
			return lista;
		}
	}
}
