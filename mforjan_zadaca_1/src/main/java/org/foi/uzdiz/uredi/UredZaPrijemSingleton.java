package org.foi.uzdiz.uredi;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.foi.uzdiz.modeli.Paket;
import org.foi.uzdiz.tvrtka.TvrtkaSingleton;

public class UredZaPrijemSingleton {
  public static UredZaPrijemSingleton uredZaPrijem;

  public Float saldo = 0.0f;

  public List<Paket> listaPrimljenihPaketa = new ArrayList<Paket>();

  private UredZaPrijemSingleton() {

  }

  public static UredZaPrijemSingleton getInstance() {
    if (uredZaPrijem == null) {
      uredZaPrijem = new UredZaPrijemSingleton();
    }
    return uredZaPrijem;
  }

  public void primiPaket(LocalDateTime trenutnoVirtualnoVrijeme) {
    for (Paket paket : TvrtkaSingleton.getInstance().listaPaketa) {
      LocalDateTime localDateTimeVrijemePrijema =
          vrijemePrijemaPaketa(paket.dohvatiVrijemePrijema());

      if (localDateTimeVrijemePrijema.isBefore(trenutnoVirtualnoVrijeme)) {
        boolean postojiPaket = false;

        for (Paket primljeniPaket : listaPrimljenihPaketa) {
          if (paket.dohvatiOznaku().equals(primljeniPaket.dohvatiOznaku())) {
            postojiPaket = true;
            break;
          }
        }
        boolean ispravnaTezina = true;
        if (paket.dohvatiVrstuPaketa().equals("X")) {
          if (paket.dohvatiTezinu() > TvrtkaSingleton.getInstance().dohvatiVrijednostMt()) {
            ispravnaTezina = false;
          }
        }

        if (!postojiPaket && ispravnaTezina) {
          listaPrimljenihPaketa.add(paket);
        }
      }
    }
  }

  private LocalDateTime vrijemePrijemaPaketa(Date vrijemePrijema) {
    Instant instantVrijemePrijema = vrijemePrijema.toInstant();
    LocalDateTime localDateTimeVrijemePrijema =
        instantVrijemePrijema.atZone(ZoneId.systemDefault()).toLocalDateTime();
    return localDateTimeVrijemePrijema;
  }

  public void naplatiIznosDostave(Float iznosDostave) {
    saldo = saldo + iznosDostave;
  }
}
