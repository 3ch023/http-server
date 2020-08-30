package com.lukianet.server.server;

import java.io.IOException;

public interface HttpServer {
  void start() throws IOException;

  void stop() throws IOException;
}
