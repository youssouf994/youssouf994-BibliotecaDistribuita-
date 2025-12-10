package it.molinari.model;

public class Cd extends Item
{
	public int numTracce, durata;
	private int id;

	public Cd() {}
	
	public Cd(String nome, String autore, int numTracce, int durata,  String tipologia,  int quanti)
	{
		super(nome, autore, tipologia, quanti);
		this.numTracce=numTracce;
		this.durata=durata;
		this.quanti=quanti;
	}
	
	@Override
	public String toString() 
	{
	    return super.toString() +
	           "  Tracce         : " + numTracce + "\n" +
	           "  Durata         : " + durata + " minuti\n"+
	           "  Quantità:		 : " + quanti + "pezzi";	
	}

	 public int getNumTracce() {
		 return numTracce;
	 }

	 public void setNumTracce(int numTracce) {
		 this.numTracce = numTracce;
	 }

	 public int getDurata() {
		 return durata;
	 }

	 public void setDurata(int durata) {
		 this.durata = durata;
	 }

	public int getQuanti() {
		return quanti;
	}

	public void setQuanti(int quanti) {
		this.quanti = quanti;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}