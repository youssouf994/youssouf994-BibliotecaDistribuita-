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
	
	private String generaToken() {

	    List<Token> listaTokenLocale = converter.leggiJson(3, Token.class);
	    if (listaTokenLocale == null) listaTokenLocale = new ArrayList<>();

	    String nuovoToken;
	    do {
	        StringBuilder builder = new StringBuilder(10);
	        for (int i = 0; i < 10; i++) {
	            int index = random.nextInt(CHARACTERS.length());
	            builder.append(CHARACTERS.charAt(index));
	        }
	        nuovoToken = builder.toString();
	    } while (esisteGia(nuovoToken, listaTokenLocale));

	    Token nuovo = new Token();
	    nuovo.setToken(nuovoToken);

	    listaTokenLocale.add(nuovo);
	    converter.scriviJson(3, listaTokenLocale);

	    return nuovoToken;
	}

	private boolean esisteGia(String tok, List<Token> lista) {
	    for (Token t : lista) {
	        if (tok.equals(t.getToken())) return true;
	    }
	    return false;
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

	
	public boolean cancellaToken(String token) {
	    this.listaToken = converter.leggiJson(3, Token.class);
	    if (listaToken == null) return false;

	    for (int i = 0; i < listaToken.size(); i++) 
	    {
	        if (listaToken.get(i).getToken().equalsIgnoreCase(token)) 
	        {
	            listaToken.remove(i);
	            converter.scriviJson(3, listaToken);
	            return true; // Token rimosso correttamente
	        }
	    }
	    return false; // Token non trovato
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