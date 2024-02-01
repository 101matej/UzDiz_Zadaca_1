package org.foi.uzdiz.modeli;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Vozilo {
  private String registracija;
  private String opis;
  private Float kapacitetTezine;
  private Float kapacitetProstora;
  private Integer redoslijed;
  public boolean dostavlja;
  public Float trenutnaTezina;
  public Float trenutniProstor;
  public List<Paket> listaUkrcanihPaketa;
  public LocalDateTime vrijemeDostave;
  public Float prikupljeniNovac;

  public Vozilo(String registracija, String opis, Float kapacitetTezine, Float kapacitetProstora,
      Integer redoslijed) {
    this.registracija = registracija;
    this.opis = opis;
    this.kapacitetTezine = kapacitetTezine;
    this.kapacitetProstora = kapacitetProstora;
    this.redoslijed = redoslijed;
    dostavlja = false;
    trenutniProstor = 0.0f;
    trenutnaTezina = 0.0f;
    listaUkrcanihPaketa = new ArrayList<>();
    vrijemeDostave = null;
    prikupljeniNovac = 0.0f;
  }

  public String dohvatiRegistraciju() {
    return registracija;
  }

  public String dohvatiOpis() {
    return opis;
  }

  public Float dohvatikapacitetTezine() {
    return kapacitetTezine;
  }

  public Float dohvatikapacitetProstora() {
    return kapacitetProstora;
  }

  public Integer dohvatiRedoslijed() {
    return redoslijed;
  }

  public Boolean dohvatiDostavlja() {
    return dostavlja;
  }

  public Float dohvatiTrenutnuTezinu() {
    return trenutnaTezina;
  }

  public Float dohvatiTrenutniProstor() {
    return trenutniProstor;
  }
}
