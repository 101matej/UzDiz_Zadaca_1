package org.foi.uzdiz.citaci;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.uzdiz.citacDatoteka.CitacDatoteka;
import org.foi.uzdiz.modeli.Paket;
import org.foi.uzdiz.tvrtka.TvrtkaSingleton;
import org.foi.uzdiz.upraviteljGresaka.UpraviteljGresakaSingleton;

public class CitacPaketa implements CitacDatoteka {

  @Override
  public void ucitajPodatke(String nazivDatoteke) {
    File datoteka = new File(nazivDatoteke);
    try {
      FileInputStream inputStream = new FileInputStream(datoteka);
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

      String prvaLinija = reader.readLine();
      String zapis = "";

      if (!prvaLinija.equals("")) {
        if (prvaLinija.equals(
            "Oznaka;Vrijeme prijema;Pošiljatelj;Primatelj;Vrsta paketa;Visina;Širina;Dužina;Težina;Usluga dostave;Iznos pouzeća")) {
          while ((zapis = reader.readLine()) != null) {
            String[] atributi = zapis.split(";");
            boolean ispravanRedak = provjeraRetka(atributi, zapis);
            boolean nePostoji = provjeriPostojanje(atributi[0], zapis);
            if (ispravanRedak && nePostoji) {
              ucitajPaket(atributi);
            }
          }
        } else {
          UpraviteljGresakaSingleton.getInstance().greskaSDatotekama(
              "Informativni redak u datoteci s paketima nije u pravilnom formatu!");
        }
      } else {
        UpraviteljGresakaSingleton.getInstance()
            .greskaSDatotekama("Datoteka s paketima je prazna!");
      }
      reader.close();
    } catch (Exception e) {
      UpraviteljGresakaSingleton.getInstance()
          .greskaSDatotekama("Neuspješno učitavanje datoteke s paketima!");
    }
  }

  private boolean provjeraRetka(String[] atributi, String zapis) {
    if (atributi.length == 11) {
      boolean ispravnaOznaka = provjeraOznake(zapis, atributi[0]);
      boolean ispravnoVrijemePrijema = provjeraVremenaPrijema(zapis, atributi[1]);
      boolean ispravanPosiljatelj = provjeraPosiljatelja(zapis, atributi[2]);
      boolean ispravanPrimatelj = provjeraPrimatelja(zapis, atributi[3]);
      boolean ispravnaVrstaPaketa = provjeraVrstePaketa(zapis, atributi[4]);
      boolean ispravnaVisina = provjeraVisine(zapis, atributi[5], atributi[4]);
      boolean ispravnaSirina = provjeraSirine(zapis, atributi[6], atributi[4]);
      boolean ispravnaDuzina = provjeraDuzine(zapis, atributi[7], atributi[4]);
      boolean ispravnaTezina = provjeraTezine(zapis, atributi[8]);
      boolean ispravnaUslugaDostave = provjeraUslugeDostave(zapis, atributi[9]);
      boolean ispravanIznosPouzeca = provjeraIznosaPouzeca(zapis, atributi[10], atributi[9]);

      if (ispravnaOznaka && ispravnoVrijemePrijema && ispravanPosiljatelj && ispravanPrimatelj
          && ispravnaVrstaPaketa && ispravnaVisina && ispravnaSirina && ispravnaDuzina
          && ispravnaTezina && ispravnaUslugaDostave && ispravanIznosPouzeca) {
        return true;
      }
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
          "Redak u tablici s paketima ne sadrži traženi broj elemenata!");
    }
    return false;
  }

  private boolean provjeriPostojanje(String oznaka, String zapis) {
    for (Paket paket : TvrtkaSingleton.getInstance().listaPaketa) {
      if (oznaka.equals(paket.dohvatiOznaku())) {
        UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
            "Paket s ovom oznakom već postoji!");
        return false;
      }
    }
    return true;
  }

  private boolean provjeraOznake(String zapis, String oznaka) {
    if (!oznaka.equals("")) {
      return true;
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis, "Oznaka paketa nije unesena!");
    }
    return false;
  }

  private boolean provjeraVremenaPrijema(String zapis, String vrijemePrijema) {
    if (!vrijemePrijema.equals("")) {
      Pattern pattern = Pattern.compile(
          "(3[01]|[12][0-9]|0[1-9])[.](1[0-2]|0[1-9])[.][0-9]{4}[.] (0[0-9]|1[0-9]|2[0-3]):(0[0-9]|[1-5][0-9]):(0[0-9]|[1-5][0-9])");
      Matcher matcher = pattern.matcher(vrijemePrijema);
      if (matcher.matches()) {
        return true;
      } else {
        UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
            "Vrijeme prijema paketa je neispravno!");
      }
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
          "Vrijeme prijema paketa nije uneseno!");
    }
    return false;
  }

  private boolean provjeraPosiljatelja(String zapis, String posiljatelj) {
    if (!posiljatelj.equals("")) {
      return true;
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
          "Pošiljatelj paketa nije unesen!");
    }
    return false;
  }


  private boolean provjeraPrimatelja(String zapis, String primatelj) {
    if (!primatelj.equals("")) {
      return true;
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis, "Primatelj paketa nije unesen!");
    }
    return false;
  }

  private boolean provjeraVrstePaketa(String zapis, String vrstaPaketa) {
    if (!vrstaPaketa.equals("")) {
      Pattern pattern = Pattern.compile("A|B|C|D|E|X");
      Matcher matcher = pattern.matcher(vrstaPaketa);
      if (matcher.matches()) {
        return true;
      } else {
        UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis, "Vrsta paketa je neispravna!");
      }
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis, "Vrsta paketa nije unesena!");
    }
    return false;
  }

  private boolean provjeraVisine(String zapis, String visina, String vrstaPaketa) {
    if (!visina.equals("")) {
      Pattern pattern = Pattern.compile("^\\d+(?:[.,]\\d+)?$");
      Matcher matcher = pattern.matcher(visina);
      if (matcher.matches()) {
        Float decimalnaVisina = Float.parseFloat(visina.replace(",", "."));
        if ((vrstaPaketa.equals("A") || vrstaPaketa.equals("B") || vrstaPaketa.equals("C")
            || vrstaPaketa.equals("D") || vrstaPaketa.equals("E")) && decimalnaVisina != 0) {
          UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
              "Visina paketa za tipove A, B, C, D i E mora biti 0,0!");
          return false;
        }
        return true;
      } else {
        UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
            "Visina paketa je neispravna!");
      }
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis, "Visina paketa nije unesena!");
    }
    return false;
  }

  private boolean provjeraSirine(String zapis, String sirina, String vrstaPaketa) {
    if (!sirina.equals("")) {
      Pattern pattern = Pattern.compile("^\\d+(?:[.,]\\d+)?$");
      Matcher matcher = pattern.matcher(sirina);
      if (matcher.matches()) {
        Float decimalnaSirina = Float.parseFloat(sirina.replace(",", "."));
        if ((vrstaPaketa.equals("A") || vrstaPaketa.equals("B") || vrstaPaketa.equals("C")
            || vrstaPaketa.equals("D") || vrstaPaketa.equals("E")) && decimalnaSirina != 0) {
          UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
              "Širina paketa za tipove A, B, C, D i E mora biti 0,0!");
          return false;
        }
        return true;
      } else {
        UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
            "Širina paketa je neispravna!");
      }
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis, "Širina paketa nije unesena!");
    }
    return false;
  }

  private boolean provjeraDuzine(String zapis, String duzina, String vrstaPaketa) {
    if (!duzina.equals("")) {
      Pattern pattern = Pattern.compile("^\\d+(?:[.,]\\d+)?$");
      Matcher matcher = pattern.matcher(duzina);
      if (matcher.matches()) {
        Float decimalnaDuzina = Float.parseFloat(duzina.replace(",", "."));
        if ((vrstaPaketa.equals("A") || vrstaPaketa.equals("B") || vrstaPaketa.equals("C")
            || vrstaPaketa.equals("D") || vrstaPaketa.equals("E")) && decimalnaDuzina != 0) {
          UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
              "Dužina paketa za tipove A, B, C, D i E mora biti 0,0!");
          return false;
        }
        return true;
      } else {
        UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
            "Dužina paketa je neispravna!");
      }
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis, "Dužina paketa nije unesena!");
    }
    return false;
  }

  private boolean provjeraTezine(String zapis, String tezina) {
    if (!tezina.equals("")) {
      Pattern pattern = Pattern.compile("^\\d+(?:[.,]\\d+)?$");
      Matcher matcher = pattern.matcher(tezina);
      if (matcher.matches()) {
        return true;
      } else {
        UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
            "Težina paketa je neispravna!");
      }
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis, "Težina paketa nije unesena!");
    }
    return false;
  }

  private boolean provjeraUslugeDostave(String zapis, String uslugaDostave) {
    if (!uslugaDostave.equals("")) {
      Pattern pattern = Pattern.compile("S|H|P|R");
      Matcher matcher = pattern.matcher(uslugaDostave);
      if (matcher.matches()) {
        return true;
      } else {
        UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
            "Usluga dostave paketa je neispravna!");
      }
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
          "Usluga dostave paketa nije unesena!");
    }
    return false;
  }

  private boolean provjeraIznosaPouzeca(String zapis, String iznosPouzeca, String uslugaDostave) {
    if (!iznosPouzeca.equals("")) {
      Pattern pattern = Pattern.compile("^\\d+(?:[.,]\\d+)?$");
      Matcher matcher = pattern.matcher(iznosPouzeca);
      if (matcher.matches()) {
        Float decimalniIznosPouzeca = Float.parseFloat(iznosPouzeca.replace(",", "."));
        if ((uslugaDostave.equals("S") || uslugaDostave.equals("H") || uslugaDostave.equals("R"))
            && decimalniIznosPouzeca != 0) {
          UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
              "Iznos pouzeća paketa za usluge dostave S, H i R mora biti 0,0!");
          return false;
        }
        return true;
      } else {
        UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
            "Iznos pouzeća paketa je neispravan!");
      }
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
          "Iznos pouzeća paketa nije unesen!");
    }
    return false;
  }

  private void ucitajPaket(String[] atributi) {
    String oznaka = atributi[0];
    String stringVrijemePrijema = atributi[1];
    Date dateVrijemePrijema = null;

    SimpleDateFormat formatDatuma = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
    try {
      dateVrijemePrijema = formatDatuma.parse(stringVrijemePrijema);
    } catch (Exception e) {
      UpraviteljGresakaSingleton.getInstance().sustavskaGreska(e);
    }

    String posiljatelj = atributi[2];
    String primatelj = atributi[3];
    String vrstaPaketa = atributi[4];
    Float visina = Float.parseFloat(atributi[5].replace(",", "."));
    Float sirina = Float.parseFloat(atributi[6].replace(",", "."));
    Float duzina = Float.parseFloat(atributi[7].replace(",", "."));
    Float tezina = Float.parseFloat(atributi[8].replace(",", "."));
    String uslugaDostave = atributi[9];
    Float iznosPouzeca = Float.parseFloat(atributi[10].replace(",", "."));
    Paket paket = new Paket(oznaka, dateVrijemePrijema, posiljatelj, primatelj, vrstaPaketa, visina,
        sirina, duzina, tezina, uslugaDostave, iznosPouzeca);
    TvrtkaSingleton.getInstance().listaPaketa.add(paket);

    sortirajListuPoVremenuPrijema(TvrtkaSingleton.getInstance().listaPaketa);
  }

  private void sortirajListuPoVremenuPrijema(List<Paket> listaPaketa) {
    int n = listaPaketa.size();
    for (int i = 0; i < n - 1; i++) {
      for (int j = 0; j < n - i - 1; j++) {
        Paket paket1 = listaPaketa.get(j);
        Paket paket2 = listaPaketa.get(j + 1);
        if (paket1.dohvatiVrijemePrijema().compareTo(paket2.dohvatiVrijemePrijema()) > 0) {
          listaPaketa.set(j, paket2);
          listaPaketa.set(j + 1, paket1);
        }
      }
    }
  }
}
