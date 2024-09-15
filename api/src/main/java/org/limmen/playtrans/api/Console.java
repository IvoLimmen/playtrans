package org.limmen.playtrans.api;

import java.time.LocalDate;
import java.util.List;

public interface Console {

  String showSources(List<String> sources);

  LocalDate selectDate();

  void showCreatingList();

  void showPlaylist(Playlist playlist);

  void showSearchingSong(Song song);
}