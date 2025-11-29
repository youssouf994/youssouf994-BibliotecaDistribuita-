package it.molinari.server.service;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.molinari.server.enums.*;
import it.molinari.server.model.*;

public class GeneratoreJson 
{
    private String token, classe;
    private ActionType actionType;
    private List<Object> listaData;
    // lista di oggetti generici così posso mischiare user e dati

    public GeneratoreJson() { }

    public GeneratoreJson(String token, ActionType actionType, List<Object> data) 
    {
        this.token = token;
        this.actionType = actionType;
        this.listaData = data;
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.CLASS,      // salva il nome completo della classe
            include = JsonTypeInfo.As.PROPERTY,
            property = "@classe"               // il campo speciale scritto nel JSON
    )
    public static Object getOggettoRequest(String token, ActionType actionType, List<Object> data) {
        return new GeneratoreJson(token, actionType, data);
    }

    /*
     * Deserializza un JSON in lista di oggetti eterogenei
     */
    public static List<Object> setOggettiDaRequest(String json) 
    {
        try 
        {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerSubtypes(User.class, Cd.class, Item.class, Rivista.class, Libro.class, Token.class);
         
            mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            
            // deserializza il JSON in GeneratoreJson (polimorfismo attivo)
            GeneratoreJson generator = mapper.readValue(json, GeneratoreJson.class);
            
            // ritorna direttamente la listaData, con oggetti già del tipo corretto
            return generator.getListaData();
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public String listToString(List<Object> lista) throws IOException
    {
    	String json;
    	ObjectMapper mapper = new ObjectMapper();
    	mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    	json=mapper.writeValueAsString(lista);
    	return json;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        if (listaData != null) {
            for (Object obj : listaData) {
                if (obj != null) {
                    sb.append("    ").append(obj.toString());
                } else {
                    sb.append("    null,\n");
                }
            }
        }
        
        sb.append("n");
        return sb.toString();
    }

    
    // getter e setter
    public String getToken() {
        return token; 
    }

    public void setToken(String token) {
        this.token = token; 
    }

    public ActionType getActionType() {
        return this.actionType; 
    }
    
    

    public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public void setActionType(ActionType actionType) {
        this.actionType = actionType; 
    }

    public List<Object> getListaData() {
        return this.listaData; 
    }

    public void setListaData(List<Object> data) {
        this.listaData = data; 
    }
}
