package org.limmen.playtrans.app;

import java.util.Properties;

import org.limmen.playtrans.api.Console;
import org.limmen.playtrans.playlists.Relisten;
import org.limmen.playtrans.spotify.SpotifyPlaylistUploader;

public class Playtrans {

  public static void main(String[] args) throws Exception {

    Console console = new ConsoleImpl();
    Properties properties = new Properties();
    properties.load(Playtrans.class.getResourceAsStream("/application.properties"));

    var from = new Relisten();
    var list = from.getSources();

    var choice = console.showSources(list);
    var date = console.selectDate();
    
    var playlist = from.getPlaylist(choice, date);
    
    if (playlist != null && !playlist.songs().isEmpty()) {

      var to = new SpotifyPlaylistUploader(
          console,
          properties.getProperty("auth.clientId"),
          properties.getProperty("auth.clientSecret"),
          properties.getProperty("userId"),
          properties.getProperty("auth.redirectUri"));
        
      console.showCreatingList();
      to.createPlayList(playlist, properties.getProperty("userId"));
    }
  }
}
