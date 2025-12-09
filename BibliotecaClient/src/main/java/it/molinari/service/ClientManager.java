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

            else if (req instanceof GetItemsRequest) {
                
            }

            else if (req instanceof PrestitoRequest prestitoReq) {
                if (loggedUser == null) return null;

                ItemPrestato ip = prestitoReq.getItem();
                listaData.add(ip);
            }

            else if (req instanceof RestituzioneRequest retReq) {
                if (loggedUser == null) return null;
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
                if (loggedUser == null || !loggedUser.isRuolo()) {
                    System.out.println("Solo admin");
                    return null;
                }
                listaData.add(loggedUser);
                listaData.add(addReq.getItem());
            }

            else if (req instanceof RimuoviItemRequest remReq) {
                if (loggedUser == null || !loggedUser.isRuolo()) {
                    System.out.println("Solo admin");
                    return null;
                }
                listaData.add(loggedUser);
                listaData.add(remReq.getItem());
            }

            GeneratoreJson wrapper = GeneratoreJson.getOggettoRequest(
                    req.getToken(),
                    req.getActionType(),
                    listaData
            );

            String json = mapper.writeValueAsString(wrapper)
                    .replace("\n", "")
                    .replace("\r", "");

			// ====== LOG INVIO ======
			System.out.println("\n---------------------------");
			System.out.println(" CLIENT → SERVER (INVIO) ");
			System.out.println("---------------------------");
			System.out.println("ActionType: " + req.getActionType());
			System.out.println("JSON inviato:");
			System.out.println(json);
			System.out.println("---------------------------\n");
			
			conn.send(json);
			
			String risposta = conn.receive();
			
			// ====== LOG RISPOSTA ======
			System.out.println("\n---------------------------");
			System.out.println(" SERVER → CLIENT (RICEVUTO) ");
			System.out.println("---------------------------");
			System.out.println("JSON ricevuto:");
			System.out.println(risposta);
			System.out.println("---------------------------\n");

            if (risposta == null) return null;

            return new Response(risposta);

        } catch (Exception e) {
            System.out.println("[LOG] Errore durante l'invio: " + e.getMessage());
            return null;
        }
    }

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
        if (resp == null) return null;

        try {
            List<List<Item>> outerList = mapper.readValue(
                resp.getDataJson(),
                mapper.getTypeFactory().constructCollectionType(
                    List.class,
                    mapper.getTypeFactory().constructCollectionType(List.class, Item.class)
                )
            );

            if (outerList.isEmpty()) return null;
            return outerList.get(0);

        } catch (Exception e) {
            return null;
        }
    }

    public List<ItemPrestato> getPrestitiUtente() {
        PrestitiUtenteRequest req = new PrestitiUtenteRequest(token, loggedUser.getUsername());
        Response resp = sendRequest(req);

        if (resp == null || !resp.isOk()) return null;

        try {
            List<ItemPrestato> lista = mapper.readValue(
                resp.getDataJson(),
                mapper.getTypeFactory().constructCollectionType(List.class, ItemPrestato.class)
            );
            return lista;

        } catch (Exception e) {
            return null;
        }
    }

    public List<Item> cerca(String valore, int modalita) {
        RicercaRequest req = new RicercaRequest(token, valore, modalita);
        Response resp = sendRequest(req);

        if (resp == null || !resp.isOk()) return null;

        try {
            List<Item> lista = mapper.readValue(
                resp.getDataJson(),
                mapper.getTypeFactory().constructCollectionType(List.class, Item.class)
            );
            return lista;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean effettuaPrestito(Item itemOriginale, int quantita) {

        if (loggedUser == null) return false;

        List<Object> lista = new ArrayList<>();

        // 1) USER
        User user = new User();
        user.setUsername(loggedUser.getUsername());
        user.setCognome(loggedUser.getCognome());
        user.setRuolo(loggedUser.isRuolo());
        lista.add(user);

        // 2) ITEM PRESTATO
        ItemPrestato prestato = new ItemPrestato();
        prestato.setTipologia("ItemPrestato");
        prestato.setId(itemOriginale.getId()); 
        prestato.setQuanti(quantita);
        prestato.setNome(user.getUsername());
        prestato.setCognome(user.getCognome());
        prestato.setInizioPrestito(LocalDate.now().toString());
        prestato.setItem(itemOriginale);

        lista.add(prestato);

        // Invio
        GeneratoreJson wrapper = GeneratoreJson.getOggettoRequest(
                token,
                ActionType.BORROW_ITEM_REQUEST,
                lista
        );

        try {
            String json = mapper.writeValueAsString(wrapper);
            conn.send(json);

            String risposta = conn.receive();
            Response resp = new Response(risposta);

            return resp.isOk();

        } catch (Exception e) {
            System.out.println("Errore invio prestito: " + e.getMessage());
            return false;
        }
    }


    public boolean restituisciItem(int idItem, int quantita) {

        ItemPrestato ip = new ItemPrestato();
        ip.setTipologia("ItemPrestato");
        ip.setNome(loggedUser.getUsername());
        ip.setId(idItem);
        ip.setQuanti(quantita);

        Item item = new Item();
        item.setId(idItem);
        item.setTipologia("Item");

        ip.setItem(item);

        RestituzioneRequest req = new RestituzioneRequest(token, ip);
        Response resp = sendRequest(req);

        return resp != null && resp.isOk();
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

    public boolean isAdmin() {
        return loggedUser != null && loggedUser.isRuolo();
    }

    public User getLoggedUser() { return loggedUser; }

    public void close() {
        try { conn.close(); }
        catch (IOException e) {}
    }
}
