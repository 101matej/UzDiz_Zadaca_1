package org.foi.uzdiz.datotekaFactory;

import org.foi.uzdiz.citacDatoteka.CitacDatoteka;

public abstract class DatotekaFactory {
  public abstract CitacDatoteka ucitajDatoteku(String nazivDatoteke);
}
