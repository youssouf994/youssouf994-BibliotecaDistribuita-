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
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;




public class GestioneConnessione 
{
	private static final int PORT=1050;
	ServerSocket serverSocket;
	Socket clientSocket=null;//abbinamento per il client
	BufferedReader inputDaClient= null;
	PrintWriter cout=null;
	private String str;
	String serverResponse;
	String userJson;
	private ObjectMapper mapper = new ObjectMapper();
	private GeneratoreJson converter =new GeneratoreJson();
	private Tokenizer tokenizer = new Tokenizer();
	List<Object> lista = new ArrayList<Object>();
	ActionType actionType;
	GestioneCollezione engine = new GestioneCollezione();
	

	
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
	            
	            // DEBUG
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
	                converter = mapper.readValue(this.str, GeneratoreJson.class);
	                this.lista = converter.getListaData() ;
	                System.out.println("JSON deserializzato con successo!\n"+converter.getListaData());
	            } 
	            catch (Exception e) 
	            {
	                System.out.println("ERRORE deserializzazione: " + e.getMessage());
	                e.printStackTrace();
	                this.cout.println(HttpStatus.BAD_REQUEST.getCodice() + " JSON non valido");
	                this.cout.flush();
	                continue;
	            }

	            

	            // Gestione azioni
	            System.out.println("ActionType ricevuto: " + converter.getActionType());
	            switch (converter.getActionType()) 
	            {
		            case LOGIN_REQUEST:
		            	Login login = new Login();
		            	userJson = mapper.writeValueAsString(lista.get(0));
		                User utente = mapper.readValue(userJson, User.class);
		                
		                if(login.provaLogin(utente)==true)
		                {
		                	converter.setToken(tokenizer.getToken());
		                	converter.setActionType(actionType.LOGIN_RESPONSE);
		                	serverResponse = HttpStatus.OK.getCodice() + "\t" +HttpStatus.OK.getMessaggio() + "\t" +converter.getToken() + "\t" + converter.getActionType()+"\t "+userJson;
		                }
		                else
		                {
		                	converter.setActionType(actionType.LOGIN_RESPONSE);
		                	serverResponse = HttpStatus.NOT_FOUND.getCodice() + "\t" +HttpStatus.NOT_FOUND.getMessaggio() + "\t" +converter.getToken() + "\t" + converter.getActionType()+"\t "+userJson;
		                }
		                break;
	
		            case REGISTRATION_REQUEST:
		            	
		            	
		            	converter.setToken(tokenizer.getToken());
		            	userJson = mapper.writeValueAsString(lista.get(0));//da cancellare?
		                User nuovoUtente = mapper.readValue(userJson, User.class);

		                Registrazione registrazione= new Registrazione();
		                userJson = mapper.writeValueAsString(nuovoUtente);
		                
		                if(registrazione.registra(nuovoUtente)==true)
		                {
		                	converter.setActionType(actionType.REGISTRATION_RESPONSE);
		                	serverResponse = HttpStatus.OK.getCodice() + "\t" +HttpStatus.OK.getMessaggio() + "\t" +converter.getToken() + "\t" + converter.getActionType()+"\t "+userJson;
		                }
		                else
		                {
		                	converter.setActionType(actionType.REGISTRATION_RESPONSE);
		                	serverResponse = HttpStatus.BAD_REQUEST.getCodice() + "\t" +HttpStatus.BAD_REQUEST.getMessaggio() + "\t" +converter.getToken() + "\t" + converter.getActionType();
		                }
		                break;
		                
		            case GET_BOOKS_REQUEST:
		            	if(tokenizer.isInSession(converter.getToken())==true)
		            	{
			            	lista.removeAll(lista);
			            	lista.add(engine.getCollezione());
			            	userJson=converter.listToString(lista);
			            	
			            	if(userJson!=null)
			            	{
			            		converter.setActionType(actionType.GET_BOOKS_RESPONSE);
			            		serverResponse= HttpStatus.OK.getCodice() + "\t" +HttpStatus.OK.getMessaggio() + "\t" +converter.getToken()+ "\t" + converter.getActionType()+"\t "+userJson;
			            	}
			            	else
			            	{
			            		converter.setActionType(actionType.GET_BOOKS_RESPONSE);
			                	serverResponse = HttpStatus.NOT_FOUND.getCodice() + "\t" +HttpStatus.NOT_FOUND.getMessaggio() + "\t" +converter.getToken()+ "\t" + converter.getActionType()+"\t "+userJson;
			            	}
		            	}
		            	else
		            	{
		            		converter.setActionType(actionType.GET_BOOKS_RESPONSE);
		                	serverResponse = HttpStatus.BAD_REQUEST.getCodice() + "\t" +HttpStatus.BAD_REQUEST.getMessaggio() + "\t" +converter.getToken() + "\t" + converter.getActionType()+"\t "+userJson;
		            	}
		            	break;
	
		            default:
		            	serverResponse = HttpStatus.INTERNAL_ERROR.getCodice() + "\t" +HttpStatus.INTERNAL_ERROR.getMessaggio()  +  converter.getActionType();
		                break;
	        }

		        // Invio risposta al client
		        this.cout.println(serverResponse);
		        this.cout.flush();
	        }
	    } 
	    catch (IOException e) 
	    {
	        System.out.println("ERRORE GRAVE nel payload:");
	        System.out.println(HttpStatus.BAD_REQUEST.getCodice() + " " + HttpStatus.BAD_REQUEST.getMessaggio());
	        e.printStackTrace();
	    } 
	    finally 
	    {
	    	lista.removeAll(lista);
	        System.out.println("Chiusura streams...");
	        //chiudiStreams();
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