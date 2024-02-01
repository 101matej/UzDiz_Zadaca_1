package org.foi.uzdiz;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.uzdiz.ispis.IspisSingleton;
import org.foi.uzdiz.tvrtka.TvrtkaSingleton;
import org.foi.uzdiz.upraviteljGresaka.UpraviteljGresakaSingleton;
import org.foi.uzdiz.virtualnoVrijeme.VirtualnoVrijemeSingleton;

public class Main {

  static int brojacVp = 0;
  static String datotekaVp = "";

  static int brojacPv = 0;
  static String datotekaPv = "";

  static int brojacPp = 0;
  static String datotekaPp = "";

  static int brojacMt = 0;
  static String stringVrijednostMt = "";
  static int vrijednostMt = 0;

  static int brojacVi = 0;
  static String stringVrijednostVi = "";
  static int vrijednostVi = 0;

  static int brojacVs = 0;
  static String stringVrijednostVs = "";
  static Date vrijednostVs = null;
  static String formatiranoVrijemeVs = "";

  static int brojacMs = 0;
  static String stringVrijednostMs = "";
  static int vrijednostMs = 0;

  static int brojacPr = 0;
  static String stringVrijednostPr = "";
  static Date vrijednostPr = null;
  static String formatiranoVrijemePr = "";

  static int brojacKr = 0;
  static String stringVrijednostKr = "";
  static Date vrijednostKr = null;
  static String formatiranoVrijemeKr = "";

  static int brojSatiRada = 0;

  static boolean ulaznaKomanda = false;

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    Pattern pattern = Pattern.compile(
        "(?<argument1>--(vp|pv|pp|mt|vi|vs|ms|pr|kr)) (?<opis1>[A-Za-zČĆŠŽĐčćšžđ0-9 .:'_-]+ ?)"
            + "(?<argument2>--(vp|pv|pp|mt|vi|vs|ms|pr|kr)) (?<opis2>[A-Za-zČĆŠŽĐčćšžđ0-9 .:'_-]+ ?)"
            + "(?<argument3>--(vp|pv|pp|mt|vi|vs|ms|pr|kr)) (?<opis3>[A-Za-zČĆŠŽĐčćšžđ0-9 .:'_-]+ ?)"
            + "(?<argument4>--(vp|pv|pp|mt|vi|vs|ms|pr|kr)) (?<opis4>[A-Za-zČĆŠŽĐčćšžđ0-9 .:'_-]+ ?)"
            + "(?<argument5>--(vp|pv|pp|mt|vi|vs|ms|pr|kr)) (?<opis5>[A-Za-zČĆŠŽĐčćšžđ0-9 .:'_-]+ ?)"
            + "(?<argument6>--(vp|pv|pp|mt|vi|vs|ms|pr|kr)) (?<opis6>[A-Za-zČĆŠŽĐčćšžđ0-9 .:'_-]+ ?)"
            + "(?<argument7>--(vp|pv|pp|mt|vi|vs|ms|pr|kr)) (?<opis7>[A-Za-zČĆŠŽĐčćšžđ0-9 .:'_-]+ ?)"
            + "(?<argument8>--(vp|pv|pp|mt|vi|vs|ms|pr|kr)) (?<opis8>[A-Za-zČĆŠŽĐčćšžđ0-9 .:'_-]+ ?)"
            + "(?<argument9>--(vp|pv|pp|mt|vi|vs|ms|pr|kr)) (?<opis9>[A-Za-zČĆŠŽĐčćšžđ0-9 .:'_-]+ ?)");

    String spojenaKomanda = String.join(" ", args);

    Matcher matcher = pattern.matcher(spojenaKomanda);

    if (matcher.matches() && ProvjeraKomandeVp(spojenaKomanda) && ProvjeraKomandePv(spojenaKomanda)
        && ProvjeraKomandePp(spojenaKomanda) && ProvjeraKomandeMt(spojenaKomanda)
        && ProvjeraKomandeVi(spojenaKomanda) && ProvjeraKomandeVs(spojenaKomanda)
        && ProvjeraKomandeMs(spojenaKomanda) && ProvjeraKomandePr(spojenaKomanda)
        && ProvjeraKomandeKr(spojenaKomanda)) {
      ulaznaKomanda = true;
      TvrtkaSingleton.getInstance().ucitajDatoteke();
      VirtualnoVrijemeSingleton.getInstance().ucitajPocetnoVrijemeVs(vrijednostVs);
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaUlazneKomande("Ulazna komanda je neispravna!");
      scanner.close();
      return;
    }

    do {
      if (UpraviteljGresakaSingleton.getInstance().dohvatiBrojGresakaSDatotekama() > 0) {
        break;
      }
      switch (spojenaKomanda.split(" ")[0]) {
        case "VR":
          ProvjeraKomandeVr(spojenaKomanda);
          break;
        case "IP":
          LocalDateTime virtualnoVrijeme =
              VirtualnoVrijemeSingleton.getInstance().trenutnoVirtualnoVrijeme;
          IspisSingleton.getInstance().ispisPrimljenihIDostavljenihPaketa(virtualnoVrijeme);
          break;
        default:
          if (!ulaznaKomanda) {
            UpraviteljGresakaSingleton.getInstance()
                .greskaUlazneKomande("Ulazna komanda je neispravna!");
          }
          ulaznaKomanda = false;
      }

      System.out.print("\nUnesite komandu tražene aktivnosti: \n");
      spojenaKomanda = scanner.nextLine();

    } while (!spojenaKomanda.equals("Q"));
    scanner.close();
  }

  private static void ProvjeraKomandeVr(String spojenaKomanda) {
    if (spojenaKomanda.split(" ").length == 2) {
      Pattern patternVr = Pattern.compile("[0-9]|0[0-9]|1[0-9]|2[0-4]");
      Matcher matcherVr = patternVr.matcher(spojenaKomanda.split(" ")[1]);
      if (matcherVr.matches()) {
        brojSatiRada = Integer.parseInt(spojenaKomanda.split(" ")[1]);
        InicijalizacijaVirtualnogSata();
      } else {
        UpraviteljGresakaSingleton.getInstance()
            .greskaURadu("Neispravno definiran broj sati rada!");
      }
    } else {
      UpraviteljGresakaSingleton.getInstance().greskaURadu("Neispravna komanda VR hh!");
    }
  }

  private static void InicijalizacijaVirtualnogSata() {
    VirtualnoVrijemeSingleton.getInstance().ucitajBrojSatiRada(brojSatiRada);
    VirtualnoVrijemeSingleton.getInstance().ucitajVrijednostMs(vrijednostMs);
    VirtualnoVrijemeSingleton.getInstance().ucitajPocetakRadaTvrtke(vrijednostPr);
    VirtualnoVrijemeSingleton.getInstance().ucitajKrajRadaTvrtke(vrijednostKr);
    VirtualnoVrijemeSingleton.getInstance().upravljajVirtualnimVremenom(vrijednostVs, vrijednostPr,
        vrijednostKr);
  }

  private static boolean ProvjeraKomandeVp(String spojenaKomanda) {
    int i = 0;
    String[] poljeStringova = spojenaKomanda.split(" ");
    for (String vp : poljeStringova) {
      if (vp.equals("--vp")) {
        brojacVp++;
        datotekaVp = poljeStringova[i + 1];
        i++;
      } else {
        i++;
      }
    }
    if (ProvjeraDatotekeVp(datotekaVp) && brojacVp == 1) {
      TvrtkaSingleton.getInstance().ucitajDatotekuVp(datotekaVp);
      return true;
    }
    return false;
  }

  private static boolean ProvjeraDatotekeVp(String datotekaVp) {
    Pattern pattern = Pattern.compile("[A-Za-zčćšžđ0-9_-]+\\.csv");
    Matcher matcher = pattern.matcher(datotekaVp);
    if (matcher.matches()) {
      return true;
    } else {
      return false;
    }
  }

  private static boolean ProvjeraKomandePv(String spojenaKomanda) {
    int i = 0;
    String[] poljeStringova = spojenaKomanda.split(" ");
    for (String pv : poljeStringova) {
      if (pv.equals("--pv")) {
        brojacPv++;
        datotekaPv = poljeStringova[i + 1];
        i++;
      } else {
        i++;
      }
    }
    if (ProvjeraDatotekePv(datotekaPv) && brojacPv == 1) {
      TvrtkaSingleton.getInstance().ucitajDatotekuPv(datotekaPv);
      return true;
    }
    return false;
  }

  private static boolean ProvjeraDatotekePv(String datotekaPv) {
    Pattern pattern = Pattern.compile("[A-Za-zčćšžđ0-9_-]+\\.csv");
    Matcher matcher = pattern.matcher(datotekaPv);
    if (matcher.matches()) {
      return true;
    } else {
      return false;
    }
  }

  private static boolean ProvjeraKomandePp(String spojenaKomanda) {
    int i = 0;
    String[] poljeStringova = spojenaKomanda.split(" ");
    for (String pp : poljeStringova) {
      if (pp.equals("--pp")) {
        brojacPp++;
        datotekaPp = poljeStringova[i + 1];
        i++;
      } else {
        i++;
      }
    }
    if (ProvjeraDatotekePp(datotekaPp) && brojacPp == 1) {
      TvrtkaSingleton.getInstance().ucitajDatotekuPp(datotekaPp);
      return true;
    }
    return false;
  }

  private static boolean ProvjeraDatotekePp(String datotekaPp) {
    Pattern pattern = Pattern.compile("[A-Za-zčćšžđ0-9_-]+\\.csv");
    Matcher matcher = pattern.matcher(datotekaPp);
    if (matcher.matches()) {
      return true;
    } else {
      return false;
    }
  }

  private static boolean ProvjeraKomandeMt(String spojenaKomanda) {
    int i = 0;
    String[] poljeStringova = spojenaKomanda.split(" ");
    for (String mt : poljeStringova) {
      if (mt.equals("--mt")) {
        brojacMt++;
        stringVrijednostMt = poljeStringova[i + 1];
        i++;
      } else {
        i++;
      }
    }
    if (ProvjeraVrijednostiMt(stringVrijednostMt) && brojacMt == 1) {
      TvrtkaSingleton.getInstance().ucitajVrijednostMt(vrijednostMt);
      return true;
    }
    return false;
  }

  private static boolean ProvjeraVrijednostiMt(String stringVrijednostMt) {
    Pattern pattern = Pattern.compile("[0-9]+");
    Matcher matcher = pattern.matcher(stringVrijednostMt);
    if (matcher.matches()) {
      vrijednostMt = Integer.parseInt(stringVrijednostMt);
      return true;
    } else {
      return false;
    }
  }

  private static boolean ProvjeraKomandeVi(String spojenaKomanda) {
    int i = 0;
    String[] poljeStringova = spojenaKomanda.split(" ");
    for (String vi : poljeStringova) {
      if (vi.equals("--vi")) {
        brojacVi++;
        stringVrijednostVi = poljeStringova[i + 1];
        i++;
      } else {
        i++;
      }
    }
    if (ProvjeraVrijednostiVi(stringVrijednostVi) && brojacVi == 1) {
      TvrtkaSingleton.getInstance().ucitajVrijednostVi(vrijednostVi);
      return true;
    }
    return false;
  }

  private static boolean ProvjeraVrijednostiVi(String stringVrijednostVi) {
    Pattern pattern = Pattern.compile("[0-9]+");
    Matcher matcher = pattern.matcher(stringVrijednostVi);
    if (matcher.matches()) {
      vrijednostVi = Integer.parseInt(stringVrijednostVi);
      return true;
    } else {
      return false;
    }
  }

  private static boolean ProvjeraKomandeVs(String spojenaKomanda) {
    int i = 0;
    String[] poljeStringova = spojenaKomanda.split(" ");
    for (String vs : poljeStringova) {
      if (vs.equals("--vs")) {
        brojacVs++;
        stringVrijednostVs = poljeStringova[i + 1] + " " + poljeStringova[i + 2];
        i++;
      } else {
        i++;
      }
    }
    if (ProvjeraVrijednostiVs(stringVrijednostVs) && brojacVs == 1) {
      TvrtkaSingleton.getInstance().ucitajVrijednostVs(vrijednostVs);
      return true;
    }
    return false;
  }

  private static boolean ProvjeraVrijednostiVs(String stringVrijednostVs) {
    Pattern pattern = Pattern.compile("(3[01]|[12][0-9]|0[1-9])[.](1[0-2]|0[1-9])[.][0-9]{4}[.] "
        + "(0[0-9]|1[0-9]|2[0-3]):(0[0-9]|[1-5][0-9]):(0[0-9]|[1-5][0-9])");
    Matcher matcher = pattern.matcher(stringVrijednostVs);
    if (matcher.matches()) {
      SimpleDateFormat formatDatuma = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
      try {
        vrijednostVs = formatDatuma.parse(stringVrijednostVs);
        formatiranoVrijemeVs = formatDatuma.format(vrijednostVs);
      } catch (Exception e) {
        UpraviteljGresakaSingleton.getInstance().sustavskaGreska(e);
      }
      return true;
    } else {
      return false;
    }
  }

  private static boolean ProvjeraKomandeMs(String spojenaKomanda) {
    int i = 0;
    String[] poljeStringova = spojenaKomanda.split(" ");
    for (String ms : poljeStringova) {
      if (ms.equals("--ms")) {
        brojacMs++;
        stringVrijednostMs = poljeStringova[i + 1];
        i++;
      } else {
        i++;
      }
    }
    if (ProvjeraVrijednostiMs(stringVrijednostMs) && brojacMs == 1) {
      TvrtkaSingleton.getInstance().ucitajVrijednostMs(vrijednostMs);
      return true;
    }
    return false;
  }

  private static boolean ProvjeraVrijednostiMs(String stringVrijednostMs) {
    Pattern pattern = Pattern.compile("[0-9]+");
    Matcher matcher = pattern.matcher(stringVrijednostMs);
    if (matcher.matches()) {
      vrijednostMs = Integer.parseInt(stringVrijednostMs);
      return true;
    } else {
      return false;
    }
  }

  public static boolean ProvjeraKomandePr(String spojenaKomanda) {
    int i = 0;
    String[] poljeStringova = spojenaKomanda.split(" ");
    for (String pr : poljeStringova) {
      if (pr.equals("--pr")) {
        brojacPr++;
        stringVrijednostPr = poljeStringova[i + 1];
        i++;
      } else {
        i++;
      }
    }
    if (ProvjeraVrijednostiPr(stringVrijednostPr) && brojacPr == 1) {
      TvrtkaSingleton.getInstance().ucitajVrijednostPr(vrijednostPr);
      return true;
    }
    return false;
  }

  public static boolean ProvjeraVrijednostiPr(String stringVrijednostPr) {
    Pattern pattern = Pattern.compile("(0[0-9]|1[0-9]|2[0-3]):(0[0-9]|[1-5][0-9])");
    Matcher matcher = pattern.matcher(stringVrijednostPr);
    if (matcher.matches()) {
      SimpleDateFormat formatDatuma = new SimpleDateFormat("hh:mm");
      try {
        vrijednostPr = formatDatuma.parse(stringVrijednostPr);
        formatiranoVrijemePr = formatDatuma.format(vrijednostPr);
      } catch (Exception e) {
        UpraviteljGresakaSingleton.getInstance().sustavskaGreska(e);
      }
      return true;
    } else {
      return false;
    }
  }

  public static boolean ProvjeraKomandeKr(String spojenaKomanda) {
    int i = 0;
    String[] poljeStringova = spojenaKomanda.split(" ");
    for (String kr : poljeStringova) {
      if (kr.equals("--kr")) {
        brojacKr++;
        stringVrijednostKr = poljeStringova[i + 1];
        i++;
      } else {
        i++;
      }
    }
    if (ProvjeraVrijednostiKr(stringVrijednostKr) && brojacKr == 1) {
      TvrtkaSingleton.getInstance().ucitajVrijednostKr(vrijednostKr);
      return true;
    }
    return false;
  }

  public static boolean ProvjeraVrijednostiKr(String stringVrijednostKr) {
    Pattern pattern = Pattern.compile("(0[0-9]|1[0-9]|2[0-3]):(0[0-9]|[1-5][0-9])");
    Matcher matcher = pattern.matcher(stringVrijednostKr);
    if (matcher.matches()) {
      SimpleDateFormat formatDatuma = new SimpleDateFormat("hh:mm");
      try {
        vrijednostKr = formatDatuma.parse(stringVrijednostKr);
        formatiranoVrijemeKr = formatDatuma.format(vrijednostKr);
      } catch (Exception e) {
        UpraviteljGresakaSingleton.getInstance().sustavskaGreska(e);
      }
      return true;
    } else {
      return false;
    }
  }

}
