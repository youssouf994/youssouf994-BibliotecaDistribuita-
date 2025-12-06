package it.molinari.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import it.molinari.model.User;
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

        conn = new ClientConnection("26.198.151.70", 1050);
    }

    private Response sendRequest(Request req) {

        try {
            if (!conn.isConnected()) {
                System.out.println("[LOG] ERRORE: Nessuna connessione al server.");
                return null;
            }

            List<Object> listaData = new ArrayList<>();
            listaData.add(req);

            GeneratoreJson wrapper = GeneratoreJson.getOggettoRequest(
                    req.getToken(),
                    req.getActionType(),
                    listaData
            );

            String json = mapper.writeValueAsString(wrapper)
                                .replace("\n", "")
                                .replace("\r", "");

            System.out.println("[LOG]"+mapper.writeValueAsString(wrapper));

            conn.send(json);

            String risposta = conn.receive();

            if (risposta == null) {
                System.out.println("[LOG] Nessuna risposta dal server.");
                return null;
            }

            System.out.println("SERVER -> " + risposta);

            return new Response(risposta);

        } catch (Exception e) {
            System.out.println("[LOG] Errore durante l'invio: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    //Login
    public boolean login(String username, String password) {

        LoginRequest req = new LoginRequest(username, password, this.token);

        Response resp = sendRequest(req);

        if (resp == null || !resp.isOk()) {
            System.out.println("[LOG] Login fallito: " + (resp != null ? resp.getMessage() : ""));
            return false;
        }

        this.token = resp.getToken();

        try {
            if (resp.getDataJson() != null && !resp.getDataJson().isBlank()) {
                this.loggedUser = mapper.readValue(resp.getDataJson(), User.class);
            }
        } catch (Exception e) {
            System.out.println("[LOG] Errore parsing User: " + e.getMessage());
        }

        return true;
    }
    
    //Registrazione
    public boolean register(String nome, String cognome, String username, String password) {

        RegisterRequest req = new RegisterRequest(nome, cognome, username, password);

        Response resp = sendRequest(req);

        if (resp == null || !resp.isOk()) {
            System.out.println("[LOG] Registrazione fallita: " + (resp != null ? resp.getMessage() : ""));
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
            System.out.println("[LOG] Errore chiusura connessione: " + e.getMessage());
        }
    }
}