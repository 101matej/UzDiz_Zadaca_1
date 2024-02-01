package org.foi.uzdiz.konkretniKreatori;

import org.foi.uzdiz.citacDatoteka.CitacDatoteka;
import org.foi.uzdiz.citaci.CitacVozila;
import org.foi.uzdiz.datotekaFactory.DatotekaFactory;

public class VozilaFactory extends DatotekaFactory {

  @Override
  public CitacDatoteka ucitajDatoteku(String nazivDatoteke) {
    CitacDatoteka product = new CitacVozila();
    return product;
  }

}
