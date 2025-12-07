package it.molinari.server.service;

import it.molinari.server.enums.*;
import it.molinari.server.token.Tokenizer;
import it.molinari.server.model.ItemPrestato;
import it.molinari.server.model.User;
import it.molinari.server.request.LoginRequest;
import it.molinari.server.request.RegistrationRequest;
import it.molinari.server.request.Request;
import it.molinari.server.response.GetBookRes;
import it.molinari.server.response.LoginResponse;
import it.molinari.server.response.RegistrationResponse;
import it.molinari.server.response.Response;

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
import java.util.Map;

import com.fasterxml.jackson.core.JacksonException;
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
	String serverResponse;
	String userJson;
	private ObjectMapper mapper = new ObjectMapper();
	private GeneratoreJson converter =new GeneratoreJson();
	private Tokenizer tokenizer = new Tokenizer();
	List<Object> lista = new ArrayList<Object>();
	ActionType actionType;
	GestioneCollezione engine = new GestioneCollezione();
	Request request;
	Response response;

	
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

	public void payload() throws IOException {
	    System.out.println("=== SERVER IN ASCOLTO ===");
	    
	    while (true) {
	        try {
	            System.out.println("In attesa di dati dal client...");
	            this.str = this.inputDaClient.readLine();

	            // Controllo chiusura connessione
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

	            // DEBUG
	            System.out.println("=== SERVER RICEVUTO ===");
	            System.out.println("Stringa ricevuta: " + this.str);
	            System.out.println("Lunghezza: " + this.str.length());
	            System.out.println("======================");

	            // Deserializza JSON
	            try 
	            {
	                System.out.println("Tentativo di deserializzazione JSON...");
	                request = mapper.readValue(this.str, Request.class);
	                this.lista = request.getDati();
	                this.actionType = request.getActionType();
	                this.tokenizer.setToken(request.getToken());
	      
	                System.out.println("JSON deserializzato con successo!\n" + request.getDati());
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
	            System.out.println("ActionType ricevuto: " + request.getActionType());
	            switch (request.getActionType()) 
	            {
	                case LOGIN_REQUEST:
	                	LoginResponse loginResponse = new LoginResponse();
	                	LoginRequest login = new LoginRequest();
	                	
	                    if (lista == null || lista.isEmpty()) 
	                    {   	
	                    	loginResponse.setCodice(HttpStatus.BAD_REQUEST.getCodice());
	                    	loginResponse.setMessaggio(HttpStatus.BAD_REQUEST.getMessaggio());
	                    	loginResponse.setJson("Login non autorizzato");
	                        //serverResponse = HttpStatus.BAD_REQUEST.getCodice() + " Lista dati vuota";
	                    } 
	                    else 
	                    {
	                        User utente = mapper.convertValue(lista.get(0), User.class);
	                        userJson = mapper.writeValueAsString(utente);
      
	                        if (login.provaLogin(utente)) 
	                        {
	                            request.setToken(tokenizer.getToken());
	                            request.setActionType(ActionType.LOGIN_RESPONSE);
	                            loginResponse.setJson("Benvenuto "+utente.getUsername());//se mi manda anche nome e cognome li uso per dare il benvenuto
	                            loginResponse.setResponse(HttpStatus.OK.getCodice(), HttpStatus.OK.getMessaggio(), request.getToken(), request.getActionType(), loginResponse.getJson());
	                            //serverResponse = HttpStatus.OK.getCodice()+"\t"+HttpStatus.OK.getMessaggio() + "\t" + token + "\t" + request.getActionType() + "\t" + userJson;
	                        } 
	                        else 
	                        {
	                        	request.setActionType(ActionType.LOGIN_RESPONSE);
	                        	request.setToken("");
	                        	loginResponse.setResponse(HttpStatus.NOT_FOUND.getCodice(), HttpStatus.NOT_FOUND.getMessaggio(), request.getToken(), request.getActionType(), "username o password errati");
	                            //serverResponse = HttpStatus.NOT_FOUND.getCodice() + "\t" + HttpStatus.NOT_FOUND.getMessaggio() + "\t" + request.getToken() + "\t" + request.getActionType() + "\t" + userJson;
	                        }
	                    }
	                    cout.println(loginResponse.getResponse());
	                    cout.flush();
	                    break;

	                case REGISTRATION_REQUEST:
	                    User nuovoUtente = mapper.convertValue(lista.get(0), User.class);
	                    Registrazione registrazione = new Registrazione();                  
	                    //userJson = mapper.writeValueAsString(nuovoUtente);
	                    RegistrationResponse response = new RegistrationResponse();
	                    response.setActionType(ActionType.REGISTRATION_REQUEST);
	                    
	                    if (registrazione.registra(nuovoUtente)) 
	                    {       	
	                    	userJson="Nuovo utente registrato "+"Nome: "+nuovoUtente.getNome()+" Cognome: "+nuovoUtente.getCognome()+" Username: "+nuovoUtente.getUsername();
	                    	response.setResponse(HttpStatus.OK.getCodice(), HttpStatus.OK.getMessaggio(), "", response.getActionType(), userJson);
	                        //serverResponse = HttpStatus.OK.getCodice() + "\t" + HttpStatus.OK.getMessaggio() + "\t" + request.getToken() + "\t" + request.getActionType() + "\t" + userJson;
	                    } 
	                    else 
	                    {
	                    	response.setResponse(HttpStatus.BAD_REQUEST.getCodice(), HttpStatus.BAD_REQUEST.getMessaggio(), "", response.getActionType(), "Questo utente è già registrato");
	                        //serverResponse = HttpStatus.BAD_REQUEST.getCodice() + "\t" + HttpStatus.BAD_REQUEST.getMessaggio() + "\t" + request.getToken() + "\t" + request.getActionType();
	                    }
	                    cout.println(response.getResponse());
	                    cout.flush();
	                    break;

	                case GET_BOOKS_REQUEST:
	                	GetBookRes responseBook = new GetBookRes();
	                	responseBook.setActionType(ActionType.GET_BOOKS_RESPONSE);
	                	
	                    if (tokenizer.isInSession(request.getToken())) 
	                    {
	                    	responseBook.setToken(request.getToken());
	                        lista.add(engine.getCollezioneLibri());
	                        userJson = converter.listToString(lista);

	                        if(userJson!=null)
	                        {
                        		responseBook.setResponse(HttpStatus.OK.getCodice(),HttpStatus.OK.getMessaggio(), responseBook.getToken(), responseBook.getActionType(), userJson);
	                        }
	                        else
	                        {
	                        	responseBook.setResponse(HttpStatus.NOT_FOUND.getCodice(), HttpStatus.NOT_FOUND.getMessaggio(), responseBook.getToken(), responseBook.getActionType(), "Non sono presenti articoli nella collezione");
	                        }	             
	                    } 
	                    else 
	                    {
	                        responseBook.setActionType(ActionType.GET_BOOKS_RESPONSE);
	                        responseBook.setResponse(HttpStatus.NOT_FOUND.getCodice(), HttpStatus.NOT_FOUND.getMessaggio(), responseBook.getToken(), responseBook.getActionType(), "Accesso al sistema non consentito verifica validità sessione");
	                    }
	                    cout.println(responseBook.getResponse());
	                    cout.flush();
	                    break;
	                    
	                case BORROW_ITEM_REQUEST:
	                	if (tokenizer.isInSession(request.getToken()))
	                	{
	                		GestorePrestiti gestorePrestiti = new GestorePrestiti();
	                		ItemPrestato item = new ItemPrestato();
	                		
	                		Object appoggio= lista.stream().filter(o ->
	                		{
	                			if(o instanceof Map m)
	                			{
	                				return "ItemPrestato".equals(m.get("tipologia"));
                				}
	                			else
	                			{
	                				return false;
	                			}
	                		}).findFirst().orElse(null);
	                		
	                		item=mapper.convertValue(appoggio, ItemPrestato.class);
	                		
	                		gestorePrestiti.setActionType(ActionType.BORROW_ITEM_RESPONSE);
	                		
	                		if(gestorePrestiti.daiInPrestito(item))
	                		{
	                			userJson="libro: "+item.getItem().getNome()+" Autore: "+item.getItem().getAutore()+" preso in prestito fino al "+item.getFinePrestito();
	                			gestorePrestiti.setResponse(HttpStatus.OK.getCodice(),HttpStatus.OK.getMessaggio(), request.getToken(), gestorePrestiti.getActionType(), userJson);
	                		}
	                		else
	                		{
	                			userJson="Il libro selezionato non è disponibile o inesistente";
	                			gestorePrestiti.setResponse(HttpStatus.NOT_FOUND.getCodice(),HttpStatus.NOT_FOUND.getMessaggio(), gestorePrestiti.getToken(), gestorePrestiti.getActionType(), userJson);
	                		}
	                		cout.println(gestorePrestiti.getResponse());
		                    cout.flush();
		                    break;
	                	}
	                	break;
	                	
	                case RETURN_ITEM_REQUEST:
	                    if (tokenizer.isInSession(request.getToken())) {
	                        GestorePrestiti gestorePrestiti = new GestorePrestiti();

	                        // Trova l'oggetto ItemPrestato nella lista
	                        Object appoggio = lista.stream()
	                            .filter(o -> o instanceof Map m && "ItemPrestato".equals(m.get("tipologia")))
	                            .findFirst()
	                            .orElse(null);

	                        if (appoggio != null) {
	                            ItemPrestato item = mapper.convertValue(appoggio, ItemPrestato.class);

	                            gestorePrestiti.setActionType(ActionType.RETURN_ITEM_RESPONSE);

	                            boolean ok = gestorePrestiti.ritornaPrestito(item);

	                            String userJson;
	                            int status;
	                            String messaggio;

	                            if (ok) {
	                                userJson = "Libro: " + item.getItem().getNome() + 
	                                           " Autore: " + item.getItem().getAutore() + 
	                                           " ritornato in biblioteca";
	                                status = HttpStatus.OK.getCodice();
	                                messaggio = HttpStatus.OK.getMessaggio();
	                            } else {
	                                userJson = "ID articolo non presente nel sistema";
	                                status = HttpStatus.NOT_FOUND.getCodice();
	                                messaggio = HttpStatus.NOT_FOUND.getMessaggio();
	                            }

	                            // Imposta la risposta correttamente
	                            gestorePrestiti.setResponse(status, messaggio, request.getToken(), 
	                                                        gestorePrestiti.getActionType(), userJson);

	                            // Risposta immediata al client
	                            cout.println(gestorePrestiti.getResponse());
	                            cout.flush();
	                        }
	                    }
	                    break;

	                	

	                default:
	                    serverResponse = HttpStatus.INTERNAL_ERROR.getCodice() + "\t" + HttpStatus.INTERNAL_ERROR.getMessaggio() + "\t" + request.getActionType()+"\t"+"Null type exception: actiontype";
	                    cout.println(serverResponse);
	                    cout.flush();
	                    break;
	            }

	        } 
	        catch (java.net.SocketException se) 
	        {
	            System.out.println("Client ha chiuso la connessione (SocketException).");
	            break;
	        } 
	        catch (JacksonException e) {
	            System.out.println("ERRORE GRAVE nel payload:");
	            chiudiStreams();
	            e.printStackTrace();
	           
	            break;
	        } finally {
	            lista.clear();
	            
	        }
	    }

	    // chiusura risorse
	    chiudiStreams();
	}

	
	



	private void chiudiStreams() throws IOException
	{
		this.cout.close();
		this.inputDaClient.close();
		this.clientSocket.close();
		this.serverSocket.close();
	}
}