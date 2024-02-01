package org.foi.uzdiz.ispis;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.foi.uzdiz.modeli.Paket;
import org.foi.uzdiz.modeli.VrstaPaketa;
import org.foi.uzdiz.tvrtka.TvrtkaSingleton;
import org.foi.uzdiz.uredi.UredZaDostavuSingleton;
import org.foi.uzdiz.uredi.UredZaPrijemSingleton;

public class IspisSingleton {
  public static IspisSingleton ispis;

  private IspisSingleton() {

  }

  public static IspisSingleton getInstance() {
    if (ispis == null) {
      ispis = new IspisSingleton();
    }
    return ispis;
  }

  public void ispisPrimljenihIDostavljenihPaketa(LocalDateTime virtualnoVrijeme) {
    LocalDateTime virtualnoVrijemeIspis = virtualnoVrijeme;
    ispisPrimljenihPaketa(virtualnoVrijemeIspis);
  }

  private void ispisPrimljenihPaketa(LocalDateTime virtualnoVrijeme) {
    String virtualnoVrijemeString = virtualnoVrijemeUString(virtualnoVrijeme);
    System.out.println("\nTRENUTNO VRIJEME VIRTUALNOG SATA: " + virtualnoVrijemeString + "\n");
    ispisiZaglavlje();

    for (Paket paket : UredZaPrijemSingleton.getInstance().listaPrimljenihPaketa) {
      SimpleDateFormat formatDatuma = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
      String vrijemePrijema = formatDatuma.format(paket.dohvatiVrijemePrijema());

      Float cijenaDostave = 0.0f;
      cijenaDostave = definirajCijenuDostave(paket, cijenaDostave);
      DecimalFormat decimalFormat = new DecimalFormat("0.00");
      String formatiranaCijenaDostave = decimalFormat.format(cijenaDostave);

      String vrijemePreuzimanja = provjeraOznakeIVremenaPreuzimanja(paket);
      String statusIsporuke = "PRIMLJENA";
      if (!vrijemePreuzimanja.equals("")) {
        LocalDateTime vrijemePreuzimanjaLocalDateTime =
            vrijemePreuzimanjaULocalDateTime(vrijemePreuzimanja);
        statusIsporuke = definirajStatusIsporuke(vrijemePreuzimanjaLocalDateTime, virtualnoVrijeme);
      }
      Float iznosPouzeca = paket.dohvatiIznosPouzeca();

      String redak = String.format(
          "|   %-20s   |   %-20s   |   %-20s   |   %-20s   |   %-20s   |   %-20s   |   %-20s   |",
          vrijemePrijema, paket.dohvatiVrstuPaketa(), paket.dohvatiUsluguDostave(), statusIsporuke,
          vrijemePreuzimanja, formatiranaCijenaDostave, iznosPouzeca);
      System.out.println(redak);
    }
    System.out.println(
        "--------------------------------------------------------------------------------------------"
            + "--------------------------------------------------------------------------------------------------");
  }

  private String provjeraOznakeIVremenaPreuzimanja(Paket paket) {
    List<String> lista = UredZaDostavuSingleton.getInstance().oznakeIVremenaPreuzimanja;
    String vrijemePreuzimanja = "";
    for (int i = 0; i < lista.size(); i++) {
      String oznaka = lista.get(i);

      if (oznaka.equals(paket.dohvatiOznaku())) {
        if (i < lista.size() - 1) {
          vrijemePreuzimanja = lista.get(i + 1);
        }
      }
    }
    return vrijemePreuzimanja;
  }

  private void ispisiZaglavlje() {
    System.out.println(
        "--------------------------------------------------------------------------------------------"
            + "--------------------------------------------------------------------------------------------------");
    System.out
        .println("|      Vrijeme prijema     |       Vrsta paketa       |       Vrsta usluge       "
            + "|      Status isporuke     |    Vrijeme preuzimanja   |      Cijena dostave      |      Iznos pouzeća       |");
    System.out.println(
        "--------------------------------------------------------------------------------------------"
            + "--------------------------------------------------------------------------------------------------");
  }

  private String virtualnoVrijemeUString(LocalDateTime virtualnoVrijeme) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.mm.yyyy. hh:mm:ss");
    String virtualnoVrijemeString = virtualnoVrijeme.format(formatter);
    return virtualnoVrijemeString;
  }

  private LocalDateTime vrijemePreuzimanjaULocalDateTime(String vrijemePreuzimanja) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
    LocalDateTime vrijemePreuzimanjaLocalDateTime =
        LocalDateTime.parse(vrijemePreuzimanja, formatter);
    return vrijemePreuzimanjaLocalDateTime;
  }

  private String definirajStatusIsporuke(LocalDateTime vrijemePreuzimanjaLocalDateTime,
      LocalDateTime virtualnoVrijeme) {
    if (vrijemePreuzimanjaLocalDateTime.isBefore(virtualnoVrijeme)) {
      return "DOSTAVLJENA";
    } else {
      return "ISPORUČENA";
    }
  }

  private Float definirajCijenuDostave(Paket paket, Float cijenaDostave) {
    for (VrstaPaketa vrstaPaketa : TvrtkaSingleton.getInstance().listaVrstePaketa) {
      if (paket.dohvatiVrstuPaketa().equals(vrstaPaketa.dohvatiOznaku())) {
        if (paket.dohvatiVrstuPaketa().equals("X")) {
          if (paket.dohvatiUsluguDostave().equals("H")) {
            Float volumen = izracunajVolumen(paket.dohvatiVisinu(), paket.dohvatiSirinu(),
                paket.dohvatiDuzinu());
            Float umnozakCijenaPIVolumen = vrstaPaketa.dohvatiCijenuP() * volumen;
            Float umnozakCijenaTITezina = vrstaPaketa.dohvatiCijenuT() * paket.dohvatiTezinu();
            cijenaDostave =
                vrstaPaketa.dohvatiCijenuHitno() + umnozakCijenaPIVolumen + umnozakCijenaTITezina;
          } else if (paket.dohvatiUsluguDostave().equals("S")
              || paket.dohvatiUsluguDostave().equals("H")
              || paket.dohvatiUsluguDostave().equals("R")) {
            Float volumen = izracunajVolumen(paket.dohvatiVisinu(), paket.dohvatiSirinu(),
                paket.dohvatiDuzinu());
            Float umnozakCijenaPIVolumen = vrstaPaketa.dohvatiCijenuP() * volumen;
            Float umnozakCijenaTITezina = vrstaPaketa.dohvatiCijenuT() * paket.dohvatiTezinu();
            cijenaDostave =
                vrstaPaketa.dohvatiCijenu() + umnozakCijenaPIVolumen + umnozakCijenaTITezina;
          } else {
            cijenaDostave = 0.0f;
          }
        } else {
          if (paket.dohvatiUsluguDostave().equals("H")) {
            cijenaDostave = vrstaPaketa.dohvatiCijenuHitno();
          } else if (paket.dohvatiUsluguDostave().equals("S")
              || paket.dohvatiUsluguDostave().equals("H")
              || paket.dohvatiUsluguDostave().equals("R")) {
            cijenaDostave = vrstaPaketa.dohvatiCijenu();
          } else {
            cijenaDostave = 0.0f;
          }
        }
      }
    }
    UredZaPrijemSingleton.getInstance().naplatiIznosDostave(cijenaDostave);
    return cijenaDostave;
  }

  private Float izracunajVolumen(Float visina, Float sirina, Float duzina) {
    return visina * sirina * duzina;
  }
}
