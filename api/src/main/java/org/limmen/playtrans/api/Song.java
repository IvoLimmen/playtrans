package org.limmen.playtrans.api;

public record Song(String artist, String title) {

  public String toSearchString() {
    return artist + " " + title;
  }
}
