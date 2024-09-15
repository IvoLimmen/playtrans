package org.limmen.playtrans.api;

import java.time.LocalDate;
import java.util.List;

public record Playlist(List<Song> songs, LocalDate date, String source) {  
}
