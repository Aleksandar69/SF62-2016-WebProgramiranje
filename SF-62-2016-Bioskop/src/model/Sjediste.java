package model;

public class Sjediste {
	
	private int RedniBroj;
	private int salaId;

	public Sjediste() {
	}
	
	
	public Sjediste(int redniBroj, int salaId) {
		super();
		RedniBroj = redniBroj;
		this.salaId = salaId;
	}

	public void setRedniBroj(int redniBroj) {
		RedniBroj = redniBroj;
	}
	public int getSala() {
		return salaId;
	}
	public void setSala(int salaId) {
		this.salaId = salaId;
	}
	public int getRedniBroj() {
		return RedniBroj;
	}
	
	

}
