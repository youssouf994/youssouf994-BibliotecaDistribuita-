package it.molinari.server.token;
import it.molinari.server.model.*;
import it.molinari.server.service.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Tokenizer 
{
	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();
    private String token;
    private List<Token> listaToken = new ArrayList<Token>();
    private boolean isInSession;
    private GestioneJson converter = new GestioneJson();
    
	public Tokenizer()
	{
		
	}
	
	private String generaToken()
	{
	    String nuovoToken;

	    // Leggi lista token esistente UNA VOLTA
	    this.listaToken = converter.leggiJson(3, Token.class);
	    if (this.listaToken == null) {
	        this.listaToken = new ArrayList<>();
	    }

	    while(true)
	    {
	        // Genera un nuovo token
	        StringBuilder tokenBuilder = new StringBuilder(10);
	        for (int i = 0; i < 10; i++) {
	            int index = random.nextInt(CHARACTERS.length());
	            tokenBuilder.append(CHARACTERS.charAt(index));
	        }
	        nuovoToken = tokenBuilder.toString();

	        // Controlla se il token esiste già
	        boolean tokenEsiste = false;
	        for(Token app : this.listaToken)
	        {
	            if(app.getToken() != null && app.getToken().equals(nuovoToken))
	            {
	                tokenEsiste = false;
	                break;
	            }
	            else
	            {
	            	tokenEsiste=true;
	            	break;
	            }
	        }

	        // Se è unico, salva e restituisci token
	        if(tokenEsiste=true)
	        {
	            this.token = nuovoToken;
	            Token nuovoTokenObj = new Token();
	    	    nuovoTokenObj.setToken(this.token);
	    	    this.listaToken.add(nuovoTokenObj);
	    	    converter.scriviJson(3, this.listaToken);
	            break;
	        }
	    }
	    return this.token;
	}

	
	public boolean isInSession(String token) 
	{
	    this.listaToken = converter.leggiJson(3, Token.class);
	    boolean check=false;
	    
	    if (this.listaToken != null) 
	    {
	        for (Token app : this.listaToken) 
	        {
	            if (app.getToken() != null && app.getToken().equalsIgnoreCase(token)) 
	            {
	                check=true;
	            }
	            else
	            {
	            	check=false;
	            }
	        }
	    }
	    
	    return check;
	}

	
	public boolean cancellaToken(String Token)
	{
		this.listaToken=converter.leggiJson(3, Token.class);
		int i=0;
			
		if(this.listaToken!=null)
		{
			for(i=0; i<listaToken.size();i++)
			{
				if(listaToken.get(i).getToken().equalsIgnoreCase(Token))
				{
					listaToken.remove(i);
					this.isInSession=true;
					break;
				}
				else
				{
					this.isInSession=false;
				}
			}
		}
		else
		{
			System.out.println("lista vuota");
		}
			return this.isInSession;
	}
	
	public String getToken() 
	{
		return this.generaToken();
	}
	
	public void setToken(String token) 
	{
		this.token=token;
	}
}