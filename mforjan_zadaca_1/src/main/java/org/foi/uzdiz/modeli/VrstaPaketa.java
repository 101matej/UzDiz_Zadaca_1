package org.foi.uzdiz.modeli;

public class VrstaPaketa {
  private String oznaka;
  private String opis;
  private Float visina;
  private Float sirina;
  private Float duzina;
  private Float maksimalnaTezina;
  private Float cijena;
  private Float cijenaHitno;
  private Float cijenaP;
  private Float cijenaT;

  public VrstaPaketa(String oznaka, String opis, Float visina, Float sirina, Float duzina,
      Float maksimalnaTezina, Float cijena, Float cijenaHitno, Float cijenaP, Float cijenaT) {
    this.oznaka = oznaka;
    this.opis = opis;
    this.visina = visina;
    this.sirina = sirina;
    this.duzina = duzina;
    this.maksimalnaTezina = maksimalnaTezina;
    this.cijena = cijena;
    this.cijenaHitno = cijenaHitno;
    this.cijenaP = cijenaP;
    this.cijenaT = cijenaT;
  }

  public String dohvatiOznaku() {
    return oznaka;
  }

  public String dohvatiOpis() {
    return opis;
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

  public Float dohvatiMaksimalnuTezinu() {
    return maksimalnaTezina;
  }

  public Float dohvatiCijenu() {
    return cijena;
  }

  public Float dohvatiCijenuHitno() {
    return cijenaHitno;
  }

  public Float dohvatiCijenuP() {
    return cijenaP;
  }

  public Float dohvatiCijenuT() {
    return cijenaT;
  }
}
