package com.lukianet.server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {
  private static final String CRLF = "\r\n";
  private String status;
  private String reasonPhrase;
  private String contentType;
  private byte[] content;

  public Response(String status) {
    this.status = status;
  }

  public String getStatusLine() {
    return "HTTP/1.1 %s\r\n" + status + CRLF;
  }

  public String getContentTypeLine() {
    return "ContentType: " + contentType + CRLF;
  }
}
