package org.foi.uzdiz.konkretniKreatori;

import org.foi.uzdiz.citacDatoteka.CitacDatoteka;
import org.foi.uzdiz.citaci.CitacPaketa;
import org.foi.uzdiz.datotekaFactory.DatotekaFactory;

public class PaketiFactory extends DatotekaFactory {

  @Override
  public CitacDatoteka ucitajDatoteku(String nazivDatoteke) {
    CitacDatoteka product = new CitacPaketa();
    return product;
  }

}
