package it.molinari.server.tests;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import it.molinari.server.enums.ActionType;
import it.molinari.server.model.Item;
import it.molinari.server.model.ItemPrestato;
import it.molinari.server.model.User;
import it.molinari.server.service.GeneratoreJson;

public class TestGestConnessione {

    public static void main(String[] args) {
        try {
            // Avvio server in thread separato
            new Thread(() -> {
                try {
                    new it.molinari.server.service.GestioneConnessione().payload();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            Thread.sleep(1000); // aspetta che il server sia pronto

            // === CLIENT ===
            Socket client = new Socket("localhost", 1051);
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            // Creo un utente valido per il login
            User user = new User();
            user.setUsername("teowee");      // username che il server riconosce
            user.setPassword("1235"); 
            user.setCognome("sdf");
            user.setNome("sjbds");
            user.setRuolo(false);
            
            Item item = new Item();
            item.setNome("Il Signore degli Anelli");
          
            item.setId(1);
            item.setTipologia("Libro");
            ItemPrestato itemPrestato= new ItemPrestato();
            itemPrestato.setNome(user.getNome());
            itemPrestato.setCognome(user.getCognome());
            itemPrestato.setInizioPrestito(LocalDate.now().toString());
            itemPrestato.setId(1);
            itemPrestato.setTipologia("ItemPrestato");
            itemPrestato.setQuanti(4);
            itemPrestato.setItem(item);
            
            List<Object> lista = new ArrayList<>();
            //lista.add(user);
            lista.add(itemPrestato);

            // Token vuoto per login iniziale
            String token = "IWG3Ua7BGD";

            GeneratoreJson request = new GeneratoreJson();
            String json = mapper.writeValueAsString(request.getOggettoRequest(token, ActionType.BORROW_ITEM_REQUEST, lista));

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
