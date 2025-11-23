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
    private boolean isNuovo;
    private GestioneJson IOJson = new GestioneJson();
    
	public Tokenizer()
	{
		
	}
	
	private Token generaToken()
	{
		Token tk = new Token();
		String nuovoToken;
		
		while(true)
		{
			// Genera un nuovo token
			StringBuilder tokenBuilder = new StringBuilder(10);
	        for (int i = 0; i < 10; i++) {
	            int index = random.nextInt(CHARACTERS.length());
	            tokenBuilder.append(CHARACTERS.charAt(index));
	        }
	        nuovoToken = tokenBuilder.toString();
	        
	        // Leggi la lista dei token esistenti
	        this.listaToken = IOJson.leggiJsonToken(3);
	        
	        // Controlla se il token esiste già
	        boolean tokenEsiste = false;
	        
	        if (this.listaToken != null && !this.listaToken.isEmpty()) {
		        for (Token app : this.listaToken)
		        {
		        	if(app.getToken() != null && app.getToken().equals(nuovoToken))
		        	{
		        		tokenEsiste = true;
		        		break;
		        	}
		        }
	        }
	        
	        // Se il token è unico, esci dal loop
	        if (!tokenEsiste) {
	        	tk.setToken(nuovoToken);
	        	break;
	        }
		}
		
        return tk;
	}
	
	public Token getToken() {
		return this.generaToken();
	}
	
	public void setToken(String token) {
		this.token = token;
	}
}