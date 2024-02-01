package org.foi.uzdiz.konkretniKreatori;

import org.foi.uzdiz.citacDatoteka.CitacDatoteka;
import org.foi.uzdiz.citaci.CitacVrstePaketa;
import org.foi.uzdiz.datotekaFactory.DatotekaFactory;

public class VrstePaketaFactory extends DatotekaFactory {

  @Override
  public CitacDatoteka ucitajDatoteku(String nazivDatoteke) {
    CitacDatoteka product = new CitacVrstePaketa();
    return product;
  }

}
