package it.molinari.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import it.molinari.model.Item;
import it.molinari.model.ItemPrestato;
import it.molinari.model.Ricercato;
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
        conn = new ClientConnection("localhost", 1050);
    }

    private Response sendRequest(Request req) {
        try {
            if (!conn.isConnected()) {
                System.out.println("[LOG] ERRORE: Nessuna connessione al server.");
                return null;
            }

            List<Object> listaData = new ArrayList<>();

            // --- COSTRUZIONE PAYLOAD ---
            if (req instanceof LoginRequest loginReq) {
                User u = new User();
                u.setUsername(loginReq.getUsername());
                u.setPassword(loginReq.getPassword());
                listaData.add(u);
            } 
            else if (req instanceof RegisterRequest regReq) {
                User u = new User();
                u.setNome(regReq.getNome());
                u.setCognome(regReq.getCognome());
                u.setUsername(regReq.getUsername());
                u.setPassword(regReq.getPassword());
                u.setRuolo(false);
                listaData.add(u);
            } 
            else if (req instanceof PrestitoRequest prestitoReq) {
                // Il Server vuole: [User, ItemPrestato]
                if (loggedUser == null) return null;
                listaData.add(loggedUser); 
                listaData.add(prestitoReq.getItem());
            } 
            else if (req instanceof RestituzioneRequest retReq) {
                // Il Server vuole: [User, ItemPrestato]
                if (loggedUser == null) return null;
                listaData.add(loggedUser);
                listaData.add(retReq.getItem());
            } 
            else if (req instanceof PrestitiUtenteRequest loansReq) {
                User u = new User();
                u.setUsername(loansReq.getUsername());
                listaData.add(u);
            } 
            else if (req instanceof RicercaRequest searchReq) {
                Ricercato r = new Ricercato();
                r.setValore(searchReq.getValore());
                r.setModalita(searchReq.getModalita());
                listaData.add(r);
            } 
            else if (req instanceof AggiungiItemRequest addReq) {
                if (loggedUser == null || !loggedUser.isRuolo()) return null;
                listaData.add(loggedUser);
                listaData.add(addReq.getItem());
            } 
            else if (req instanceof RimuoviItemRequest remReq) {
                if (loggedUser == null || !loggedUser.isRuolo()) return null;
                listaData.add(loggedUser);
                listaData.add(remReq.getItem());
            }

            // --- INVIO E LOGGING ---
            GeneratoreJson wrapper = GeneratoreJson.getOggettoRequest(req.getToken(), req.getActionType(), listaData);
            String json = mapper.writeValueAsString(wrapper).replace("\n", "").replace("\r", "");

            // ORA VEDRAI QUESTO LOG ANCHE PER IL PRESTITO
            System.out.println("\n---------------------------");
            System.out.println(" CLIENT -> SERVER (INVIO) ");
            System.out.println("ActionType: " + req.getActionType());
            System.out.println("JSON inviato (anteprima): " + (json.length() > 100 ? json.substring(0, 100) + "..." : json));
            System.out.println("---------------------------\n");

            conn.send(json);
            String risposta = conn.receive();

            if (risposta == null) return null;
            return new Response(risposta);

        } catch (Exception e) {
            System.out.println("[LOG] Errore comunicazione: " + e.getMessage());
            return null;
        }
    }

    public boolean effettuaPrestito(Item itemOriginale, int quantita) {
        if (loggedUser == null) return false;
        
        ItemPrestato prestato = new ItemPrestato();
        prestato.setTipologia("ItemPrestato");
        
        // FIX IMPORTANTE: ID del prestito DEVE essere 0 (nuovo prestito), non l'ID del libro!
        prestato.setId(0); 
        
        prestato.setQuanti(quantita);
        prestato.setNome(loggedUser.getUsername());
        prestato.setCognome(loggedUser.getCognome());
        prestato.setInizioPrestito(LocalDate.now().toString());
        prestato.setItem(itemOriginale); // L'ID del libro è qui dentro

        // Ora usiamo sendRequest, quindi vedrai il log corretto
        PrestitoRequest req = new PrestitoRequest(token, prestato);
        Response resp = sendRequest(req);
        
        if(resp != null && !resp.isOk()) {
            System.out.println("Errore dal Server: " + resp.getMessage());
        }
        
        return resp != null && resp.isOk();
    }

    public boolean restituisciItem(int idItem, int quantita) {
        if (loggedUser == null) return false;
        
        ItemPrestato ip = new ItemPrestato();
        ip.setTipologia("ItemPrestato");
        ip.setNome(loggedUser.getUsername());
        ip.setId(idItem); // Qui serve l'ID del prestito o dell'item a seconda di come il server lo cerca
        ip.setQuanti(quantita);
        
        Item item = new Item();
        item.setId(idItem); 
        ip.setItem(item);

        RestituzioneRequest req = new RestituzioneRequest(token, ip);
        Response resp = sendRequest(req);
        
        if(resp != null && !resp.isOk()) {
            System.out.println("Errore dal Server: " + resp.getMessage());
        }

        return resp != null && resp.isOk();
    }
    
    // --- METODI STANDARD RIMASTI UGUALI ---

    public boolean login(String username, String password) {
        LoginRequest req = new LoginRequest(username, password, this.token);
        Response resp = sendRequest(req);
        if (resp == null || !resp.isOk()) return false;
        this.token = resp.getToken();
        try {
            if (resp.getDataJson() != null && !resp.getDataJson().isBlank()) {
                this.loggedUser = mapper.readValue(resp.getDataJson(), User.class);
            }
        } catch (Exception e) {}
        return true;
    }

    public boolean register(String nome, String cognome, String username, String password) {
        RegisterRequest req = new RegisterRequest(nome, cognome, username, password);
        Response resp = sendRequest(req);
        return resp != null && resp.isOk();
    }

    public List<Item> getItems() {
        Request req = new GetItemsRequest(token);
        Response resp = sendRequest(req);
        if (resp == null || !resp.isOk()) return null;
        try {
            return mapper.readValue(resp.getDataJson(), mapper.getTypeFactory().constructCollectionType(List.class, Item.class));
        } catch (Exception e) { return null; }
    }

    public List<ItemPrestato> getPrestitiUtente() {
        if (loggedUser == null) return null;
        PrestitiUtenteRequest req = new PrestitiUtenteRequest(token, loggedUser.getUsername());
        Response resp = sendRequest(req);
        if (resp == null || !resp.isOk()) return null;
        try {
            return mapper.readValue(resp.getDataJson(), mapper.getTypeFactory().constructCollectionType(List.class, ItemPrestato.class));
        } catch (Exception e) { return null; }
    }

    public List<Item> cerca(String valore, int modalita) {
        RicercaRequest req = new RicercaRequest(token, valore, modalita);
        Response resp = sendRequest(req);
        if (resp == null || !resp.isOk()) return null;
        try {
            String json = resp.getDataJson();
            if (json.trim().startsWith("[")) {
                return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, Item.class));
            } else {
                Item singolo = mapper.readValue(json, Item.class);
                List<Item> l = new ArrayList<>();
                l.add(singolo);
                return l;
            }
        } catch (Exception e) { return null; }
    }

    public boolean aggiungiItem(Item item) {
        AggiungiItemRequest req = new AggiungiItemRequest(token, item);
        Response resp = sendRequest(req);
        return resp != null && resp.isOk();
    }

    public boolean rimuoviItem(Item item) {
        RimuoviItemRequest req = new RimuoviItemRequest(token, item);
        Response resp = sendRequest(req);
        return resp != null && resp.isOk();
    }

    public boolean isAdmin() { return loggedUser != null && loggedUser.isRuolo(); }
    public User getLoggedUser() { return loggedUser; }
    public void close() { try { conn.close(); } catch (IOException e) {} }
}