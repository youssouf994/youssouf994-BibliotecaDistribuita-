package it.molinari.server.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME,
	    include = JsonTypeInfo.As.PROPERTY,
	    property = "@classe"
	)

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class User 
{
	private int id;
	private String nome, cognome, username, pass, classe;
	private List<Item> listisInprestito = new ArrayList<>();
	private boolean ruolo;
	
	public User() 
	{
		
	}
	
	public User(int id, String nome, String cognome, String username, String password, List<Item> listIsPrestito, boolean ruolo)
	{
		this.id=id;
		this.nome=nome;
		this.cognome=cognome;
		this.username=username;
		this.pass=password;
		this.listisInprestito=listIsPrestito;
		this.ruolo=ruolo;
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("User {\n");
	    sb.append("  id: ").append(id).append(",\n");
	    sb.append("  nome: ").append(nome).append(",\n");
	    sb.append("  cognome: ").append(cognome).append(",\n");
	    sb.append("  username: ").append(username).append(",\n");
	    sb.append("  pass: ").append(pass).append(",\n");
	    sb.append("  ruolo: ").append(ruolo).append(",\n");
	    sb.append("  classe: ").append(classe).append(",\n");
	    sb.append("  listisInprestito: [\n");
	    
	    if (listisInprestito != null) {
	        for (Item item : listisInprestito) {
	            if (item != null) {
	                sb.append("    ").append(item.toString().replace("\n", "\n    ")).append(",\n");
	            } else {
	                sb.append("    null,\n");
	            }
	        }
	    }
	    
	    sb.append("  ]\n");
	    sb.append("}");
	    return sb.toString();
	}

	
	public void setId(int id)
	{
		this.id=id;
	}

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

	
	
	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public List<Item> getListisInprestito() {
		return listisInprestito;
	}

	public void setListisInprestito(List<Item> listisInprestito) {
		this.listisInprestito = listisInprestito;
	}

	public boolean isRuolo() {
		return ruolo;
	}

	public void setRuolo(boolean ruolo) {
		this.ruolo = ruolo;
	}

	public int getId() {
		return id;
	}
}
