package it.molinari.server.app.server.model;

import java.time.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;


@JsonTypeInfo //usata per gestire il polimorfismo e ereditarietà
(
		use= JsonTypeInfo.Id.NAME, //usa il nome specificato in subTypes
		include =JsonTypeInfo.As.PROPERTY, //aggiunge al json una proprieta che indica il tipo di oggetto
		property= "tipologia", //nome da dare alla proprieta che indica il tipo oggetto
		visible = true //rende accessibile la proprietà tipo altrimenti usata solo da jackson in modo privato (restituirebbe null senza)
)

@JsonSubTypes //dico a jacksomn qulai specializzazioni della superclasse esistono (definite in jsonTypeInfo) 
(
		{
			//value = sottoclasse creata da jackson, name= nome da dare all'atributo
			@JsonSubTypes.Type(value=Libro.class, name= "Libro"),
			@JsonSubTypes.Type(value=Rivista.class, name= "Rivista"),
			@JsonSubTypes.Type(value=Cd.class, name= "Cd")
		}
)


public class Item 
{
	public String nome, autore, richiedente, tipologia;
	public int codice;
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


}
