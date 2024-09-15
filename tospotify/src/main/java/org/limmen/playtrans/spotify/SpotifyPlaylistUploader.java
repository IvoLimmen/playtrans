package org.limmen.playtrans.spotify;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.limmen.playtrans.api.Console;
import org.limmen.playtrans.api.Playlist;
import org.limmen.playtrans.api.Song;

public final class SpotifyPlaylistUploader {

  private final HttpClient client;

  private final String accessToken;

  private final Console console;

  private final Integer PAGE_SIZE = 100;

  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public SpotifyPlaylistUploader(
      final Console console,
      final String clientId,
      final String clientSecret,
      final String userId,
      final String redirectUrl
  ) throws Exception {

    this.client = HttpClient.newHttpClient();
    this.console = console;

    HashMap<String, String> parameters = new HashMap<>();
    parameters.put("client_id", clientId);
    parameters.put("response_type", "code");
    parameters.put("scope", "playlist-modify-private");
    parameters.put("redirect_uri", redirectUrl);
    String form = parameters.keySet().stream()
        .map(key -> key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8))
        .collect(Collectors.joining("&"));

    var webServer = new WebServer();
    var thread = new Thread(webServer);
    thread.start();

    ProcessBuilder builder = new ProcessBuilder("open", "https://accounts.spotify.com/authorize?" + form);
    builder.start();

    thread.join();

    String code = webServer.getCode();

    parameters.clear();
    parameters.put("code", code);
    parameters.put("redirect_uri", redirectUrl);
    parameters.put("grant_type", "authorization_code");
    form = parameters.keySet().stream()
        .map(key -> key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8))
        .collect(Collectors.joining("&"));

    String auth = clientId + ":" + clientSecret;
    var request = HttpRequest.newBuilder()
        .uri(URI.create("https://accounts.spotify.com/api/token"))
        .POST(HttpRequest.BodyPublishers.ofString(form))
        .headers("Content-Type", "application/x-www-form-urlencoded")
        .header("Authorization", "Basic " + Base64.getEncoder().encodeToString(auth.getBytes()))
        .build();

    var response = client.send(request, BodyHandlers.ofString());
    var json = new JSONObject(response.body());
    this.accessToken = json.getString("access_token");
  }

  public void createPlayList(final Playlist playlist, final String userId) {

    try {
      var list = new ArrayList<String>();

      playlist.songs().forEach(song -> {
        list.add(findSpotifyTrack(song));
      });

      if (!list.isEmpty()) {
        String id = this.createSpotifyPlaylist(playlist, userId);
        addTracksSpotiyPlaylist(id, list);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String playListName(final Playlist playlist) {
    return String.format("%s %s", playlist.source(), formatter.format(playlist.date()));
  }

  private String playListDescription(final Playlist playlist) {
    return String.format("Playlist made from %s from %s", playlist.source(), formatter.format(playlist.date()));
  }

  private void addTracksSpotiyPlaylist(final String playlistId, final List<String> uris) throws Exception {
    // we need to send the songs in batches as we can only add 100 songs per request
    while (!uris.isEmpty()) {

      List<String> tmpList = new ArrayList<>();

      for (var i = 0; i < PAGE_SIZE && i < uris.size(); i++) {
        tmpList.add(uris.get(i));
      }
      uris.removeAll(tmpList);

      var body = new JSONObject();
      body.put("uris", new JSONArray(tmpList));
      body.put("position", "0");

      var request = HttpRequest.newBuilder()
          .uri(URI.create("https://api.spotify.com/v1/playlists/" + playlistId + "/tracks"))
          .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
          .headers("Content-Type", "application/json")
          .header("Authorization", "Bearer " + this.accessToken)
          .build();

      client.send(request, BodyHandlers.ofString());
    }
  }

  private String createSpotifyPlaylist(final Playlist playlist, final String userId) throws Exception {

    var body = new JSONObject();
    body.put("name", playListName(playlist));
    body.put("description", playListDescription(playlist));
    body.put("public", false);
    body.put("collaborative", false);

    var request = HttpRequest.newBuilder()
        .uri(URI.create("https://api.spotify.com/v1/me/playlists"))
        .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
        .headers("Content-Type", "application/json")
        .header("Authorization", "Bearer " + this.accessToken)
        .build();

    var response = client.send(request, BodyHandlers.ofString());
    var json = new JSONObject(response.body());

    return json.getString("id");
  }

  private String findSpotifyTrack(final Song song) {

    console.showSearchingSong(song);

    HashMap<String, String> parameters = new HashMap<>();
    parameters.put("market", "NL");
    parameters.put("type", "track");
    parameters.put("limit", "1");
    parameters.put("q", song.artist() + " " + song.title());

    String query = parameters.keySet().stream()
        .map(key -> key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8))
        .collect(Collectors.joining("&"));

    var request = HttpRequest.newBuilder()
        .uri(URI.create("https://api.spotify.com/v1/search?" + query))
        .GET()
        .headers("Content-Type", "application/json")
        .header("Authorization", "Bearer " + this.accessToken)
        .build();

    try {
      var response = client.send(request, BodyHandlers.ofString());
      var json = new JSONObject(response.body());
      var tracks = json.getJSONObject("tracks");
      var items = tracks.optJSONArray("items");
      return items.getJSONObject(0).getString("uri");
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
