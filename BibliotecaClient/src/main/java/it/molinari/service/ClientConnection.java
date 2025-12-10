package it.molinari.service;
import java.io.*;
import java.net.*;


public class ClientConnection {

	private Socket socket = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	
	public ClientConnection(String host, int port) {
		
		System.out.println("\n[LOG]Connessione al server in corso...");
        
		
		try {
			// Creo una socket che tenta di collegarsi all'IP e porta del server
            socket = new Socket(host, port);
            
            // Stream per RICEVERE dati (dal server → client)
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
            // Stream per INVIARE dati (dal client → server)
            out = new PrintWriter(socket.getOutputStream(), true);
            
            System.out.println("[LOG]Connessione stabilita con il server!\n");

		} catch (Exception error) {
	        System.out.println("[LOG]Errore connessione: " + error.getMessage());
	        socket = null;
	        in = null;
	        out = null;
	    }
	}
	
	public boolean isConnected() {
	    return socket != null && out != null && in != null;
	}
	
	public void send(String json) {
		out.println(json);
		out.flush();
	}
	
	public String receive() throws IOException {
		return in.readLine();
	}
	
	public void close() throws IOException {
		socket.close();
	}
	
}
