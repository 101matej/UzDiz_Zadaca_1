package org.foi.uzdiz.citaci;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.uzdiz.citacDatoteka.CitacDatoteka;
import org.foi.uzdiz.modeli.VrstaPaketa;
import org.foi.uzdiz.tvrtka.TvrtkaSingleton;
import org.foi.uzdiz.upraviteljGresaka.UpraviteljGresakaSingleton;

public class CitacVrstePaketa implements CitacDatoteka {

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
            "Oznaka;Opis;Visina;Širina;Dužina;Maksimalna težina;Cijena;Cijena hitno;CijenaP;CijenaT")) {
          while ((zapis = reader.readLine()) != null) {
            String[] atributi = zapis.split(";");
            boolean ispravanRedak = provjeraRetka(atributi, zapis);
            boolean nePostoji = provjeriPostojanje(atributi[0], zapis);
            if (ispravanRedak && nePostoji) {
              ucitajVrstuPaketa(atributi, zapis);
            }
          }
        } else {
          UpraviteljGresakaSingleton.getInstance().greskaSDatotekama(
              "Informativni redak u datoteci s vrstama paketa nije u pravilnom formatu!");
        }
      } else {
        UpraviteljGresakaSingleton.getInstance()
            .greskaSDatotekama("Datoteka s vrstama paketa je prazna!");
      }
      reader.close();
    } catch (Exception e) {
      UpraviteljGresakaSingleton.getInstance()
          .greskaSDatotekama("Neuspješno učitavanje datoteke s vrstama paketa!");
    }
  }

  private boolean provjeraRetka(String[] atributi, String zapis) {
    if (atributi.length == 10) {
      boolean ispravnaOznaka = provjeraOznake(zapis, atributi[0]);
      boolean ispravanOpis = provjeraOpisa(zapis, atributi[1]);
      boolean ispravnaVisina = provjeraVisine(zapis, atributi[2]);
      boolean ispravnaSirina = provjeraSirine(zapis, atributi[3]);
      boolean ispravnaDuzina = provjeraDuzine(zapis, atributi[4]);
      boolean ispravnaMaksimalnaTezina = provjeraMaksimalneTezine(zapis, atributi[5], atributi[0]);
      boolean ispravnaCijena = provjeraCijene(zapis, atributi[6]);
      boolean ispravnaCijenaHitno = provjeraCijeneHitno(zapis, atributi[7]);
      boolean ispravnaCijenaP = provjeraCijeneP(zapis, atributi[8], atributi[0]);
      boolean ispravnaCijenaT = provjeraCijeneT(zapis, atributi[9], atributi[0]);

      if (ispravnaOznaka && ispravanOpis && ispravnaVisina && ispravnaSirina && ispravnaDuzina
          && ispravnaMaksimalnaTezina && ispravnaCijena && ispravnaCijenaHitno && ispravnaCijenaP
          && ispravnaCijenaT) {
        return true;
      }
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
          "Redak u tablici s vrstama paketa ne sadrži traženi broj elemenata!");
    }
    return false;
  }

  private boolean provjeriPostojanje(String oznaka, String zapis) {
    for (VrstaPaketa vrstaPaketa : TvrtkaSingleton.getInstance().listaVrstePaketa) {
      if (oznaka.equals(vrstaPaketa.dohvatiOznaku())) {
        UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
            "Vrsta paketa s ovom oznakom već postoji!");
        return false;
      }
    }
    return true;
  }

  private boolean provjeraOznake(String zapis, String oznaka) {
    if (!oznaka.equals("")) {
      Pattern pattern = Pattern.compile("A|B|C|D|E|X");
      Matcher matcher = pattern.matcher(oznaka);
      if (matcher.matches()) {
        return true;
      } else {
        UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
            "Oznaka vrste paketa je neispravna!");
      }
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
          "Oznaka vrste paketa nije unesena!");
    }
    return false;
  }

  private boolean provjeraOpisa(String zapis, String opis) {
    if (!opis.equals("")) {
      return true;
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
          "Opis vrste paketa nije unesen!");
    }
    return false;
  }

  private boolean provjeraVisine(String zapis, String visina) {
    if (!visina.equals("")) {
      Pattern pattern = Pattern.compile("^\\d+(?:[.,]\\d+)?$");
      Matcher matcher = pattern.matcher(visina);
      if (matcher.matches()) {
        return true;
      } else {
        UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
            "Visina vrste paketa je neispravna!");
      }
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
          "Visina vrste paketa nije unesena!");
    }
    return false;
  }

  private boolean provjeraSirine(String zapis, String sirina) {
    if (!sirina.equals("")) {
      Pattern pattern = Pattern.compile("^\\d+(?:[.,]\\d+)?$");
      Matcher matcher = pattern.matcher(sirina);
      if (matcher.matches()) {
        return true;
      } else {
        UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
            "Širina vrste paketa je neispravna!");
      }
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
          "Širina vrste paketa nije unesena!");
    }
    return false;
  }

  private boolean provjeraDuzine(String zapis, String duzina) {
    if (!duzina.equals("")) {
      Pattern pattern = Pattern.compile("^\\d+(?:[.,]\\d+)?$");
      Matcher matcher = pattern.matcher(duzina);
      if (matcher.matches()) {
        return true;
      } else {
        UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
            "Dužina vrste paketa je neispravna!");
      }
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
          "Dužina vrste paketa nije unesena!");
    }
    return false;
  }

  private boolean provjeraMaksimalneTezine(String zapis, String maksimalnaTezina, String oznaka) {
    if (!maksimalnaTezina.equals("")) {
      Pattern pattern = Pattern.compile("^\\d+(?:[.,]\\d+)?$");
      Matcher matcher = pattern.matcher(maksimalnaTezina);
      if (matcher.matches()) {
        return true;
      } else {
        UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
            "Maksimalna težina vrste paketa je neispravna!");
      }
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
          "Maksimalna težina vrste paketa nije unesena!");
    }
    return false;
  }

  private boolean provjeraCijene(String zapis, String cijena) {
    if (!cijena.equals("")) {
      Pattern pattern = Pattern.compile("^\\d+(?:[.,]\\d+)?$");
      Matcher matcher = pattern.matcher(cijena);
      if (matcher.matches()) {
        return true;
      } else {
        UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
            "Cijena vrste paketa je neispravna!");
      }
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
          "Cijena vrste paketa nije unesena!");
    }
    return false;
  }

  private boolean provjeraCijeneHitno(String zapis, String cijenaHitno) {
    if (!cijenaHitno.equals("")) {
      Pattern pattern = Pattern.compile("^\\d+(?:[.,]\\d+)?$");
      Matcher matcher = pattern.matcher(cijenaHitno);
      if (matcher.matches()) {
        return true;
      } else {
        UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
            "Cijena hitno vrste paketa je neispravna!");
      }
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
          "Cijena hitno vrste paketa nije unesena!");
    }
    return false;
  }

  private boolean provjeraCijeneP(String zapis, String cijenaP, String oznaka) {
    if (!cijenaP.equals("")) {
      Pattern pattern = Pattern.compile("^\\d+(?:[.,]\\d+)?$");
      Matcher matcher = pattern.matcher(cijenaP);
      if (matcher.matches()) {
        Float decimalnaCijenaP = Float.parseFloat(cijenaP.replace(",", "."));
        if ((oznaka.equals("A") || oznaka.equals("B") || oznaka.equals("C") || oznaka.equals("D")
            || oznaka.equals("E")) && decimalnaCijenaP != 0) {
          UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
              "CijenaP vrste paketa za tipove A, B, C, D i E mora biti 0,0!");
          return false;
        }
        return true;
      } else {
        UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
            "CijenaP vrste paketa je neispravna!");
      }
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
          "CijenaP vrste paketa nije unesena!");
    }
    return false;
  }

  private boolean provjeraCijeneT(String zapis, String cijenaT, String oznaka) {
    if (!cijenaT.equals("")) {
      Pattern pattern = Pattern.compile("^\\d+(?:[.,]\\d+)?$");
      Matcher matcher = pattern.matcher(cijenaT);
      if (matcher.matches()) {
        Float decimalnaCijenaT = Float.parseFloat(cijenaT.replace(",", "."));
        if ((oznaka.equals("A") || oznaka.equals("B") || oznaka.equals("C") || oznaka.equals("D")
            || oznaka.equals("E")) && decimalnaCijenaT != 0) {
          UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
              "CijenaT vrste paketa za tipove A, B, C, D i E mora biti 0,0!");
          return false;
        }
        return true;
      } else {
        UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
            "CijenaT vrste paketa je neispravna!");
      }
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
          "CijenaT vrste paketa nije unesena!");
    }
    return false;
  }

  private void ucitajVrstuPaketa(String[] atributi, String zapis) {
    String oznaka = atributi[0];
    String opis = atributi[1];
    Float visina = Float.parseFloat(atributi[2].replace(",", "."));
    Float sirina = Float.parseFloat(atributi[3].replace(",", "."));
    Float duzina = Float.parseFloat(atributi[4].replace(",", "."));

    if (postaviVrijednostMt(atributi[5], atributi[0], zapis).equals("")) {
      return;
    }
    Float maksimalnaTezina =
        Float.parseFloat(postaviVrijednostMt(atributi[5], atributi[0], zapis).replace(",", "."));

    Float cijena = Float.parseFloat(atributi[6].replace(",", "."));
    Float cijenaHitno = Float.parseFloat(atributi[7].replace(",", "."));
    Float cijenaP = Float.parseFloat(atributi[8].replace(",", "."));
    Float cijenaT = Float.parseFloat(atributi[9].replace(",", "."));

    VrstaPaketa vrstaPaketa = new VrstaPaketa(oznaka, opis, visina, sirina, duzina,
        maksimalnaTezina, cijena, cijenaHitno, cijenaP, cijenaT);
    TvrtkaSingleton.getInstance().listaVrstePaketa.add(vrstaPaketa);
  }

  private String postaviVrijednostMt(String maksimalnaTezina, String oznaka, String zapis) {
    Float decimalnaMaksimalnaTezina = Float.parseFloat(maksimalnaTezina.replace(",", "."));
    if (oznaka.equals("X") && decimalnaMaksimalnaTezina == 0) {
      int stvarnaMaksimalnaTezinaInt = TvrtkaSingleton.getInstance().dohvatiVrijednostMt();
      return postaviVrijednostMt(stvarnaMaksimalnaTezinaInt);
    } else if (oznaka.equals("X") && decimalnaMaksimalnaTezina != 0) {
      UpraviteljGresakaSingleton.getInstance().greskaURetku(zapis,
          "Maksimalna težina vrste paketa za tip X mora biti 0,0!");
      return "";
    } else {
      return maksimalnaTezina;
    }
  }

  private String postaviVrijednostMt(int stvarnaMaksimalnaTezinaInt) {
    String maksimalnaTezinaString = Integer.toString(stvarnaMaksimalnaTezinaInt);
    return maksimalnaTezinaString;
  }

}
