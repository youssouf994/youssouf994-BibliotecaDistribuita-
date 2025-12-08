package it.molinari.server.service;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JacksonException;

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
	public Payload() throws IOException
	{
		
	}
	
	
	
	public void payload() throws IOException {
	    System.out.println("=== SERVER IN ASCOLTO ===");
	    
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
	                chiudiStreams();
	                /*if (request != null && request.getToken() != null && !request.getToken().isEmpty()) 
		            {
		                tokenizer.cancellaToken(request.getToken());
		            }*/
	                

	                break;
	            }

	            // Comando di terminazione
	            if (super.str.equals("end)")) 
	            {
	                System.out.println("Comando di chiusura ricevuto");
	                super.cout.println(HttpStatus.OK.getCodice() + " " + HttpStatus.OK.getMessaggio());
	                chiudiStreams();
	               /* if (request != null && request.getToken() != null && !request.getToken().isEmpty()) 
		            {
		                tokenizer.cancellaToken(request.getToken());
		            }*/

	                break;
	            }

	            // DEBUG
	            System.out.println("=== SERVER RICEVUTO ===");
	            System.out.println("Stringa ricevuta: " + super.str);
	            System.out.println("Lunghezza: " + super.str.length());
	            System.out.println("======================");

	            // Deserializza JSON
	            try 
	            {
	                System.out.println("Tentativo di deserializzazione JSON...");
	                request = mapper.readValue(super.str, Request.class);
	                super.lista = request.getDati();
	                super.actionType = request.getActionType();
	                super.tokenizer.setToken(request.getToken());
	      
	                System.out.println("JSON deserializzato con successo!\n" + request.getDati());
	            } 
	            catch (Exception e) 
	            {
	                System.out.println("ERRORE deserializzazione: " + e.getMessage());
	                e.printStackTrace();
	                super.cout.println(HttpStatus.BAD_REQUEST.getCodice() + " JSON non valido");
	                super.cout.flush();
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
	                        	if(request.getToken() == null || request.getToken().isEmpty())
	                        	{
		                            request.setToken(tokenizer.getToken());
		                            request.setActionType(ActionType.LOGIN_RESPONSE);
		                            loginResponse.setJson("Benvenuto "+utente.getUsername());//se mi manda anche nome e cognome li uso per dare il benvenuto
		                            loginResponse.setResponse(HttpStatus.OK.getCodice(), HttpStatus.OK.getMessaggio(), request.getToken(), request.getActionType(), loginResponse.getJson());
		                            //serverResponse = HttpStatus.OK.getCodice()+"\t"+HttpStatus.OK.getMessaggio() + "\t" + token + "\t" + request.getActionType() + "\t" + userJson;
	                        	}
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

	                case GET_ITEMS_REQUEST:
	                	GetBookRes responseBook = new GetBookRes();
	                	responseBook.setActionType(ActionType.GET_ITEMS_RESPONSE);
	                	
	                    if (tokenizer.isInSession(request.getToken())) 
	                    {
	                    	responseBook.setToken(request.getToken());
	                        lista.add(engine.getCollezione());
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
	                        responseBook.setActionType(ActionType.GET_ITEMS_RESPONSE);
	                        responseBook.setResponse(HttpStatus.NOT_FOUND.getCodice(), HttpStatus.NOT_FOUND.getMessaggio(), responseBook.getToken(), responseBook.getActionType(), "Accesso al sistema non consentito verifica validità sessione");
	                    }
	                    cout.println(responseBook.getResponse());
	                    cout.flush();
	                    break;
	                    
	                case BORROW_ITEM_REQUEST:
	                	GestorePrestiti gestorePrestiti = new GestorePrestiti();
	                	if (tokenizer.isInSession(request.getToken()))
	                	{ 	                		
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
	                		
	                	}
	                	else
	                	{
	                		gestorePrestiti.setActionType(ActionType.GET_ITEMS_RESPONSE);
	                        gestorePrestiti.setResponse(HttpStatus.NOT_FOUND.getCodice(), HttpStatus.NOT_FOUND.getMessaggio(), gestorePrestiti.getToken(), gestorePrestiti.getActionType(), "Accesso al sistema non consentito verifica validità sessione");
	                	}
	                	cout.println(gestorePrestiti.getResponse());
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
	                        }
	                    }
	                    else
	                    {
	                    	gestorePrestiti.setActionType(ActionType.GET_ITEMS_RESPONSE);
	                        gestorePrestiti.setResponse(HttpStatus.NOT_FOUND.getCodice(), HttpStatus.NOT_FOUND.getMessaggio(), gestorePrestiti.getToken(), gestorePrestiti.getActionType(), "Accesso al sistema non consentito verifica validità sessione");
	                    }
	                    

                        cout.println(gestorePrestiti.getResponse());
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
	                			gestorePrestiti.setResponse(HttpStatus.OK.getCodice(), HttpStatus.OK.getMessaggio(), request.getToken(), ActionType.GET_USER_LOANS_RESPONSE, "nessun Prestito a carico");
							}
	                		else
	                		{
	                			userJson=converter.listToString(lista);
	                			gestorePrestiti.setResponse(HttpStatus.OK.getCodice(), HttpStatus.OK.getMessaggio(), request.getToken(), ActionType.GET_USER_LOANS_RESPONSE, userJson);
	                		}
	                	}
	                	else
	                	{
	                		gestorePrestiti.setActionType(ActionType.GET_USER_LOANS_RESPONSE);
	                        gestorePrestiti.setResponse(HttpStatus.NOT_FOUND.getCodice(), HttpStatus.NOT_FOUND.getMessaggio(), gestorePrestiti.getToken(), gestorePrestiti.getActionType(), "Accesso al sistema non consentito verifica validità sessione");
	                	}


                        cout.println(gestorePrestiti.getResponse());
                        cout.flush();
	                	
                	break;
	                	
	                case ADD_ITEMS_REQUEST:
	                	engine = new GestioneCollezione();
	                	
	                	if(tokenizer.isInSession(request.getToken()))
	                	{
	                		System.out.println(lista);
	                		Item item= mapper.convertValue(lista.get(1), Item.class);
	                		User utente = mapper.convertValue(lista.get(0), User.class);
	                		
	                		if(utente.isRuolo()==true)
            				{
	                			if (engine.aggiungiItem(item))
            					{
	                				userJson=item.getQuanti()+" "+item.getNome()+" inseriti correttamente";
	                				engine.setResponse(HttpStatus.OK.getCodice(), HttpStatus.OK.getMessaggio(), request.getToken(), ActionType.ADD_ITEMS_RESPONSE, userJson);			
            					}
		                		else 
		                		{
		                			userJson="Non hai compilato tutti i campi richiesti"+ mapper.writeValueAsString(item);
		                			engine.setResponse(HttpStatus.OK.getCodice(), HttpStatus.OK.getMessaggio(), request.getToken(), ActionType.ADD_ITEMS_RESPONSE, userJson);
		                		}
            				}
	                		else
	                		{
		                		engine.setActionType(ActionType.ADD_ITEMS_RESPONSE);
		                        engine.setResponse(HttpStatus.NOT_FOUND.getCodice(), HttpStatus.NOT_FOUND.getMessaggio(), request.getToken(), ActionType.ADD_ITEMS_RESPONSE, "r+w-x+ richiedere privilegi ad amministratore");

	                		}
	                	}
	                	else
	                	{
	                		engine.setActionType(ActionType.ADD_ITEMS_RESPONSE);
	                        engine.setResponse(HttpStatus.NOT_FOUND.getCodice(), HttpStatus.NOT_FOUND.getMessaggio(), request.getToken(), ActionType.ADD_ITEMS_RESPONSE, "Accesso al sistema non consentito verifica validità sessione");
	                	}
	                	
	                	cout.println(engine.getResponse());
                        cout.flush();
                	break;
	                	
	                case REMOVE_ITEMS_REQUEST:
	                	engine = new GestioneCollezione();
	                	
	                	if(tokenizer.isInSession(request.getToken()))
	                	{
	                		Item item = mapper.convertValue(lista.get(1), Item.class);
	                		User user =mapper.convertValue(lista.get(0), User.class);
	                		
	                		engine.setActionType(ActionType.REMOVE_ITEMS_RESPONSE);
	                		
	                		if(user.isRuolo()==true)
	                		{
		                		if(engine.cancellaItem(item))
		                		{
		                			userJson=item.getQuanti()+" "+item.getNome()+" cancellati correttamente";
	                				engine.setResponse(HttpStatus.OK.getCodice(), HttpStatus.OK.getMessaggio(), request.getToken(), engine.getActionType(), userJson);			
		                		}
		                		else
		                		{
		                			userJson="id o quantità errati";
	                				engine.setResponse(HttpStatus.OK.getCodice(), HttpStatus.OK.getMessaggio(), request.getToken(), engine.getActionType(), userJson);			
		                		}
	                		}
	                	}
	                	else
	                	{
	                		engine.setActionType(ActionType.REMOVE_ITEMS_RESPONSE);
	                        engine.setResponse(HttpStatus.NOT_FOUND.getCodice(), HttpStatus.NOT_FOUND.getMessaggio(), engine.getToken(), engine.getActionType(), "Accesso al sistema non consentito verifica validità sessione");
	                	}
	                	
	                	cout.println(engine.getResponse());
                        cout.flush();
            		break;
            		
	                case SEARCH_ITEMS_REQUEST:
	                	engine = new GestioneCollezione();
	                	
	                	if(tokenizer.isInSession(request.getToken()))
	                	{
		                	Ricercato ricercato = new Ricercato();
		                	Ricerca ricerca = new Ricerca();
		                	
		                	ricercato=mapper.convertValue(lista.get(0), Ricercato.class);
		                	
		                	userJson=ricerca.cerca(ricercato.getValore(), ricercato.getModalita());
		                	engine.setActionType(ActionType.SEARCH_ITEMS_RESPONSE);
		                	engine.setResponse(HttpStatus.OK.getCodice(), HttpStatus.OK.getMessaggio(), request.getToken(), engine.getActionType(), userJson);			
	                	
	                	}
	                	else
	                	{
	                		engine.setActionType(ActionType.SEARCH_ITEMS_RESPONSE);
	                        engine.setResponse(HttpStatus.NOT_FOUND.getCodice(), HttpStatus.NOT_FOUND.getMessaggio(), engine.getToken(), engine.getActionType(), "Accesso al sistema non consentito verifica validità sessione");
	                	}
	                	
	                	
	                	cout.println(engine.getResponse());
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
	            chiudiStreams();
	            

	            break;
	        } 
	        catch (JacksonException e) {
	            System.out.println("ERRORE GRAVE nel payload:");
	            chiudiStreams();
	            
	            e.printStackTrace();
	            break;
	            
	        } 
	        finally 
	        {
	        	//lista.clear();
	            
	        }
	    }
	}
	
	
}
