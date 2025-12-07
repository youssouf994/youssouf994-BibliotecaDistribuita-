package it.molinari.server.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;


@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME,
	    include = JsonTypeInfo.As.PROPERTY,
	    property = "tipologia",
	    visible = true
	)
	@JsonSubTypes({
	    @JsonSubTypes.Type(value = Libro.class, name = "Libro"),
	    @JsonSubTypes.Type(value = Rivista.class, name = "Rivista"),
	    @JsonSubTypes.Type(value = Cd.class, name = "Cd"),
	    @JsonSubTypes.Type(value = ItemPrestato.class, name = "ItemPrestato")
	})

	/*@JsonIdentityInfo(
	    generator = ObjectIdGenerators.PropertyGenerator.class,
	    property = "id"
	)*/

public class Item 
{
	public String nome, autore, tipologia;
	public int id, quanti;
	
	public Item()
	{
		
	}
	
	public Item(String nome, String autore, String tipologia, int quanti)
	{
		this.nome=nome;
		this.autore=autore;
		this.tipologia=tipologia;
		this.quanti=quanti;
	}
	
	
	@Override
	public String toString() 
	{
		return this.tipologia +":\n"+
	           "  Nome           : " + this.nome + "\n" +
	           "  Autore         : " + this.autore + "\n" +
	           "  Tipologia      : " + this.tipologia + "\n" +
			   "  Quantità disponibile: "+ this.quanti;
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

	
	

	public int getQuanti() {
		return quanti;
	}

	public void setQuanti(int quanti) {
		this.quanti = quanti;
	}

	public void setAutore(String autore) {
		this.autore = autore;
	}

	public String getTipologia() {
		return tipologia;
	}

	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
}
