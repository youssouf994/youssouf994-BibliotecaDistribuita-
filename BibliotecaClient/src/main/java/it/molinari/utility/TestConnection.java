package it.molinari.utility;

import java.io.*;
import java.net.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import it.molinari.model.Cd;
import it.molinari.model.Item;
import it.molinari.model.User;

import com.fasterxml.jackson.databind.SerializationFeature;

public class TestConnection {
	public static void main(String[] args) throws UnknownHostException, IOException {

        ActionType actionType;

            // === CLIENT SOCKET ===
            Socket client = new Socket("26.198.151.70", 1050);
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            List<Object> oggettoMittente = new ArrayList<Object>();

            // JACKSON
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            

            // === CREO UN ITEM DI ESEMPIO ===
            Item item = new Cd(
                    "Raggata the blanc",
                    "The Police",
                    false,
                    "utente1",
                    16,
                    120,
                    5,
                    "Cd",
                    LocalTime.now()
            );

            List<Item> prestiti = new ArrayList<>();
            //prestiti.add(item);

            // Creo user
            User user = new User(
                    0, "Mari", "Rossi", "youss125", "password123", prestiti, false);

            // Creo wrapper con token e action
            String token = "";
            actionType = ActionType.LOGIN_REQUEST;
            
            oggettoMittente.add(user);

            GeneratoreJson request = new GeneratoreJson();
            
            

            // Serializzo tutto in JSON su una sola linea
            String json = mapper.writeValueAsString(request.getOggettoRequest(token, actionType, oggettoMittente)).replace("\n", "").replace("\r", "");

            // === OUTPUT CLIENT ===
            System.out.println("=== CLIENT - JSON INVIATO ===");
            System.out.println(json);
            System.out.println();

            // Invio al server
            out.println(json);
            out.flush();

            // Attendi la risposta con timeout
            client.setSoTimeout(5000);
            
            String response = in.readLine();
            
            System.out.println("=== CLIENT - RISPOSTA RICEVUTA ===");
            if (response != null) {
                // Parse della risposta formato: CODICE\tMESSAGGIO\tTOKEN\tACTION\tDATI
                String[] parts = response.split("\t");
                
                System.out.println("Codice Stato: " + (parts.length > 0 ? parts[0] : "N/A"));
                System.out.println("Messaggio: " + (parts.length > 1 ? parts[1] : "N/A"));
                System.out.println("Token: " + (parts.length > 2 ? parts[2] : "N/A"));
                System.out.println("Action Type: " + (parts.length > 3 ? parts[3] : "N/A"));
                System.out.println("Dati: " + (parts.length > 4 ? parts[4] : "N/A"));
            } else {
                System.out.println("ERRORE: Nessuna risposta dal server!");
            }
            System.out.println();

            // Chiudi la connessione
            out.println("end)");
            out.flush();

            String finalResponse = in.readLine();
            System.out.println("=== CLIENT - CONFERMA CHIUSURA ===");
            if (finalResponse != null) {
                System.out.println(finalResponse);
            }

            client.close();
            
			}
    	}	