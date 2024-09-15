package org.limmen.playtrans.api;

public enum Source {

  ARROW_CAZ("caz", "Arrow Caz"),
  ARROW("arrow", "Arrow Classic Rock"),
  FRESH("fresh", "FreshFM"),
  FUNX("funx", "Fun X"),
  JOY("joyradio", "Joy Radio"),
  JUIZE("juize", "Juize"),
  KINK_DISTORTION("kink-distortion", "KINK Distortion"),
  KINK_DNA("kink-dna", "KINK DNA"),
  KINK_INDIE("kink-indie", "KINK Indie"),
  KINK("kink", "KINK"),
  QMUSIC_FOUT("qmusic-foute-uur", "Q Music Foute uur Non-Stop"),
  QMUSIC("qmusic", "Q Music"),
  QMUSIC_NONSTOP("qmusic-nonstop", "Q Music Non-Stop"),
  PINGUIN("pinguin", "Pinguin Radio"),
  RADIO_DECIBEL("decibel", "Radio Decibel"),
  RADIO1("radio1", "Radio 1"),
  RADIO10("radio10", "Radio 10"),
  RADIO10_CLASSICS("radio10-60s-70s-hits", "Radio 10 60's & 70's hits"),
  RADIO100NL("100p", "100% NL"),
  RADIO2("radio2", "Radio 2"),
  RADIO3("3fm", "Radio 3 FM"),
  RADIO3FMALT("3fm-alternative", "Radio 3FM Alternative"),
  RADIO3FMKX("3fm-kx", "Radio 3FM KX"),
  RADIO5("radio5", "Radio 5"),
  RADIO538_DANCE("538-dance-department", "538 Dance Department"),
  RADIO538_HIT("538-hitzone", "538 Hitzone"),
  RADIO538_PARTY("538-party", "538 Party"),
  RADIO538("538", "538"),
  RADIO8FM("radio8fmzuid", "Radio 8FM"),
  RADIONL("radionl", "Radio NL"),
  RADIO_WAVE("wave", "Radio Wave"),
  SIMONEFM("simonefm", "Simone FM"),
  SKY("skyradio", "SKY Radio"),
  SLAMFM("slamfm", "SLAM!"),
  SLAMFM_HARDSTYLE("slam-hardstyle", "SLAM! Hardstyle"),
  SUBLIMEFM("sublimefm", "Sublime FM"),
  VERONICA("veronica", "Veronica"),
  WILDFM("wildfm", "Wild FM");

  Source(String source, String label) {
    this.source = source;
    this.label = label;
  }

  private String source;
  private String label;

  public String getLabel() {
    return label;
  }

  public String getSource() {
    return source;
  }

  public static Source parseFromLabel(String label) {
    for (Source s: Source.values()) {      
      if (s.getLabel().equalsIgnoreCase(label)) {
        return s;
      }
    }

    return null;
  }
}
