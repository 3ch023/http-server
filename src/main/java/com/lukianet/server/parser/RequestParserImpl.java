package com.lukianet.server.parser;

import com.lukianet.server.dto.Request;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

public class RequestParserImpl implements RequestParser {
  private static final Logger LOG = Logger.getLogger(RequestParserImpl.class);

  @Override
  public Request parse(final InputStream is) throws IOException, HttpFormatException {
    Request request = new Request();
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    parseRequestHeadline(request, br);
    parseHeaders(request, br);
    LOG.debug("Request parsed: %s" + request.toString());
    return request;
  }

  private void parseRequestHeadline(Request request, BufferedReader br)
      throws IOException, HttpFormatException {
    String requestHeadline = br.readLine();
    if (requestHeadline == null) {
      throw new HttpFormatException("Invalid empty request.");
    }
    StringTokenizer parsedHeadline = new StringTokenizer(requestHeadline);
    request.setMethod(parsedHeadline.nextToken());
    request.setRequestURI(parsedHeadline.nextToken());
    request.setVersion(parsedHeadline.nextToken());
  }

  private void parseHeaders(Request request, BufferedReader br) throws IOException {
    List<String> headers = new ArrayList<>();
    String header;
    while (!(header = br.readLine()).isEmpty()) {
      headers.add(header);
    }
    request.setHeaders(headers);
  }
}
