package model;

import java.util.Date;

public class Projekcija {
	
	private int id;
	private Film film;
	private TipProjekcije tipProjekcije;
	private Sala sala;
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
	
	
	public Projekcija(int id, Film film, TipProjekcije tipProjekcije, Sala sala, Date datumiVrijemePrikazivanja,
			int cijenaKarte, String usernameAdministratora, String status, int maksimumKarata, int prodaneKarte) {
		super();
		this.id = id;
		this.film = film;
		this.tipProjekcije = tipProjekcije;
		this.sala = sala;
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
	public Film getFilm() {
		return film;
	}
	public void setFilm(Film film) {
		this.film = film;
	}
	public TipProjekcije getTipProjekcije() {
		return tipProjekcije;
	}
	public void setTipProjekcije(TipProjekcije tipProjekcije) {
		this.tipProjekcije = tipProjekcije;
	}
	public Sala getSala() {
		return sala;
	}
	public void setSala(Sala sala) {
		this.sala = sala;
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
