package model;

import java.util.ArrayList;

public class Film {
	
	private int id;
	private String naziv;
	private String reziser;
	private String glumci;
	private ArrayList<Zanr> zanrovi;
	private int trajanje;
	private String distributer;
	private String zemljaPorjekla;
	private int godinaProizvodnje;
	private String opis;
	private String status;
	
	public Film() {}
	

	
	public Film(int iD, String naziv, String reziser, String glumci, ArrayList<Zanr> zanrovi, int trajanje, String distributer,
			String zemljaPorjekla, int godinaProizvodnje, String opis) {
		super();
		id = iD;
		this.naziv = naziv;
		this.reziser = reziser;
		this.glumci = glumci;
		this.zanrovi = zanrovi;
		this.trajanje = trajanje;
		this.distributer = distributer;
		this.zemljaPorjekla = zemljaPorjekla;
		this.godinaProizvodnje = godinaProizvodnje;
		this.opis = opis;
	}
	public int getID() {
		return id;
	}
	public void setID(int iD) {
		id = iD;
	}
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public String getReziser() {
		return reziser;
	}
	public void setReziser(String reziser) {
		this.reziser = reziser;
	}
	public String getGlumci() {
		return glumci;
	}
	public void setGlumci(String glumci) {
		this.glumci = glumci;
	}

	public ArrayList<Zanr> getZanrovi() {
		return zanrovi;
	}

	public void setZanrovi(ArrayList<Zanr> zanrovi) {
		this.zanrovi = zanrovi;
	}

	public int getTrajanje() {
		return trajanje;
	}
	public void setTrajanje(int trajanje) {
		this.trajanje = trajanje;
	}
	public String getDistributer() {
		return distributer;
	}
	public void setDistributer(String distributer) {
		this.distributer = distributer;
	}
	public String getZemljaPorjekla() {
		return zemljaPorjekla;
	}
	public void setZemljaPorjekla(String zemljaPorjekla) {
		this.zemljaPorjekla = zemljaPorjekla;
	}
	public int getGodinaProizvodnje() {
		return godinaProizvodnje;
	}
	public void setGodinaProizvodnje(int godinaProizvodnje) {
		this.godinaProizvodnje = godinaProizvodnje;
	}
	public String getOpis() {
		return opis;
	}
	public void setOpis(String opis) {
		this.opis = opis;
	}
	
	
	
	
}
