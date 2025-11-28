package it.molinari.server.model;

import java.time.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;


@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME,
	    include = JsonTypeInfo.As.PROPERTY,
	    property = "tipo",
	    visible = true
	)
	@JsonSubTypes({
	    @JsonSubTypes.Type(value = Libro.class, name = "Libro"),
	    @JsonSubTypes.Type(value = Rivista.class, name = "Rivista"),
	    @JsonSubTypes.Type(value = Cd.class, name = "Cd")
	})

	@JsonIdentityInfo(
	    generator = ObjectIdGenerators.PropertyGenerator.class,
	    property = "id"
	)

public class Item 
{
	public String nome, autore, richiedente, tipologia, classe;
	public int codice, id;
	

	public void setPrestato(boolean isPrestato) {
		this.isPrestato = isPrestato;
	}

	public boolean isPrestato;
	public LocalTime orarioPrestito;
	
	public Item()
	{
		
	}
	
	public Item(String nome, String autore, boolean isPrestato, String richiedente, int codice, String tipologia, LocalTime orarioPrestito)
	{
		this.nome=nome;
		this.autore=autore;
		this.isPrestato=isPrestato;
		this.richiedente=richiedente;
		this.codice=codice;
		this.tipologia=tipologia;
		this.orarioPrestito=orarioPrestito;
	}
	
	
	@Override
	public String toString() 
	{
		return this.tipologia +":\n"+
	           "  Nome           : " + this.nome + "\n" +
	           "  Autore         : " + this.autore + "\n" +
	           "  Prestato       : " + this.isPrestato + "\n" +
	           "  Richiedente    : " + (this.richiedente != null ? this.richiedente : "Nessuno") + "\n" +
	           "  Codice         : " + this.codice + "\n" +
	           "  Tipologia      : " + this.tipologia + "\n" +
	           "  Orario prestito: " + (this.orarioPrestito != null ? this.orarioPrestito : "N/D") + "\n";
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getAutore() {
		return autore;
	}

	
	
	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public void setAutore(String autore) {
		this.autore = autore;
	}

	public String getRichiedente() {
		return richiedente;
	}

	public void setRichiedente(String richiedente) {
		this.richiedente = richiedente;
	}

	public String getTipologia() {
		return tipologia;
	}

	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}

	public int getCodice() {
		return codice;
	}

	public void setCodice(int codice) {
		this.codice = codice;
	}

	public boolean getIsPrestato() {
		return isPrestato;
	}

	public void setIsPrestato(boolean isPrestato) {
		this.isPrestato = isPrestato;
	}

	public LocalTime getOrarioPrestito() {
		return orarioPrestito;
	}

	public void setOrarioPrestito(LocalTime orarioPrestito) {
		this.orarioPrestito = orarioPrestito;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
