package it.molinari.server.service;

import it.molinari.server.enums.*;
import it.molinari.server.model.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

public class GestioneJson 
{
	private String[] archivi={
		"src/main/java/it/molinari/server/files/archivio.json", 
		"src/main/java/it/molinari/server/files/users.json", 
		"src/main/java/it/molinari/server/files/whitelist.json", 
		"src/main/java/it/molinari/server/files/tokens.json"
	};
	private ObjectMapper mapper = new ObjectMapper();
	private File streamFile;

	public GestioneJson() 
	{
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);    
	}
	
	public void aggiornaJson(List<Item> nuoviItem, int indiceFile) {
	    try {
	        this.streamFile = new File(this.archivi[indiceFile]);

	        if (!this.streamFile.getParentFile().exists()) {
	            this.streamFile.getParentFile().mkdirs();
	        }

	        List<Item> listaEsistente = new ArrayList<>();
	        
	        // Se il file esiste e non è vuoto, leggi la lista corrente
	        if (this.streamFile.exists() && this.streamFile.length() > 0) {
	            listaEsistente = Arrays.asList(mapper.readValue(this.streamFile, Item[].class));
	            listaEsistente = new ArrayList<>(listaEsistente); // per renderla modificabile
	        }

	        // Aggiungi i nuovi item
	        listaEsistente.addAll(nuoviItem);

	        // Riscrivi tutto il file
	        mapper.writeValue(this.streamFile, listaEsistente);
	        System.out.println("aggiornaJson OK: " + this.streamFile.getAbsolutePath());
	    } catch (Exception e) {
	        System.out.println(HttpStatus.INTERNAL_ERROR.getCodice()+" "+HttpStatus.INTERNAL_ERROR.getMessaggio());
	        e.printStackTrace();
	    }
	}

	
	public void aggiornaJsonUser(List<User> listaUtenti, int indiceFile) {
	    try {    
	        this.streamFile = new File(this.archivi[indiceFile]);
	        
	        if (!this.streamFile.getParentFile().exists()) {
	            this.streamFile.getParentFile().mkdirs();
	        }

	        // Scrivi direttamente la lista ricevuta (lista già aggiornata da registra)
	        this.mapper.writeValue(this.streamFile, listaUtenti);
	        System.out.println("aggiornaJsonUser OK: " + this.streamFile.getAbsolutePath());
	    } catch(Exception e) {
	        System.out.println(HttpStatus.INTERNAL_ERROR.getCodice() + " " + HttpStatus.INTERNAL_ERROR.getMessaggio());
	        e.printStackTrace();
	    }
	}

	
	public List<Item> leggiJson(int indiceFile)
	{
		List<Item> lista= new ArrayList<Item>();
		
		try
		{
			this.streamFile = new File(this.archivi[indiceFile]);
			
			if(!this.streamFile.exists())
			{
				System.out.println("leggiJson: file vuoto o inesistente");
				return lista;
			}
			
			lista = this.mapper.readValue(this.streamFile, new TypeReference<List<Item>>() {});
			System.out.println("leggiJson OK: " + lista.size() + " elementi");
			return lista;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return lista;
		}
	}
	
	public List<User> leggiJsonUser(int indiceFile) {
	    List<User> lista = new ArrayList<>();
	    try {
	        this.streamFile = new File(this.archivi[indiceFile]);

	        if (!this.streamFile.exists() || this.streamFile.length() == 0) {
	            return lista;
	        }

	        lista = mapper.readValue(this.streamFile, new TypeReference<List<User>>() {});
	        System.out.println("leggiJsonUser OK: " + lista.size() + " utenti");
	        return lista;
	    } catch (com.fasterxml.jackson.databind.JsonMappingException e) {
	        System.out.println("Errore durante la lettura del JSON: ID duplicati negli item");
	        e.printStackTrace();
	        return lista; // puoi decidere se ritornare lista vuota o interrompere
	    } catch (Exception e) {
	        System.out.println(HttpStatus.INTERNAL_ERROR.getCodice() + " " + HttpStatus.INTERNAL_ERROR.getMessaggio());
	        e.printStackTrace();
	        return lista;
	    }
	}


	public List<Token> leggiJsonToken(int indiceFile)
	{
	    List<Token> lista = new ArrayList<Token>();
	    
	    try
	    {
	        this.streamFile = new File(this.archivi[indiceFile]);
	        
	        // Se il file non esiste o è vuoto, ritorna lista vuota
	        if(!this.streamFile.exists())
	        {
	            System.out.println("leggiJsonToken: file non esiste, creo nuovo file");
	            this.streamFile.getParentFile().mkdirs();
	            this.mapper.writeValue(this.streamFile, lista);
	            return lista;
	        }
	        
	        if(this.streamFile.length() == 0)
	        {
	            System.out.println("leggiJsonToken: file vuoto, inizializzo array");
	            this.mapper.writeValue(this.streamFile, lista);
	            return lista;
	        }
	        
	        lista = this.mapper.readValue(this.streamFile, new TypeReference<List<Token>>() {});
	        System.out.println("leggiJsonToken OK: " + lista.size() + " token");
	        return lista;
	    }
	    catch(Exception e)
	    {
	        System.out.println(HttpStatus.INTERNAL_ERROR.getCodice()+" "+HttpStatus.INTERNAL_ERROR.getMessaggio());
	        e.printStackTrace();
	        return lista;
	    }
	}
}