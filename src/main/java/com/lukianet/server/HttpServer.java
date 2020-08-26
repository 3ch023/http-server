package com.lukianet.server;

import com.lukianet.server.handler.RequestHandler;
import java.io.IOException;
import java.net.ServerSocket;
import org.apache.log4j.Logger;

public class HttpServer {
  private static final int DEFAULT_PORT = 8081;
  private static final Logger LOG = Logger.getLogger(HttpServer.class);

  public static void main(String[] args) {
    int port = getAvailablePort(args);
    try {
      start(port);
    } catch (IOException e) {
      LOG.error("Failed to start the server", e);
    }
  }

  private static void start(final int port) throws IOException {
    LOG.info("Starting the server. Listening for connections on a port " + port);
    try (ServerSocket serverSocket = new ServerSocket(port)) {
      while (true) {
        try {
          RequestHandler handler = new RequestHandler(serverSocket.accept());
          Thread thread = new Thread(handler);
          thread.start();
        } catch (IOException e) {
          LOG.error("Connection error", e);
        }
      }
    }
  }

  private static int getAvailablePort(final String[] args) {
    if (args.length > 0 && args[0] != null) {
      try {
        return Integer.parseInt(args[0]);
      } catch (NumberFormatException e) {
        LOG.error(
            "Failed to parse provided port [{}]"
                + args[0]
                + ". Fall back to DEFAULT_PORT "
                + DEFAULT_PORT);
      }
    }
    return DEFAULT_PORT;
  }
}
