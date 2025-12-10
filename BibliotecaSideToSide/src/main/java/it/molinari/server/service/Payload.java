package it.molinari.server.service;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.molinari.server.enums.ActionType;
import it.molinari.server.enums.HttpStatus;
import it.molinari.server.model.Item;
import it.molinari.server.model.ItemPrestato;
import it.molinari.server.model.Ricercato;
import it.molinari.server.model.User;
import it.molinari.server.request.LoginRequest;
import it.molinari.server.request.Request;
import it.molinari.server.response.GetBookRes;
import it.molinari.server.response.LoginResponse;
import it.molinari.server.response.RegistrationResponse;

public class Payload extends GestioneConnessione
{
	ObjectNode jsonApp = mapper.createObjectNode();
	JsonNode nodoJsonApp;
	ItemPrestato item= new ItemPrestato();
	User utente = new User();
	Item item2 = new Item();
	
	
	public Payload() throws IOException
	{
		super();
	}
	
	public Payload(GestioneConnessione conn) {

	    // Socket & networking
	    this.serverSocket   = conn.serverSocket;
	    this.clientSocket   = conn.clientSocket;
	    this.inputDaClient  = conn.inputDaClient;
	    this.cout           = conn.cout;

	    // Variabili operative
	    this.str            = conn.str;
	    this.serverResponse = conn.serverResponse;
	    this.userJson       = conn.userJson;

	    // Oggetti helper
	    this.mapper         = conn.mapper;
	    this.converter      = conn.converter;
	    this.tokenizer      = conn.tokenizer;
	    this.lista          = conn.lista;

	    // Business logic
	    this.actionType     = conn.actionType;
	    this.engine         = conn.engine;

	    // Request/Response JSON
	    this.request        = conn.request;
	    this.response       = conn.response;
	}

	
	public void payload() throws IOException {
	    System.out.println("=== SERVER IN ASCOLTO ===");
	    String currentToken = null; // Mantieni traccia del token corrente
	    
	    try {
	        while (true) 
	        {
	            try 
	            {
	                System.out.println("In attesa di dati dal client...");
	                super.str = super.inputDaClient.readLine();

	                // Controllo chiusura connessione
	                if (super.str == null) 
	                {
	                    System.out.println("Client ha chiuso la connessione");
	                    break; // Esci dal while, il finally gestirà la pulizia
	                }

	                // Comando di terminazione
	                if (super.str.equals("end")) 
	                {
	                    System.out.println("Comando di chiusura ricevuto");
	                    super.cout.println(HttpStatus.OK.getCodice() + " " + HttpStatus.OK.getMessaggio());
	                    break; // Esci dal while, il finally gestirà la pulizia
	                }

	                // DEBUG
	                System.out.println("=== SERVER RICEVUTO ===");
	                System.out.println("Stringa ricevuta: " + super.str);
	                System.out.println("Lunghezza: " + super.str.length());
	                System.out.println("======================");

	                // Deserializza JSON
	                try 
	                {
	                    request = mapper.readValue(super.str, Request.class);
	                    super.lista = request.getDati();
	                    super.actionType = request.getActionType();
	                    super.tokenizer.setToken(request.getToken());
	                    
	                    // Aggiorna il token corrente
	                    if (request.getToken() != null && !request.getToken().isEmpty()) {
	                        currentToken = request.getToken();
	                    }
	          
	                    System.out.println("JSON deserializzato con successo!\n" + request.getToken()+" "+request.getDati());
	                } 
	                catch (Exception e) 
	                {
	                    System.out.println("ERRORE deserializzazione: " + e.getMessage());
	                    e.printStackTrace();
	                    super.cout.println(HttpStatus.BAD_REQUEST.getCodice() + " JSON non valido");
	                    super.cout.flush();
	                    continue;
	                }

	                switch (request.getActionType()) 
	                {
	                case LOGIN_REQUEST:
	                	LoginResponse loginResponse = new LoginResponse();
	                	LoginRequest login = new LoginRequest();
	                	
	                    if (lista == null || lista.isEmpty()) 
	                    {   	
	                    	loginResponse.setCodice(HttpStatus.BAD_REQUEST.getCodice());
	                    	loginResponse.setMessaggio(HttpStatus.BAD_REQUEST.getMessaggio()+"Utente non registrato");
	                    } 
	                    else 
	                    {
	                        User utente = mapper.convertValue(lista.get(0), User.class);
      
	                        if (login.provaLogin(utente)) 
	                        {
	                        	if(request.getToken() == null || request.getToken().isEmpty())
	                        	{
		                            request.setToken(tokenizer.getToken());
		                            request.setActionType(ActionType.LOGIN_RESPONSE);
		                            loginResponse.setResponse(HttpStatus.OK.getCodice(), HttpStatus.OK.getMessaggio(), request.getToken(), request.getActionType());		                        
	                        	} 
		                        else 
		                        {
		                        	request.setActionType(ActionType.LOGIN_RESPONSE);
		                        	request.setToken("");
		                        	
		                        	loginResponse.setResponse(HttpStatus.NOT_FOUND.getCodice(), HttpStatus.NOT_FOUND.getMessaggio()+"username o password errati", request.getToken(), request.getActionType());
		                        }
	                        }
	                    }
                    	jsonApp = mapper.createObjectNode();
                    	nodoJsonApp = mapper.valueToTree(loginResponse);
                    	jsonApp.setAll((ObjectNode) nodoJsonApp);
                    	jsonApp.set("User", mapper.valueToTree(login.getU()));
	                    System.out.println(mapper.writeValueAsString(jsonApp));
	                    cout.println(mapper.writeValueAsString(jsonApp));
	                    cout.flush();
	                    break;

	                case REGISTRATION_REQUEST:
	                    User nuovoUtente = mapper.convertValue(lista.get(0), User.class);
	                    Registrazione registrazione = new Registrazione();                  
	                    RegistrationResponse response = new RegistrationResponse();
	                    response.setActionType(ActionType.REGISTRATION_REQUEST);
	                    
	                    if (registrazione.registra(nuovoUtente)) 
	                    {       	
	                    	response.setResponse(HttpStatus.OK.getCodice(), HttpStatus.OK.getMessaggio(), "", response.getActionType());     } 
	                    else 
	                    {
	                    	response.setResponse(HttpStatus.BAD_REQUEST.getCodice(), HttpStatus.BAD_REQUEST.getMessaggio()+"Questo utente è già registrato", "", response.getActionType());
	                    }
	                    
	                    jsonApp = mapper.createObjectNode();
                    	nodoJsonApp = mapper.valueToTree(response);
                    	jsonApp.setAll((ObjectNode) nodoJsonApp);
                    	jsonApp.set("Registrazione", mapper.valueToTree(response));
                    	jsonApp.set("Registrato", mapper.valueToTree(registrazione));
	                    System.out.println(mapper.writeValueAsString(jsonApp));
	                    cout.println(mapper.writeValueAsString(jsonApp));
	                    cout.flush();
	                    break;

	               case GET_ITEMS_REQUEST:
	                	GetBookRes responseBook = new GetBookRes();
	                	responseBook.setActionType(ActionType.GET_ITEMS_RESPONSE);
	                	
	                    if (tokenizer.isInSession(request.getToken())) 
	                    {
	                    	responseBook.setToken(request.getToken());
	                        lista.add(engine.getCollezione());


	                        if(lista!=null)
	                        {
                        		responseBook.setResponse(HttpStatus.OK.getCodice(),HttpStatus.OK.getMessaggio(), responseBook.getToken(), responseBook.getActionType());
	                        }
	                        else
	                        {
	                        	responseBook.setResponse(HttpStatus.NOT_FOUND.getCodice(), HttpStatus.NOT_FOUND.getMessaggio()+"Non sono presenti articoli nella collezione", responseBook.getToken(), responseBook.getActionType());
	                        }	             
	                    } 
	                    else 
	                    {
	                        responseBook.setActionType(ActionType.GET_ITEMS_RESPONSE);
	                        responseBook.setResponse(HttpStatus.NOT_FOUND.getCodice(), HttpStatus.NOT_FOUND.getMessaggio()+"Accesso al sistema non consentito verifica validità sessione", responseBook.getToken(), responseBook.getActionType());
	                    }
	                    jsonApp = mapper.createObjectNode();
                    	nodoJsonApp = mapper.valueToTree(responseBook);
                    	jsonApp.setAll((ObjectNode) nodoJsonApp);
                    	jsonApp.set("listaData", mapper.valueToTree(engine.getCollezione()));
	                    System.out.println(mapper.writeValueAsString(jsonApp));
	                    cout.println(mapper.writeValueAsString(jsonApp));
	                    cout.flush();
	                    break;
	                    
	                case BORROW_ITEM_REQUEST:
	                	GestorePrestiti gestorePrestiti = new GestorePrestiti();
	                	
	                	if (tokenizer.isInSession(request.getToken()))
	                	{ 	                		   		
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
	                			//userJson="libro: "+item.getItem().getNome()+" Autore: "+item.getItem().getAutore()+" preso in prestito fino al "+item.getFinePrestito();
	                			gestorePrestiti.setResponse(HttpStatus.OK.getCodice(),HttpStatus.OK.getMessaggio(), request.getToken(), gestorePrestiti.getActionType());
	                		}
	                		else
	                		{
	                			gestorePrestiti.setResponse(HttpStatus.NOT_FOUND.getCodice(),HttpStatus.NOT_FOUND.getMessaggio()+"Il libro selezionato non è disponibile o inesistente", gestorePrestiti.getToken(), gestorePrestiti.getActionType());
	                		}
	                		
	                	}
	                	else
	                	{
	                		gestorePrestiti.setActionType(ActionType.GET_ITEMS_RESPONSE);
	                        gestorePrestiti.setResponse(HttpStatus.NOT_FOUND.getCodice(), HttpStatus.NOT_FOUND.getMessaggio()+"Accesso al sistema non consentito verifica validità sessione", gestorePrestiti.getToken(), gestorePrestiti.getActionType());
	                	}
	                	jsonApp = mapper.createObjectNode();
                    	nodoJsonApp = mapper.valueToTree(gestorePrestiti);
                    	jsonApp.setAll((ObjectNode) nodoJsonApp);
                    	jsonApp.set("listaData", mapper.valueToTree(item));
	                    System.out.println(mapper.writeValueAsString(jsonApp));
	                    cout.println(mapper.writeValueAsString(jsonApp));
	                    cout.flush();
	                    break;
	                	
	                case RETURN_ITEM_REQUEST:
	                	gestorePrestiti=new GestorePrestiti();
	                    if (tokenizer.isInSession(request.getToken())) 
	                    {
	                        // Trova l'oggetto ItemPrestato nella lista
	                        Object appoggio = lista.stream()
	                            .filter(o -> o  instanceof Map m && "ItemPrestato".equals(m.get("tipologia")))
	                            .findFirst()
	                            .orElse(null);

	                        if (appoggio != null) {
	                            item = mapper.convertValue(appoggio, ItemPrestato.class);

	                            gestorePrestiti.setActionType(ActionType.RETURN_ITEM_RESPONSE);

	                            boolean ok = gestorePrestiti.ritornaPrestito(item);

	                            int status;
	                            String messaggio;

	                            if (ok) 
	                            {
	                                status = HttpStatus.OK.getCodice();
	                                messaggio = HttpStatus.OK.getMessaggio();
	                            } 
	                            else 
	                            {
	                                status = HttpStatus.NOT_FOUND.getCodice();
	                                messaggio = HttpStatus.NOT_FOUND.getMessaggio()+"ID articolo non presente nel sistema";
	                            }
	                            gestorePrestiti.setResponse(status, messaggio, request.getToken(), gestorePrestiti.getActionType());
	                        }
	                    }
	                    else
	                    {
	                    	gestorePrestiti.setActionType(ActionType.GET_ITEMS_RESPONSE);
	                        gestorePrestiti.setResponse(HttpStatus.NOT_FOUND.getCodice(), HttpStatus.NOT_FOUND.getMessaggio()+"Accesso al sistema non consentito verifica validità sessione", gestorePrestiti.getToken(), gestorePrestiti.getActionType());
	                    }
	                    
	                    jsonApp = mapper.createObjectNode();
                    	nodoJsonApp = mapper.valueToTree(gestorePrestiti);
                    	jsonApp.setAll((ObjectNode) nodoJsonApp);
                    	jsonApp.set("listaData", mapper.valueToTree(item));
	                    System.out.println(mapper.writeValueAsString(jsonApp));
                        cout.flush();
	                    break;
	                    
	                case GET_USER_LOANS_REQUEST:
	                	gestorePrestiti=new GestorePrestiti();
	                	
	                	if (tokenizer.isInSession(request.getToken())) 
	                	{
	                		User utente= mapper.convertValue(lista.get(0), User.class);
	                		String username= utente.getUsername();
	                		lista=gestorePrestiti.prestitiUtente(username);
	                		if(lista.isEmpty())
							{
	                			gestorePrestiti.setResponse(HttpStatus.OK.getCodice(), HttpStatus.OK.getMessaggio()+" nessun Prestito a carico", request.getToken(), ActionType.GET_USER_LOANS_RESPONSE);
							}
	                		else
	                		{
	                			gestorePrestiti.setResponse(HttpStatus.OK.getCodice(), HttpStatus.OK.getMessaggio(), request.getToken(), ActionType.GET_USER_LOANS_RESPONSE);
	                		}
	                	}
	                	else
	                	{
	                		gestorePrestiti.setActionType(ActionType.GET_USER_LOANS_RESPONSE);
	                        gestorePrestiti.setResponse(HttpStatus.NOT_FOUND.getCodice(), HttpStatus.NOT_FOUND.getMessaggio()+" Accesso al sistema non consentito verifica validità sessione", gestorePrestiti.getToken(), gestorePrestiti.getActionType());
	                	}
	                	
	                	jsonApp = mapper.createObjectNode();
                    	nodoJsonApp = mapper.valueToTree(gestorePrestiti);
                    	jsonApp.setAll((ObjectNode) nodoJsonApp);
                    	jsonApp.set("listaData", mapper.valueToTree(lista));
	                    System.out.println(mapper.writeValueAsString(jsonApp));
                        cout.flush();
	                	
                	break;
	                	
	                case ADD_ITEMS_REQUEST:
	                	engine = new GestioneCollezione();
	                	
	                	
	                	if(tokenizer.isInSession(request.getToken()))
	                	{
	                		System.out.println(lista);
	                		item2= mapper.convertValue(lista.get(1), Item.class);
	                		utente = mapper.convertValue(lista.get(0), User.class);
	                		
	                		if(utente.isRuolo()==true)
            				{
	                			if (engine.aggiungiItem(item))
            					{
	                				userJson=item2.getQuanti()+" "+item2.getNome()+" inseriti correttamente";
	                				engine.setResponse(HttpStatus.OK.getCodice(), HttpStatus.OK.getMessaggio()+" "+userJson, request.getToken(), ActionType.ADD_ITEMS_RESPONSE);			
            					}
		                		else 
		                		{
		                			userJson="Non hai compilato tutti i campi richiesti"+ mapper.writeValueAsString(item2);
		                			engine.setResponse(HttpStatus.OK.getCodice(), HttpStatus.OK.getMessaggio()+userJson, request.getToken(), ActionType.ADD_ITEMS_RESPONSE);
		                		}
            				}
	                		else
	                		{
		                		engine.setActionType(ActionType.ADD_ITEMS_RESPONSE);
		                        engine.setResponse(HttpStatus.NOT_FOUND.getCodice(), HttpStatus.NOT_FOUND.getMessaggio()+"r+w-x+ richiedere privilegi ad amministratore", request.getToken(), ActionType.ADD_ITEMS_RESPONSE);

	                		}
	                	}
	                	else
	                	{
	                		engine.setActionType(ActionType.ADD_ITEMS_RESPONSE);
	                        engine.setResponse(HttpStatus.NOT_FOUND.getCodice(), HttpStatus.NOT_FOUND.getMessaggio()+"Accesso al sistema non consentito verifica validità sessione", request.getToken(), ActionType.ADD_ITEMS_RESPONSE);
	                	}
	                	
	                	jsonApp = mapper.createObjectNode();
                    	nodoJsonApp = mapper.valueToTree(engine);
                    	jsonApp.setAll((ObjectNode) nodoJsonApp);
                    	jsonApp.set("User", mapper.valueToTree(utente));
                    	jsonApp.set("Item", mapper.valueToTree(item2));
	                    System.out.println(mapper.writeValueAsString(jsonApp));
                        cout.flush();
                	break;
	                	
	                case REMOVE_ITEMS_REQUEST:
	                	engine = new GestioneCollezione();
	                	User user =mapper.convertValue(lista.get(0), User.class);
	                	if(tokenizer.isInSession(request.getToken()))
	                	{
	                		item2 = mapper.convertValue(lista.get(1), Item.class);
	
	                		engine.setActionType(ActionType.REMOVE_ITEMS_RESPONSE);
	                		
	                		if(user.isRuolo()==true)
	                		{
		                		if(engine.cancellaItem(item))
		                		{
		                			userJson=item2.getQuanti()+" "+item2.getNome()+" cancellati correttamente";
	                				engine.setResponse(HttpStatus.OK.getCodice(), HttpStatus.OK.getMessaggio()+userJson, request.getToken(), engine.getActionType());			
		                		}
		                		else
		                		{
		                			userJson=" id o quantità errati";
	                				engine.setResponse(HttpStatus.OK.getCodice(), HttpStatus.OK.getMessaggio()+userJson, request.getToken(), engine.getActionType());			
		                		}
	                		}
	                	}
	                	else
	                	{
	                		engine.setActionType(ActionType.REMOVE_ITEMS_RESPONSE);
	                        engine.setResponse(HttpStatus.NOT_FOUND.getCodice(), HttpStatus.NOT_FOUND.getMessaggio()+" Accesso al sistema non consentito verifica validità sessione", request.getToken(), engine.getActionType());
	                	}
	                	
	                	jsonApp = mapper.createObjectNode();
                    	nodoJsonApp = mapper.valueToTree(engine);
                    	jsonApp.setAll((ObjectNode) nodoJsonApp);
                    	jsonApp.set("User", mapper.valueToTree(user));
                    	jsonApp.set("Item", mapper.valueToTree(item2));
	                    System.out.println(mapper.writeValueAsString(jsonApp));
                        cout.flush();
            		break;
            		
	                case SEARCH_ITEMS_REQUEST:
	                	engine = new GestioneCollezione();
	                	Ricerca ricerca = new Ricerca();
	                	
	                	if(tokenizer.isInSession(request.getToken()))
	                	{
		                	Ricercato ricercato = new Ricercato();
	
		                	ricercato=mapper.convertValue(lista.get(0), Ricercato.class);
		                	
		                	ricerca.cerca(ricercato.getValore(), ricercato.getModalita());
		                	engine.setActionType(ActionType.SEARCH_ITEMS_RESPONSE);
		                	engine.setResponse(HttpStatus.OK.getCodice(), HttpStatus.OK.getMessaggio(), request.getToken(), engine.getActionType());			
	                	
	                	}
	                	else
	                	{
	                		engine.setActionType(ActionType.SEARCH_ITEMS_RESPONSE);
	                        engine.setResponse(HttpStatus.NOT_FOUND.getCodice(), HttpStatus.NOT_FOUND.getMessaggio()+" Accesso al sistema non consentito verifica validità sessione", request.getToken(), engine.getActionType());
	                	}
	                	
	                	jsonApp = mapper.createObjectNode();
                    	nodoJsonApp = mapper.valueToTree(engine);
                    	jsonApp.setAll((ObjectNode) nodoJsonApp);
                    	jsonApp.set("User", mapper.valueToTree(ricerca));
                    	jsonApp.set("Item", mapper.valueToTree(item2));
	                    System.out.println(mapper.writeValueAsString(jsonApp));
                        cout.flush();
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
	        }
	    } 
	    catch (JacksonException e) 
	    {
	        System.out.println("ERRORE GRAVE nel payload: jackson");
	        e.printStackTrace();
	    } 
	    catch (Exception e) 
	    {
	        System.out.println("ERRORE IMPREVISTO nel payload: runtime");
	        e.printStackTrace();
	    }
	    finally 
	    {
	        System.out.println("Pulizia risorse in corso...");
	        
	        if (currentToken != null && !currentToken.isEmpty()) 
	        {
	            try 
	            {
	                tokenizer.cancellaToken(currentToken);
	                System.out.println("Token cancellato: " + currentToken);
	            } 
	            catch (Exception e) 
	            {
	                System.out.println("Errore durante cancellazione token: " + e.getMessage());
	            }
	        }
	        
	        try 
	        {
	            chiudiStreams();
	            System.out.println("Stream chiusi correttamente");
	        } 
	        catch (Exception e) 
	        {
	            System.out.println("Errore durante chiusura stream: " + e.getMessage());
	        }
	    }
	}
	
	
}
