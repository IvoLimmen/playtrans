package org.limmen.playtrans.playlists;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.limmen.playtrans.api.Playlist;
import org.limmen.playtrans.api.PlaylistSource;
import org.limmen.playtrans.api.Song;
import org.limmen.playtrans.api.Source;

public class Relisten extends AbstractPlaylistRetriever implements PlaylistSource {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");

  @Override
  public List<String> getSources() {
    return Arrays.asList(Source.values())
        .stream()
        .map(Source::getLabel)
        .toList();
  }

  @Override
  public Playlist getPlaylist(String source, LocalDate date) {    
    try {
      Source s = Source.parseFromLabel(source);
      String url = String.format("https://www.relisten.nl/playlists/%s/%s.html", s.getSource(), formatter.format(date));
      String pageData = getPageData(URI.create(url));

      Document doc = Jsoup.parse(pageData);

      Elements items = doc.select(new Evaluator() {
        @Override
        public boolean matches(Element root, Element element) {
          if (element.nodeName().equals("span")) {
            if (element.hasAttr("itemprop")) {
              return true;
            }
          }

          return false;
        }
      });

      var songs = new ArrayList<Song>();

      for (var i = 0; i < items.size(); i++) {
        String title = items.get(i).text();
        String artist = items.get(i + 1).text();

        songs.add(new Song(artist, title));
        i++;
      }

      return new Playlist(songs, LocalDate.now(), s.getLabel());
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
