package org.foi.uzdiz.upraviteljGresaka;

public class UpraviteljGresakaSingleton {
  public static UpraviteljGresakaSingleton upraviteljGresaka;
  private Integer brojacGresaka = 0;
  private Integer brojacGresakaSDatotekama = 0;

  private UpraviteljGresakaSingleton() {

  }

  public static UpraviteljGresakaSingleton getInstance() {
    if (upraviteljGresaka == null) {
      upraviteljGresaka = new UpraviteljGresakaSingleton();
    }
    return upraviteljGresaka;
  }

  public void greskaUlazneKomande(String greska) {
    brojacGresaka++;
    System.out.println("Redni broj greške: " + brojacGresaka + " || Opis: " + greska + "\n");
  }

  public void sustavskaGreska(Exception e) {
    brojacGresaka++;
    System.out
        .println("Redni broj greške: " + brojacGresaka + " || Opis: " + e.getMessage() + "\n");
  }

  public void greskaURadu(String greska) {
    brojacGresaka++;
    System.out.println("Redni broj greške: " + brojacGresaka + " || Opis: " + greska + "\n");
  }

  public void greskaSDatotekama(String greska) {
    brojacGresaka++;
    brojacGresakaSDatotekama++;
    System.out.println("Redni broj greške: " + brojacGresaka + " || Opis: " + greska + "\n");
  }

  public void greskaURetku(String zapis, String greska) {
    brojacGresaka++;
    System.out.println("Redni broj greške: " + brojacGresaka + " || Sadržaj retka: " + zapis
        + " || Opis: " + greska + "\n");
  }

  public int dohvatiBrojGresakaSDatotekama() {
    return brojacGresakaSDatotekama;
  }
}
