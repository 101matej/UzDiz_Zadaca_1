package org.foi.uzdiz.tvrtka;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.foi.uzdiz.citacDatoteka.CitacDatoteka;
import org.foi.uzdiz.konkretniKreatori.PaketiFactory;
import org.foi.uzdiz.konkretniKreatori.VozilaFactory;
import org.foi.uzdiz.konkretniKreatori.VrstePaketaFactory;
import org.foi.uzdiz.modeli.Paket;
import org.foi.uzdiz.modeli.Vozilo;
import org.foi.uzdiz.modeli.VrstaPaketa;
import org.foi.uzdiz.upraviteljGresaka.UpraviteljGresakaSingleton;

public class TvrtkaSingleton {
  public static TvrtkaSingleton tvrtka;

  static String datotekaVp = "";
  static String datotekaPv = "";
  static String datotekaPp = "";
  static int vrijednostMt = 0;
  static int vrijednostVi = 0;
  static Date vrijednostVs = null;
  static String formatiranoVrijemeVs = "";
  static int vrijednostMs = 0;
  static Date vrijednostPr = null;
  static String formatiranoVrijemePr = "";
  static Date vrijednostKr = null;
  static String formatiranoVrijemeKr = "";

  public List<Vozilo> listaVozila = new ArrayList<Vozilo>();
  public List<VrstaPaketa> listaVrstePaketa = new ArrayList<VrstaPaketa>();
  public List<Paket> listaPaketa = new ArrayList<Paket>();

  private TvrtkaSingleton() {

  }

  public static TvrtkaSingleton getInstance() {
    if (tvrtka == null) {
      tvrtka = new TvrtkaSingleton();
    }
    return tvrtka;
  }

  public void ucitajDatoteke() {
    ucitajVozila();
    ucitajVrstePaketa();
    ucitajPakete();
  }

  private void ucitajVozila() {
    CitacDatoteka datotekaVozila = new VozilaFactory().ucitajDatoteku(datotekaPv);
    if (datotekaVozila != null) {
      datotekaVozila.ucitajPodatke(datotekaPv);
    } else {
      UpraviteljGresakaSingleton.getInstance()
          .greskaSDatotekama("Neuspješno učitavanje datoteke s popisom vozila!");
    }
  }

  private void ucitajVrstePaketa() {
    CitacDatoteka datotekaVrstaPaketa = new VrstePaketaFactory().ucitajDatoteku(datotekaVp);
    if (datotekaVrstaPaketa != null) {
      datotekaVrstaPaketa.ucitajPodatke(datotekaVp);
    } else {
      UpraviteljGresakaSingleton.getInstance()
          .greskaSDatotekama("Neuspješno učitavanje datoteke s vrstama paketa!");
    }
  }

  private void ucitajPakete() {
    CitacDatoteka datotekaPaketa = new PaketiFactory().ucitajDatoteku(datotekaPp);
    if (datotekaPaketa != null) {
      datotekaPaketa.ucitajPodatke(datotekaPp);
    } else {
      UpraviteljGresakaSingleton.getInstance()
          .greskaSDatotekama("Neuspješno učitavanje datoteke s paketima!");
    }
  }

  public void ucitajDatotekuVp(String ucitanaDatotekaVp) {
    datotekaVp = ucitanaDatotekaVp;
  }

  public String dohvatiDatotekuVp() {
    return datotekaVp;
  }

  public void ucitajDatotekuPv(String ucitanaDatotekaPv) {
    datotekaPv = ucitanaDatotekaPv;
  }

  public String dohvatiDatotekuPv() {
    return datotekaPv;
  }

  public void ucitajDatotekuPp(String ucitanaDatotekaPp) {
    datotekaPp = ucitanaDatotekaPp;
  }

  public String dohvatiDatotekuPp() {
    return datotekaPp;
  }

  public void ucitajVrijednostMt(int ucitanaVrijednostMt) {
    vrijednostMt = ucitanaVrijednostMt;
  }

  public int dohvatiVrijednostMt() {
    return vrijednostMt;
  }

  public void ucitajVrijednostVi(int ucitanaVrijednostVi) {
    vrijednostVi = ucitanaVrijednostVi;
  }

  public int dohvatiVrijednostVi() {
    return vrijednostVi;
  }

  public void ucitajVrijednostVs(Date ucitanaVrijednostVs) {
    vrijednostVs = ucitanaVrijednostVs;
  }

  public Date dohvatiVrijednostVs() {
    return vrijednostVs;
  }

  public void ucitajVrijednostMs(int ucitanaVrijednostMs) {
    vrijednostMs = ucitanaVrijednostMs;
  }

  public int dohvatiVrijednostMs() {
    return vrijednostMs;
  }

  public void ucitajVrijednostPr(Date ucitanaVrijednostPr) {
    vrijednostPr = ucitanaVrijednostPr;
  }

  public Date dohvatiVrijednostPr() {
    return vrijednostPr;
  }

  public void ucitajVrijednostKr(Date ucitanaVrijednostKr) {
    vrijednostKr = ucitanaVrijednostKr;
  }

  public Date dohvatiVrijednostKr() {
    return vrijednostKr;
  }
}
