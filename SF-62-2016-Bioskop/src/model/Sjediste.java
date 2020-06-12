package model;

public class Sjediste {
	
	private int RedniBroj;
	private Sala sala;
	public int getRedniBroj() {
		return RedniBroj;
	}
	
	public Sjediste() {
	}
	
	
	public Sjediste(int redniBroj, Sala sala) {
		super();
		RedniBroj = redniBroj;
		this.sala = sala;
	}

	public void setRedniBroj(int redniBroj) {
		RedniBroj = redniBroj;
	}
	public Sala getSala() {
		return sala;
	}
	public void setSala(Sala sala) {
		this.sala = sala;
	}
	
	

}
