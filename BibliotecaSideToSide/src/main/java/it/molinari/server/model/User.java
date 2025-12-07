package it.molinari.server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User 
{
	//private int id;
	private String nome, cognome, username, password;
	//private List<Item> listisInprestito = new ArrayList<>();
	private boolean ruolo;
	
	public User() 
	{
		
	}
	
	public User(String nome, String cognome, String username, String password, boolean ruolo)
	{
		//this.id=id;
		this.nome=nome;
		this.cognome=cognome;
		this.username=username;
		this.password=password;
		//this.listisInprestito=listIsPrestito;
		this.ruolo=ruolo;
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("User {\n");
	    //sb.append("  id: ").append(this.id).append(",\n");
	    sb.append("  nome: ").append(nome).append(",\n");
	    sb.append("  cognome: ").append(cognome).append(",\n");
	    sb.append("  username: ").append(username).append(",\n");
	    sb.append("  pass: ").append(password).append(",\n");
	    sb.append("  ruolo: ").append(ruolo).append(",\n");
	    sb.append("  listisInprestito: [\n");
	    
	    /*if (listisInprestito != null) {
	        for (Item item : listisInprestito) {
	            if (item != null) {
	                sb.append("    ").append(item.toString().replace("\n", "\n    ")).append(",\n");
	            } else {
	                sb.append("    null,\n");
	            }
	        }
	    }*/
	    
	    sb.append("  ]\n");
	    sb.append("}");
	    return sb.toString();
	}

	
	/*public void setId(int id)
	{
		this.id=id;
	}*/

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String pass) {
		this.password = pass;
	}

	/*public List<Item> getListisInprestito() {
		return listisInprestito;
	}

	public void setListisInprestito(List<Item> listisInprestito) {
		this.listisInprestito = listisInprestito;
	}*/

	public boolean isRuolo() {
		return ruolo;
	}

	public void setRuolo(boolean ruolo) {
		this.ruolo = ruolo;
	}

	/*public int getId() {
		return id;
	}*/
}
