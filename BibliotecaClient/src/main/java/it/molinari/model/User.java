package it.molinari.model;

public class User 
{

	private String nome;
	private String cognome;
	private String username;
	private String password;
	private boolean ruolo;
	
	public User() {}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public User(String nome, String cognome, String username, String password, boolean ruolo)
	{

		this.nome=nome;
		this.cognome=cognome;
		this.username=username;
		this.password=password;
		this.ruolo=ruolo;
	}
	
/*	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("User {\n");
	    sb.append("  nome: ").append(nome).append(",\n");
	    sb.append("  cognome: ").append(cognome).append(",\n");
	    sb.append("  username: ").append(username).append(",\n");
	    sb.append("  pass: ").append(password).append(",\n");
	    sb.append("  ruolo: ").append(ruolo).append(",\n");
	    sb.append("}");
	    return sb.toString();
	}
*/
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

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRuolo() {
		return ruolo;
	}

	public void setRuolo(boolean ruolo) {
		this.ruolo = ruolo;
	}
}
