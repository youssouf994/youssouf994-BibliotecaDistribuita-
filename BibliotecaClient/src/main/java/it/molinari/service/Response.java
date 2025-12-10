package it.molinari.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.molinari.utility.ActionType;

public class Response {

    private String code;
    private String message;
    private String token;
    private ActionType actionType;
    // Questo campo conterrà il JSON grezzo dei dati, estratto dal contenitore variabile del server
    private String dataJson; 

    public Response(String jsonRaw) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(jsonRaw);

            // 1. Parsing dei campi Header del protocollo
            if (root.has("codice")) this.code = root.get("codice").asText();
            if (root.has("messaggio")) this.message = root.get("messaggio").asText();
            if (root.has("token")) this.token = root.get("token").asText();
            if (root.has("actionType")) {
                try {
                    this.actionType = ActionType.valueOf(root.get("actionType").asText());
                } catch (Exception e) {
                    this.actionType = ActionType.ERROR_RESPONSE;
                }
            }

            if (root.has("listaData")) {
                this.dataJson = root.get("listaData").toString();
            } 
            else if (root.has("User")) {
                JsonNode userNode = root.get("User");
                
                if (userNode.has("collezione")) {
                    this.dataJson = userNode.get("collezione").toString();
                } else {
                    
                	this.dataJson = userNode.toString();
                }
            } 
            else if (root.has("Item")) {
                this.dataJson = root.get("Item").toString();
            } 
            else if (root.has("Registrazione")) {
                this.dataJson = root.get("Registrazione").toString();
            } 
            else {
                this.dataJson = null; // Nessun dato presente
            }

        } catch (Exception e) {
            System.out.println("[CLIENT ERROR] Errore parsing risposta JSON: " + e.getMessage());
            this.code = "500";
            this.message = "Errore parsing client";
        }
    }

    public boolean isOk() { return "200".equals(code); }
    public String getCode() { return code; }
    public String getMessage() { return message; }
    public String getToken() { return token; }
    public ActionType getActionType() { return actionType; }
    public String getDataJson() { return dataJson; }
}


