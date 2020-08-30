package com.lukianet.server.server;

import com.lukianet.server.handler.RequestHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;

@NoArgsConstructor
public class HttpServerImpl implements HttpServer {
  private static final Logger LOG = Logger.getLogger(HttpServerImpl.class);
  private int port;
  private boolean isStopped;

  public HttpServerImpl(int port) {
    this.port = port;
  }

  @Override
  public void start() throws IOException {
    LOG.info("Starting the server. Listening for connections on a port " + port);
    ExecutorService threadPool = Executors.newCachedThreadPool();
    try (ServerSocket serverSocket = new ServerSocket(port)) {
      while (!isStopped()) {
        try {
          RequestHandler handler = new RequestHandler(serverSocket.accept());
          threadPool.execute(handler);
        } catch (IOException e) {
          if (isStopped()) {
            LOG.warn("Failed to handle the request: server is stopped.");
            break;
          }
          throw new IOException("Connection error", e);
        }
      }
      LOG.info("Server Stopped.");
    }
  }

  private synchronized boolean isStopped() {
    return this.isStopped;
  }

  @Override
  public void stop() {
    LOG.info("Stopping the server.");
    this.isStopped = true;
  }
}
