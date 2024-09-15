package org.limmen.playtrans.playlists;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

public class AbstractPlaylistRetriever {

  protected HttpClient client = HttpClient.newHttpClient();

  protected String getPageData(URI uri) {
    var request = HttpRequest.newBuilder()
        .uri(uri)
        .header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:130.0) Gecko/20100101 Firefox/130.0")
        .GET()
        .build();

    try {
      return client.send(request, BodyHandlers.ofString()).body();
    } catch (Exception e) {
      return null;
    }
  }
}
