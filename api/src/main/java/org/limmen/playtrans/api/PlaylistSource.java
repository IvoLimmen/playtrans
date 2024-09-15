package org.limmen.playtrans.api;

import java.time.LocalDate;
import java.util.List;

public interface PlaylistSource {

  List<String> getSources();

  Playlist getPlaylist(String source, LocalDate date);
}
