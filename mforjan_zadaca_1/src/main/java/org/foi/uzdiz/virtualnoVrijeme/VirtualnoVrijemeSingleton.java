package org.foi.uzdiz.virtualnoVrijeme;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.foi.uzdiz.upraviteljGresaka.UpraviteljGresakaSingleton;
import org.foi.uzdiz.uredi.UredZaDostavuSingleton;
import org.foi.uzdiz.uredi.UredZaPrijemSingleton;

public class VirtualnoVrijemeSingleton {
  private static VirtualnoVrijemeSingleton virtualnoVrijeme;

  static Date pocetnoVrijemeVs = null;
  static LocalDateTime localDateTimePocetnoVrijemeVs = null;

  static LocalDateTime pocetakVirtualnogSata = null;

  static Date pocetakRadaTvrtke = null;
  static LocalTime localTimePocetakRadaTvrtke = null;
  static Date krajRadaTvrtke = null;
  static LocalTime localTimeKrajRadaTvrtke = null;

  static Integer vrijednostMs = null;
  static Integer brojSatiRada = null;

  public LocalDateTime trenutnoVirtualnoVrijeme = null;

  private VirtualnoVrijemeSingleton() {}

  public static VirtualnoVrijemeSingleton getInstance() {
    if (virtualnoVrijeme == null) {
      virtualnoVrijeme = new VirtualnoVrijemeSingleton();
    }
    return virtualnoVrijeme;
  }

  public void ucitajPocetnoVrijemeVs(Date ucitanoPocetnoVrijemeVs) {
    pocetnoVrijemeVs = ucitanoPocetnoVrijemeVs;
    trenutnoVirtualnoVrijeme =
        pocetnoVrijemeVs.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  public void ucitajVrijednostMs(int ucitanaVrijednostMs) {
    vrijednostMs = ucitanaVrijednostMs;
  }

  public void ucitajBrojSatiRada(int ucitanBrojSatiRada) {
    brojSatiRada = ucitanBrojSatiRada;
  }

  public void ucitajPocetakRadaTvrtke(Date ucitanPocetakRadaTvrtke) {
    pocetakRadaTvrtke = ucitanPocetakRadaTvrtke;
  }

  public void ucitajKrajRadaTvrtke(Date ucitanKrajRadaTvrtke) {
    krajRadaTvrtke = ucitanKrajRadaTvrtke;
  }

  public LocalDateTime pocetakVremenaVsULocalDateTime(Date pocetnoVrijemeVs) {
    Instant instantPocetnoVrijemeVs = pocetnoVrijemeVs.toInstant();
    localDateTimePocetnoVrijemeVs =
        instantPocetnoVrijemeVs.atZone(ZoneId.systemDefault()).toLocalDateTime();
    return localDateTimePocetnoVrijemeVs;
  }

  public void pocetakRadaTvrtkeULocalTime(Date pocetakRadaTvrtke) {
    Instant instant = pocetakRadaTvrtke.toInstant();
    ZoneId zoneId = ZoneId.systemDefault();
    localTimePocetakRadaTvrtke = instant.atZone(zoneId).toLocalTime();
  }

  public void krajRadaTvrtkeULocalTime(Date krajRadaTvrtke) {
    Instant instant = krajRadaTvrtke.toInstant();
    ZoneId zoneId = ZoneId.systemDefault();
    localTimeKrajRadaTvrtke = instant.atZone(zoneId).toLocalTime();
  }

  public void upravljajVirtualnimVremenom(Date pocetnoVrijemeVs, Date pocetakRadaTvrtke,
      Date krajRadaTvrtke) {
    if (pocetakVirtualnogSata == null) {
      pocetakVirtualnogSata = pocetakVremenaVsULocalDateTime(pocetnoVrijemeVs);
    }

    pocetakRadaTvrtkeULocalTime(pocetakRadaTvrtke);
    krajRadaTvrtkeULocalTime(krajRadaTvrtke);

    LocalDateTime krajVirtualnogSata = pocetakVirtualnogSata.plusHours(brojSatiRada);
    LocalDateTime virtualnoVrijeme = pocetakVirtualnogSata;

    while (virtualnoVrijeme.isBefore(krajVirtualnogSata)
        && virtualnoVrijeme.toLocalTime().isBefore(localTimeKrajRadaTvrtke)) {
      virtualnoVrijeme = virtualnoVrijeme.plusSeconds(1 * vrijednostMs);

      String trenutnoVirtualnoVrijeme =
          DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss").format(virtualnoVrijeme);
      System.out.println("VIRTUALNO VRIJEME: " + trenutnoVirtualnoVrijeme + "\n");
      try {
        UredZaPrijemSingleton.getInstance().primiPaket(virtualnoVrijeme);
        UredZaDostavuSingleton.getInstance().ukrcajPakete(virtualnoVrijeme);
        Thread.sleep(1000);
      } catch (Exception e) {
        UpraviteljGresakaSingleton.getInstance().sustavskaGreska(e);
      }
    }
    provjeraRadnogVremena(virtualnoVrijeme, krajVirtualnogSata);
    trenutnoVirtualnoVrijeme = virtualnoVrijeme;
  }

  private void provjeraRadnogVremena(LocalDateTime virtualnoVrijeme,
      LocalDateTime krajVirtualnogSata) {
    if (virtualnoVrijeme.toLocalTime().isAfter(localTimeKrajRadaTvrtke)) {
      LocalDate virtualniDatum = virtualnoVrijeme.toLocalDate();
      LocalDate sljedeciVirtualniDan = virtualniDatum.plusDays(1);
      virtualnoVrijeme = sljedeciVirtualniDan.atTime(localTimePocetakRadaTvrtke);
      pocetakVirtualnogSata = virtualnoVrijeme;
      System.out.println("RADNO VRIJEME TVRTKE JE ISTEKLO!");
    } else if (virtualnoVrijeme.toLocalTime().isBefore(localTimeKrajRadaTvrtke)) {
      pocetakVirtualnogSata = krajVirtualnogSata;
      System.out.println("VRIJEME RADA DEFINIRANO ARGUMENTOM VR JE ISTEKLO!");
    } else {
      LocalDate virtualniDatum = virtualnoVrijeme.toLocalDate();
      LocalDate sljedeciVirtualniDan = virtualniDatum.plusDays(1);
      virtualnoVrijeme = sljedeciVirtualniDan.atTime(localTimePocetakRadaTvrtke);
      pocetakVirtualnogSata = virtualnoVrijeme;
      System.out.println("RADNO VRIJEME TVRTKE JE ISTEKLO!");
    }
  }
}
