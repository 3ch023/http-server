package com.lukianet.server;

import com.lukianet.server.server.HttpServer;
import com.lukianet.server.server.HttpServerImpl;
import java.io.IOException;
import org.apache.log4j.Logger;

public class Application {
  private static final int DEFAULT_PORT = 8081;
  private static final Logger LOG = Logger.getLogger(Application.class);

  public static void main(String[] args) {
    HttpServer server = new HttpServerImpl(DEFAULT_PORT);
    try {
      server.start();
    } catch (IOException e) {
      LOG.error("Failed to start the server", e);
    }
  }
}
