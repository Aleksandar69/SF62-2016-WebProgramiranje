package model;

import java.util.Date;

public class Projekcija {
	
	private int id;
	private int idFilma;
	private String tipProjekcije;
	private int idSale;
	private Date datumiVrijemePrikazivanja;
	private int cijenaKarte;
	private String usernameAdministratora;
	private String status;
	private int MaksimumKarata;
	private int ProdaneKarte;
	public int getId() {
		return id;
	}
	

	public Projekcija() {
	}
	
	
	public Projekcija(int id, int idFilma, String tipProjekcije, int idSale, Date datumiVrijemePrikazivanja,
			int cijenaKarte, String usernameAdministratora, String status, int maksimumKarata, int prodaneKarte) {
		super();
		this.id = id;
		this.idFilma = idFilma;
		this.tipProjekcije = tipProjekcije;
		this.idSale = idSale;
		this.datumiVrijemePrikazivanja = datumiVrijemePrikazivanja;
		this.cijenaKarte = cijenaKarte;
		this.usernameAdministratora = usernameAdministratora;
		this.status = status;
		MaksimumKarata = maksimumKarata;
		ProdaneKarte = prodaneKarte;
	}




	public void setId(int id) {
		this.id = id;
	}
	public int getFilm() {
		return idFilma;
	}
	public void setFilm(int idFilma) {
		this.idFilma = idFilma;
	}
	public String getTipProjekcije() {
		return tipProjekcije;
	}
	public void setTipProjekcije(String tipProjekcije) {
		this.tipProjekcije = tipProjekcije;
	}
	public int getSala() {
		return idSale;
	}
	public void setSala(int idSale) {
		this.idSale = idSale;
	}
	public Date getDatumiVrijemePrikazivanja() {
		return datumiVrijemePrikazivanja;
	}
	public void setDatumiVrijemePrikazivanja(Date datumiVrijemePrikazivanja) {
		this.datumiVrijemePrikazivanja = datumiVrijemePrikazivanja;
	}
	public int getCijenaKarte() {
		return cijenaKarte;
	}
	public void setCijenaKarte(int cijenaKarte) {
		this.cijenaKarte = cijenaKarte;
	}
	public String getUsernameAdministratora() {
		return usernameAdministratora;
	}
	public void setUsernameAdministratora(String usernameAdministratora) {
		this.usernameAdministratora = usernameAdministratora;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getMaksimumKarata() {
		return MaksimumKarata;
	}
	public void setMaksimumKarata(int maksimumKarata) {
		MaksimumKarata = maksimumKarata;
	}
	public int getProdaneKarte() {
		return ProdaneKarte;
	}
	public void setProdaneKarte(int prodaneKarte) {
		ProdaneKarte = prodaneKarte;
	}
	
	

}
