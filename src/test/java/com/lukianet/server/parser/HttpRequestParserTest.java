package com.lukianet.server.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.lukianet.server.dto.Request;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HttpRequestParserTest {
  private static final String VALID_REQUEST =
      "GET / HTTP/1.1\n"
          + "Host: localhost:8081\n"
          + "Connection: keep-alive\n"
          + "Upgrade-Insecure-Requests: 1\n"
          + "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36\n"
          + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\n"
          + "Accept-Encoding: gzip, deflate, br\n"
          + "Accept-Language: en-GB,en-US;q=0.9,en;q=0.8,uk;q=0.7,de;q=0.6,ru;q=0.5\n"
          + "Cookie: login-token=127a4811-ee82-4f64-bba0-33eac2863110%3aaac64eb9-9a11-4e91-8c09-ae1dd6eda4b1_baf42d91b2432c458d0cf9164c862678%3acrx.default\n"
          + "\n"
          + "\n";
  private RequestParserImpl parser;

  @BeforeEach
  void setUp() {
    parser = new RequestParserImpl();
  }

  @Test
  void parse() throws IOException, HttpFormatException {
    Request request = parser.parse(getInputStream(VALID_REQUEST));
    assertEquals(request.getMethod(), "GET", "Should parse correct method");
    assertEquals(request.getVersion(), "HTTP/1.1", "Should parse correct version");
    assertEquals(request.getRequestURI(), "/", "Should parse correct request URI");
    assertEquals(request.getHeaders().size(), 8, "Should parse 1 header");
  }

  private InputStream getInputStream(String request) throws UnsupportedEncodingException {
    return new ByteArrayInputStream(request.getBytes("UTF-8"));
  }
}
