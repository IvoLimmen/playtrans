package org.limmen.playtrans.spotify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class WebServer implements Runnable {

  private String code;

  /**
   * Very simplified 'webserver' to capture the code that Spotify sends back.
   */
  public void run() {

    try (
        ServerSocket serverSocket = new ServerSocket(1234);
        Socket socket = serverSocket.accept();
        var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        var outputStream = socket.getOutputStream()) {
              
        var list = new ArrayList<String>();
        String line;
        while ((line = reader.readLine()) != null) {
          list.add(line);
        }

        outputStream.write("HTTP/1.1 200 OK\n\rContent-Length: 0\n\r\n\r".getBytes());

        String codeLine = list.get(0).split(" ")[1];
        codeLine = codeLine.replace("/?code=", "");
        this.code = codeLine;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String getCode() {
    return code;
  }
}
