package com.lukianet.server;

import com.lukianet.server.server.HttpServer;
import com.lukianet.server.server.HttpServerImpl;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

public class Application {
  private static final int DEFAULT_PORT = 8080;
  private static final Logger LOG = Logger.getLogger(Application.class);
  private static final String PROPERTY_FILE_NAME = "config.properties";
  private static final String PROPERTY_PORT = "port";

  public static void main(String[] args) {
    HttpServer server = new HttpServerImpl(resolvePort());
    try {
      server.start();
    } catch (IOException e) {
      LOG.error("Failed to start the server", e);
    }
  }

  private static int resolvePort() {
    Properties properties = new Properties();
    try (InputStream is =
        Application.class.getClassLoader().getResourceAsStream(PROPERTY_FILE_NAME)) {
      if (is != null) {
        properties.load(is);
        return readIntProperty(properties, PROPERTY_PORT, DEFAULT_PORT);
      }
    } catch (IOException e) {
      LOG.error("Failed to read the config file", e);
    }
    return DEFAULT_PORT;
  }

  private static int readIntProperty(
      final Properties properties, final String propertyName, int defaultValue) {
    String portString = properties.getProperty(propertyName);
    if (portString != null) {
      try {
        return Integer.parseInt(portString);
      } catch (NumberFormatException e) {
        LOG.error("Failed to read config file", e);
      }
    }
    return defaultValue;
  }
}
