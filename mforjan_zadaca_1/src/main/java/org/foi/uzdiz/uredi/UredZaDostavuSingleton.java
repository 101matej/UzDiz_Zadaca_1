package org.foi.uzdiz.uredi;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.foi.uzdiz.modeli.Paket;
import org.foi.uzdiz.modeli.Vozilo;
import org.foi.uzdiz.modeli.VrstaPaketa;
import org.foi.uzdiz.tvrtka.TvrtkaSingleton;

public class UredZaDostavuSingleton {
  public static UredZaDostavuSingleton uredZaDostavu;

  public List<Paket> listaIsporucenihPaketa = new ArrayList<Paket>();
  public ArrayList<String> oznakeIVremenaPreuzimanja = new ArrayList<>();

  private UredZaDostavuSingleton() {

  }

  public static UredZaDostavuSingleton getInstance() {
    if (uredZaDostavu == null) {
      uredZaDostavu = new UredZaDostavuSingleton();
    }
    return uredZaDostavu;
  }

  public void ukrcajPakete(LocalDateTime virtualnoVrijeme) {
    String virtualnoVrijemeString = pretvoriVirtualnoVrijemeUString(virtualnoVrijeme);
    sortirajVozilaPremaRedoslijedu(TvrtkaSingleton.getInstance().listaVozila);
    for (Paket primljeniPaket : UredZaPrijemSingleton.getInstance().listaPrimljenihPaketa) {
      Float volumen = 0.0f;
      Float tezina = 0.0f;
      for (Vozilo vozilo : sortirajVozilaPremaRedoslijedu(
          TvrtkaSingleton.getInstance().listaVozila)) {
        if (primljeniPaket.dohvatiVrstuPaketa().equals("X")) {
          volumen = izracunajVolumenPaketaTipaX(primljeniPaket, volumen);
        } else {
          volumen = izracunajVolumenTipskihPaketa(primljeniPaket, volumen);
        }
        tezina = primljeniPaket.dohvatiTezinu();

        if (provjeriPostojanje(primljeniPaket, vozilo) == true) {
          break;
        }

        if (vozilo.dostavlja == false) {
          if ((vozilo.trenutniProstor + volumen) <= vozilo.dohvatikapacitetProstora()
              && (vozilo.trenutnaTezina + tezina) <= vozilo.dohvatikapacitetTezine()) {
            vozilo.trenutniProstor = vozilo.trenutniProstor + volumen;
            vozilo.trenutnaTezina = vozilo.trenutnaTezina + tezina;
            vozilo.listaUkrcanihPaketa.add(primljeniPaket);
            System.out.println("VIRTUALNO VRIJEME: " + virtualnoVrijemeString + " PAKET S OZNAKOM: "
                + primljeniPaket.dohvatiOznaku() + "JE UKRCAN U VOZILO!\n");
            break;

          } else {
            vozilo.dostavlja = true;
            System.out.println(
                "VIRTUALNO VRIJEME: " + virtualnoVrijemeString + " VOZILO S REGISTARSKOM OZNAKOM: "
                    + vozilo.dohvatiRegistraciju() + " JE KRENULO S DOSTAVOM PAKETA!\n");
            isporuciPakete(vozilo, virtualnoVrijeme);
            naplatiDostavu(vozilo);
          }
        }
      }
    }
  }

  private void isporuciPakete(Vozilo vozilo, LocalDateTime virtualnoVrijeme) {
    int vrijemeIsporuke = TvrtkaSingleton.getInstance().dohvatiVrijednostVi();
    for (Paket paket : vozilo.listaUkrcanihPaketa) {
      Duration intervalIsporuke = Duration.ofMinutes(vrijemeIsporuke);
      virtualnoVrijeme = virtualnoVrijeme.plus(intervalIsporuke);
      String virtualnoVrijemeString = pretvoriVirtualnoVrijemeUString(virtualnoVrijeme);
      oznakeIVremenaPreuzimanja.add(paket.dohvatiOznaku());
      oznakeIVremenaPreuzimanja.add(virtualnoVrijemeString);
      System.out.println("PAKET S OZNAKOM: " + paket.dohvatiOznaku() + " ĆE BITI ISPORUČEN U "
          + virtualnoVrijemeString + "\n");
    }
  }

  private void naplatiDostavu(Vozilo vozilo) {
    for (Paket paket : vozilo.listaUkrcanihPaketa) {
      if (paket.dohvatiUsluguDostave().equals("P")) {
        vozilo.prikupljeniNovac = vozilo.prikupljeniNovac + paket.dohvatiIznosPouzeca();
      }
    }
  }

  private boolean provjeriPostojanje(Paket primljeniPaket, Vozilo vozilo) {
    for (Paket paket : vozilo.listaUkrcanihPaketa) {
      if (primljeniPaket.dohvatiOznaku().equals(paket.dohvatiOznaku())) {
        return true;
      }
    }
    return false;
  }

  private Float izracunajVolumenTipskihPaketa(Paket primljeniPaket, Float volumen) {
    for (VrstaPaketa vrstaPaketa : TvrtkaSingleton.getInstance().listaVrstePaketa) {
      if (vrstaPaketa.dohvatiOznaku().equals(primljeniPaket.dohvatiVrstuPaketa())) {
        volumen =
            vrstaPaketa.dohvatiVisinu() * vrstaPaketa.dohvatiSirinu() * vrstaPaketa.dohvatiDuzinu();
      }
    }
    return volumen;
  }

  private Float izracunajVolumenPaketaTipaX(Paket primljeniPaket, Float volumen) {
    volumen = primljeniPaket.dohvatiVisinu() * primljeniPaket.dohvatiSirinu()
        * primljeniPaket.dohvatiDuzinu();
    return volumen;
  }

  public List<Vozilo> sortirajVozilaPremaRedoslijedu(List<Vozilo> listaVozila) {
    int n = listaVozila.size();
    boolean zamijenjeno;
    do {
      zamijenjeno = false;
      for (int i = 1; i < n; i++) {
        if (listaVozila.get(i - 1).dohvatiRedoslijed() > listaVozila.get(i).dohvatiRedoslijed()) {
          Vozilo temp = listaVozila.get(i - 1);
          listaVozila.set(i - 1, listaVozila.get(i));
          listaVozila.set(i, temp);
          zamijenjeno = true;
        }
      }
    } while (zamijenjeno);

    return listaVozila;
  }

  private String pretvoriVirtualnoVrijemeUString(LocalDateTime virtualnoVrijeme) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
    return virtualnoVrijeme.format(formatter);
  }
}
