package org.limmen.playtrans.app;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.limmen.playtrans.api.Console;
import org.limmen.playtrans.api.Playlist;
import org.limmen.playtrans.api.Song;

public class ConsoleImpl implements Console {
  
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  @Override
  public String showSources(List<String> sources) {
    System.out.println("Choose a radio station:");
    var i = new AtomicInteger(1);
    sources.forEach(s -> {      
      System.out.println(String.format("%2d - %s", i.getAndIncrement(), s));
    });
        
    var input = System.console().readLine();
    int selection = Integer.parseInt(input);

    return sources.get(selection - 1);
  }

  @Override
  public LocalDate selectDate() {
    System.out.println("Enter a date to retrieve the playlist from.");
    System.out.println("Keep in mind that today is not usefull unless it is near midnight.");
    System.out.println("Enter the date (yyyy-mm-dd)");
    String input = System.console().readLine();

    return LocalDate.parse(input, formatter);
  }

  @Override
  public void showCreatingList() {
    System.out.println("Creating playlist...");
  }

  @Override
  public void showPlaylist(Playlist playlist) {
    System.out.println(String.format("Playlist of '%s' on '%s'", playlist.source(), formatter.format(playlist.date())));
    playlist.songs().forEach(s -> {
      System.out.println(String.format("%s - %s", s.artist(), s.title()));
    });
  }

  @Override
  public void showSearchingSong(Song song) {
    System.out.println(String.format("Searching for '%s - %s'", song.artist(), song.title()));
  }
}
