package org.foi.uzdiz.citaci;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.uzdiz.citacDatoteka.CitacDatoteka;
import org.foi.uzdiz.modeli.Vozilo;
import org.foi.uzdiz.tvrtka.TvrtkaSingleton;
import org.foi.uzdiz.upraviteljGresaka.UpraviteljGresakaSingleton;

public class CitacVozila implements CitacDatoteka {

  @Override
  public void ucitajPodatke(String nazivDatoteke) {
    File datoteka = new File(nazivDatoteke);
    try {
      FileInputStream inputStream = new FileInputStream(datoteka);
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

      String prvaLinija = reader.readLine();
      String zapis = "";

      if (!prvaLinija.equals("")) {
        if (prvaLinija
            .equals("Registracija;Opis;Kapacitet težine u kg;Kapacitet prostora u m3;Redoslijed")) {
          while ((zapis = reader.readLine()) != null) {
            String[] atributi = zapis.split(";");
            boolean ispravanRedak = provjeraRetka(atributi, zapis);
            boolean nePostoji = provjeriPostojanje(atributi[0], zapis);
            if (ispravanRedak && nePostoji) {
              ucitajVozilo(atributi);
            }
          }
        } else {
          UpraviteljGresakaSingleton.getInstance().greskaSDatotekama(
              "Informativni redak u datoteci s popisom vozila nije u pravilnom formatu!");
        }
      } else {
        UpraviteljGresakaSingleton.getInstance()
            .greskaSDatotekama("Datoteka s popisom vozila je prazna!");
      }
      reader.close();
    } catch (Exception e) {
      UpraviteljGresakaSingleton.getInstance()
          .greskaSDatotekama("Neuspješno učitavanje datoteke s popisom vozila!");
    }
  }

  private boolean provjeraRetka(String[] atributi, String zapis) {
    if (atributi.length == 5) {
      boolean ispravnaRegistracija = provjeraRegistracije(zapis, atributi[0]);
      boolean ispravanOpis = provjeraOpisa(zapis, atributi[1]);
      boolean ispravanKapacitetTezine = provjeraKapacitetaTezine(zapis, atributi[2]);
      boolean ispravanKapacitetProstora = provjeraKapacitetaProstora(zapis, atributi[3]);
      boolean ispravanRedoslijed = provjeraRedoslijeda(zapis, atributi[4]);

      if (ispravnaRegistracija && ispravanOpis && ispravanKapacitetTezine
          && ispravanKapacitetProstora && ispravanRedoslijed) {
        return true;
      }
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
          "Redak u tablici s popisom vozila ne sadrži traženi broj elemenata!");
    }
    return false;
  }

  private boolean provjeriPostojanje(String registracija, String zapis) {
    for (Vozilo vozilo : TvrtkaSingleton.getInstance().listaVozila) {
      if (registracija.equals(vozilo.dohvatiRegistraciju())) {
        UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
            "Vozilo s ovom registracijskom oznakom već postoji!");
        return false;
      }
    }
    return true;
  }

  private boolean provjeraRegistracije(String zapis, String registracija) {
    if (!registracija.equals("")) {
      Pattern pattern = Pattern.compile("[A-Z0-9ŽŠČĆĐ]+");
      Matcher matcher = pattern.matcher(registracija);
      if (matcher.matches()) {
        return true;
      } else {
        UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
            "Registracija vozila je neispravna!");
      }
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
          "Registracija vozila nije unesena!");
    }
    return false;
  }

  private boolean provjeraOpisa(String zapis, String opis) {
    if (!opis.equals("")) {
      return true;
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis, "Opis vozila nije unesen!");
    }
    return false;
  }

  private boolean provjeraKapacitetaTezine(String zapis, String kapacitetTezine) {
    if (!kapacitetTezine.equals("")) {
      Pattern pattern = Pattern.compile("^\\d+(?:[.,]\\d+)?$");
      Matcher matcher = pattern.matcher(kapacitetTezine);
      if (matcher.matches()) {
        return true;
      } else {
        UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
            "Kapacitet težine vozila je neispravan!");
      }
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
          "Kapacitet težine vozila nije unesen!");
    }
    return false;
  }

  private boolean provjeraKapacitetaProstora(String zapis, String kapacitetProstora) {
    if (!kapacitetProstora.equals("")) {
      Pattern pattern = Pattern.compile("^\\d+(?:[.,]\\d+)?$");
      Matcher matcher = pattern.matcher(kapacitetProstora);
      if (matcher.matches()) {
        return true;
      } else {
        UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
            "Kapacitet prostora vozila je neispravan!");
      }
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
          "Kapacitet prostora vozila nije unesen!");
    }
    return false;
  }

  private boolean provjeraRedoslijeda(String zapis, String redoslijed) {
    if (!redoslijed.equals("")) {
      Pattern pattern = Pattern.compile("[0-9]+");
      Matcher matcher = pattern.matcher(redoslijed);
      if (matcher.matches()) {
        return true;
      } else {
        UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
            "Redoslijed vozila je neispravan!");
      }
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
          "Redoslijed vozila nije unesen!");
    }
    return false;
  }

  private void ucitajVozilo(String[] atributi) {
    String registracija = atributi[0];
    String opis = atributi[1];
    Float kapacitetTezine = Float.parseFloat(atributi[2].replace(",", "."));
    Float kapacitetProstora = Float.parseFloat(atributi[3].replace(",", "."));
    Integer redoslijed = Integer.parseInt(atributi[4]);
    Vozilo vozilo = new Vozilo(registracija, opis, kapacitetTezine, kapacitetProstora, redoslijed);
    TvrtkaSingleton.getInstance().listaVozila.add(vozilo);
  }
}
