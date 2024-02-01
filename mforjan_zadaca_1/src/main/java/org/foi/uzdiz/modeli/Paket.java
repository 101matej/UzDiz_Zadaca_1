package org.foi.uzdiz.modeli;

import java.util.Date;

public class Paket {
  private String oznaka;
  private Date vrijemePrijema;
  private String posiljatelj;
  private String primatelj;
  private String vrstaPaketa;
  private Float visina;
  private Float sirina;
  private Float duzina;
  private Float tezina;
  private String uslugaDostave;
  private Float iznosPouzeca;

  public Paket(String oznaka, Date vrijemePrijema, String posiljatelj, String primatelj,
      String vrstaPaketa, Float visina, Float sirina, Float duzina, Float tezina,
      String uslugaDostave, Float iznosPouzeca) {
    this.oznaka = oznaka;
    this.vrijemePrijema = vrijemePrijema;
    this.posiljatelj = posiljatelj;
    this.primatelj = primatelj;
    this.vrstaPaketa = vrstaPaketa;
    this.visina = visina;
    this.sirina = sirina;
    this.duzina = duzina;
    this.tezina = tezina;
    this.uslugaDostave = uslugaDostave;
    this.iznosPouzeca = iznosPouzeca;
  }

  public String dohvatiOznaku() {
    return oznaka;
  }

  public Date dohvatiVrijemePrijema() {
    return vrijemePrijema;
  }

  public String dohvatiPosiljatelja() {
    return posiljatelj;
  }

  public String dohvatiPrimatelja() {
    return primatelj;
  }

  public String dohvatiVrstuPaketa() {
    return vrstaPaketa;
  }

  public Float dohvatiVisinu() {
    return visina;
  }

  public Float dohvatiSirinu() {
    return sirina;
  }

  public Float dohvatiDuzinu() {
    return duzina;
  }

  public Float dohvatiTezinu() {
    return tezina;
  }

  public String dohvatiUsluguDostave() {
    return uslugaDostave;
  }

  public Float dohvatiIznosPouzeca() {
    return iznosPouzeca;
  }
}
