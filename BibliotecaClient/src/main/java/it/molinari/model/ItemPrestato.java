package it.molinari.model;

public class ItemPrestato extends Item
{
	 protected String nomePrestatario, cognome, inizioPrestito, FinePrestito;
	 
	 protected Item item = new Item();
	 
	public ItemPrestato()
	{
		super();
	}

	public String getNome() {
		return nomePrestatario;
	}

	public void setNome(String nome) {
		this.nomePrestatario = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getInizioPrestito() {
		return inizioPrestito;
	}

	public void setInizioPrestito(String inizioPrestito) {
		this.inizioPrestito = inizioPrestito;
	}

	public String getFinePrestito() {
		return FinePrestito;
	}

	public void setFinePrestito(String finePrestito) {
		FinePrestito = finePrestito;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	
}