package it.molinari.server.tests;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import it.molinari.server.enums.ActionType;
import it.molinari.server.model.Item;
import it.molinari.server.model.ItemPrestato;
import it.molinari.server.model.Ricercato;
import it.molinari.server.model.User;
import it.molinari.server.service.GeneratoreJson;
import it.molinari.server.service.GestioneConnessione;
import it.molinari.server.service.Payload;

public class TestGestConnessione {

    public static void main(String[] args) throws IOException {
        try {
            // Avvio server in thread separato
            new Thread(() -> {
                try {
                    GestioneConnessione conn = new GestioneConnessione(1051);
                    Payload payload = new Payload(conn);
                    payload.payload();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            Thread.sleep(1000); // aspetta che il server sia pronto

            // === CLIENT ===
            Socket client = new Socket("localhost", 1050);
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            
            // Creo un utente valido per il login
            User user = new User();
            user.setUsername("teo");      // username che il server riconosce
            user.setPassword("1235"); 
            user.setCognome("sdf");
            user.setNome("teow");
            user.setRuolo(true);
            
            Item item = new Item();
            item.setNome("pincopallo");
            item.setAutore("ciao");
            item.setQuanti(4);
            item.setId(15);
            item.setTipologia("Libro");
            ItemPrestato itemPrestato= new ItemPrestato();
            itemPrestato.setNome(user.getUsername());
            itemPrestato.setCognome(user.getCognome());
            itemPrestato.setInizioPrestito(LocalDate.now().toString());
            itemPrestato.setId(1);
            itemPrestato.setTipologia("ItemPrestato");
            itemPrestato.setQuanti(1);
            itemPrestato.setItem(item);
            Ricercato ricercato = new Ricercato();
            ricercato.setValore("Libro");
            ricercato.setModalita(2);
            
            List<Object> lista = new ArrayList<>();
            //lista.add(user);
            //lista.add(item);
            lista.add(user);
            
            // Token vuoto per login iniziale
            String token = "Oukimcq4Nf";

            GeneratoreJson request = new GeneratoreJson();
            String json = mapper.writeValueAsString(request.getOggettoRequest(token, ActionType.LOGIN_REQUEST, lista));

            System.out.println("=== CLIENT - INVIO LOGIN_REQUEST ===");
            System.out.println(json);

            out.println(json);
            out.flush();

            // Ricevo la risposta
            String response = in.readLine();
            System.out.println("=== CLIENT - RISPOSTA ===");
            System.out.println(response);

            // Chiudo connessione
            out.println("end)");
            out.flush();

            client.close();

        } catch (Exception e) 
        {
        	System.out.println("ERRORE GRAVE nel client:");
        	new it.molinari.server.service.GestioneConnessione().chiudiStreams();
            //tokenizer.cancellaToken(request.getToken());
            e.printStackTrace();
            
           // e.printStackTrace();
        }
    }
}
