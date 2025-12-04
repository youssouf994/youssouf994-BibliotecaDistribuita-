package it.molinari.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import it.molinari.model.Item;
import it.molinari.model.Libro;
import it.molinari.model.User;
import it.molinari.utility.ActionType;
import it.molinari.utility.GeneratoreJson;

public class ClientManager {

    private ClientConnection conn;
    private ObjectMapper mapper;

    private String token = "";
    private User loggedUser = null;

    public ClientManager() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        conn = new ClientConnection("127.0.0.1", 1050);
    }

    private String send(List<Object> dati, ActionType actionType) {
        try {
            GeneratoreJson gen = GeneratoreJson.getOggettoRequest(
                    token,         
                    actionType,    
                    dati           
            );

            String json = mapper.writeValueAsString(gen)
                                .replace("\n", "")
                                .replace("\r", "");

            
            System.out.println("CLIENT: " + json);
            conn.send(json);

            String response = conn.receive();
            System.out.println("SERVER: " + response);
            return response;

        } catch (IOException e) {
            System.out.println("[LOG]Errore I/O: " + e.getMessage());
            return null;
        }
    }

    // LOGIN
     public boolean login(String username, String password) {

    	 if (!conn.isConnected()) {
    		    System.out.println("[LOG]Impossibile connettersi al server.");
    		    return false;
    		}

        List<Object> dati = new ArrayList<>();

        User u = new User();
        u.setUsername(username);
        u.setPass(password);
        dati.add(u);

        String response = send(dati, ActionType.LOGIN_REQUEST);

        if (response == null)
            return false;

        String[] parts = response.split("\t");

        if (!parts[0].equals("200")) {
            System.out.println("Login fallito: " + parts[1]);
            return false;
        }

        this.token = parts[2];

        if (parts.length > 4 && !parts[4].isEmpty()) {
            try {
                this.loggedUser = mapper.readValue(parts[4], User.class);
            } catch (Exception e) {
                System.out.println("[LOG]Errore parsing utente: " + e.getMessage());
            }
        }

        return true;
    }

    // REGISTRAZIONE
    public boolean register(String nome, String cognome, String username, String password) {

    	if (!conn.isConnected()) {
		    System.out.println("[LOG]Impossibile connettersi al server.");
		    return false;
		}
    	
        List<Object> dati = new ArrayList<>();

        User nuovo = new User(
                0,
                nome,
                cognome,
                username,
                password,
                new ArrayList<Item>(),
                false
        );

        dati.add(nuovo);

        String response = send(dati, ActionType.REGISTRATION_REQUEST);

        if (response == null)
            return false;

        String[] parts = response.split("\t");

        if (!parts[0].equals("200")) {
            System.out.println("[LOG]Registrazione fallita: " + parts[1]);
            return false;
        }

        return true;
    }

    public String getToken() { return token; }

    public User getLoggedUser() { return loggedUser; }

    public boolean isAdmin() {
        return loggedUser != null && loggedUser.isRuolo();
    }

    public void close() {
        try {
            conn.close();
        } catch (IOException e) {
            System.out.println("[LOG]Errore chiusura connessione.");
        }
    }
}