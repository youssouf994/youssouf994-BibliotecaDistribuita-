package it.molinari.server.service;

import it.molinari.server.enums.*;
import it.molinari.server.token.Tokenizer;
import it.molinari.server.model.Token;
import it.molinari.server.model.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;




public class GestioneConnessione 
{
	private static final int PORT=1051;
	ServerSocket serverSocket;
	Socket clientSocket=null;//abbinamento per il client
	BufferedReader inputDaClient= null;
	PrintWriter cout=null;
	private String str;
	
	private ActionType actionType;
	private Login login = new Login();
	private GestioneJson IOJson = new GestioneJson();
	private ObjectMapper mapper = new ObjectMapper();
	private GeneratoreJson pacchettatore =new GeneratoreJson();
	private Tokenizer tokenizer = new Tokenizer();
	
	

	
	public GestioneConnessione() throws IOException//RICHIESTA CONNESSIONE
	{
		try
		{
			this.mapper.registerModule(new JavaTimeModule());
			this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			
			this.serverSocket = new ServerSocket(PORT);//server in ascolto 
			this.clientSocket= this.serverSocket.accept();
			System.out.println("Connessione con client: "+this.clientSocket);
			
			this.apriStreamDaClient();
			this.apriStreamPerClient();
		}
		catch(IOException e)
		{
			System.out.println(HttpStatus.BAD_REQUEST.getCodice()+" "+HttpStatus.BAD_REQUEST.getMessaggio());	
			System.out.println(e);
			this.chiudiStreams();
		}
	}
	
	private void apriStreamDaClient() throws IOException
	{
		InputStreamReader isr = new InputStreamReader(this.clientSocket.getInputStream());
		this.inputDaClient=new BufferedReader(isr);
	}
	
	private void apriStreamPerClient() throws IOException
	{
		OutputStreamWriter osw = new OutputStreamWriter(this.clientSocket.getOutputStream());
		BufferedWriter bw = new BufferedWriter(osw);
		this.cout= new PrintWriter(bw, true);
	}

	public void payload() throws IOException 
	{
	    try 
	    {
	        System.out.println("=== SERVER IN ASCOLTO ===");
	        while (true) 
	        {
	            System.out.println("In attesa di dati dal client...");
	            this.str = this.inputDaClient.readLine();
	            
	            // DEBUG - FONDAMENTALE
	            System.out.println("=== SERVER RICEVUTO ===");
	            System.out.println("Stringa ricevuta: [" + this.str + "]");
	            System.out.println("Lunghezza: " + (this.str != null ? this.str.length() : "null"));
	            System.out.println("======================");

	            // Controllo se il client ha chiuso la connessione
	            if (this.str == null) 
	            {
	                System.out.println("Client ha chiuso la connessione");
	                break;
	            }

	            // Comando di terminazione
	            if (this.str.equals("end)")) 
	            {
	                System.out.println("Comando di chiusura ricevuto");
	                this.cout.println(HttpStatus.OK.getCodice() + " " + HttpStatus.OK.getMessaggio());
	                break;
	            }

	            // Deserializza la richiesta JSON
	            try 
	            {
	                System.out.println("Tentativo di deserializzazione JSON...");
	                pacchettatore = mapper.readValue(this.str, GeneratoreJson.class);
	                System.out.println("JSON deserializzato con successo!");
	            } 
	            catch (Exception e) 
	            {
	                System.out.println("ERRORE deserializzazione: " + e.getMessage());
	                e.printStackTrace();
	                this.cout.println(HttpStatus.BAD_REQUEST.getCodice() + " JSON non valido");
	                this.cout.flush();
	                continue;
	            }

	            String serverResponse;

	            // Gestione azioni
	            System.out.println("ActionType ricevuto: " + pacchettatore.getActionType());
	            switch (pacchettatore.getActionType()) 
	            {
		            case LOGIN:
		                Token tk = tokenizer.getToken();
		                String username = ((User) pacchettatore.getData()).getUsername();
		                
		                // Formato: CODICE\tMESSAGGIO\tTOKEN\tACTION\tDATI
		                serverResponse = HttpStatus.OK.getCodice() + "\t" +
		                                HttpStatus.OK.getMessaggio() + "\t" +
		                                tk.getToken() + "\t" +
		                                "LOGIN" + "\t" +
		                                "User: " + username;
		                break;
	
		            case REGISTRAZIONE:
		            	User nuovoUtente = (User) pacchettatore.getData();
		                Token tkReg = tokenizer.getToken();
		                
		                Registrazione registrazione= new Registrazione();
		                
		                registrazione.registra(nuovoUtente.getId(), nuovoUtente.getNome(), nuovoUtente.getCognome(), nuovoUtente.getUsername(), nuovoUtente.getPass(), nuovoUtente.isRuolo());
		                serverResponse = HttpStatus.OK.getCodice() + "\t" +
		                                HttpStatus.OK.getMessaggio() + "\t" +
		                                tkReg.getToken() + "\t" +
		                                "REGISTRAZIONE" + "\t" +
		                                "Utente registrato";
		                break;
	
		            default:
		                serverResponse = HttpStatus.INTERNAL_ERROR.getCodice() + "\t" +
		                                HttpStatus.INTERNAL_ERROR.getMessaggio() + "\t" +
		                                "N/A" + "\t" +
		                                "UNKNOWN" + "\t" +
		                                "Azione non riconosciuta";
		                break;
	        }

		        // Invio risposta al client
		        this.cout.println(serverResponse);
		        this.cout.flush();
	        }
	    } 
	    catch (Exception e) 
	    {
	        System.out.println("ERRORE GRAVE nel payload:");
	        System.out.println(HttpStatus.BAD_REQUEST.getCodice() + " " + HttpStatus.BAD_REQUEST.getMessaggio());
	        e.printStackTrace();
	    } 
	    finally 
	    {
	        System.out.println("Chiusura streams...");
	        chiudiStreams();
	    }
	}
	



	private void chiudiStreams() throws IOException
	{
		this.cout.close();
		this.inputDaClient.close();
		this.clientSocket.close();
		this.serverSocket.close();
	}
}
