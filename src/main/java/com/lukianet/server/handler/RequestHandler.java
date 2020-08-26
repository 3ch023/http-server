package com.lukianet.server.handler;

import com.google.common.io.ByteStreams;
import com.lukianet.server.dto.Request;
import com.lukianet.server.dto.RequestValidationResult;
import com.lukianet.server.dto.Response;
import com.lukianet.server.parser.HttpFormatException;
import com.lukianet.server.parser.RequestParser;
import com.lukianet.server.parser.RequestParserImpl;
import com.lukianet.server.validator.RequestValidator;
import com.lukianet.server.validator.RequestValidatorImpl;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLConnection;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;

@AllArgsConstructor
public class RequestHandler implements Runnable {
  private static final Logger LOG = Logger.getLogger(RequestHandler.class);
  private static final String CONTENT_TYPE_TEXT_HTML = "text/html";
  private static final String CRLF = "\r\n";
  private static final String ROOT = "/";
  private static final String CONTENT_FOLDER = "/content";
  private static final String METHOD_GET = "GET";
  private static final String INDEX_PAGE = CONTENT_FOLDER + "/index.html";
  private static final String STATUS_400 = "400 Bad Request";
  private static final String STATUS_200 = "200 OK";
  private static final String STATUS_500 = "500 Internal Server Error";
  private static final String MESSAGE_RESOURCE_NOT_FOUND = "Resource not found: ";
  private static final String MESSAGE_FAILED_TO_PROCESS_REQUEST = "Failed to process request.";
  private RequestParser parser;
  private RequestValidator validator;
  private Socket client;

  public RequestHandler(Socket client) {
    this.client = client;
    this.parser = new RequestParserImpl();
    this.validator = new RequestValidatorImpl();
  }

  @Override
  public void run() {
    LOG.info("Handling new client " + client.toString());
    try (InputStream input = client.getInputStream();
        OutputStream output = client.getOutputStream()) {
      Request request = parser.parse(input);
      RequestValidationResult validationResult = validator.validate(request);
      Response response =
          validationResult == null
              ? processRequest(output, request)
              : createErrorResponse(validationResult.getStatus(), validationResult.getMessage());
      sendResponse(output, response);
      client.close();
    } catch (IOException | HttpFormatException e) {
      LOG.error("Failed to handle request", e);
    }
  }

  private Response processRequest(final OutputStream output, Request request) {
    try (InputStream resource = getResource(request.getRequestURI())) {
      if (resource == null) {
        return createErrorResponse(
            STATUS_400, MESSAGE_RESOURCE_NOT_FOUND + request.getRequestURI());
      }
      Response response = new Response(STATUS_200);
      if (METHOD_GET.equals(request.getMethod())) {
        String contentType = URLConnection.guessContentTypeFromStream(resource);
        response.setContentType(contentType);
        response.setContent(ByteStreams.toByteArray(resource));
      }
      return response;
    } catch (IOException e) {
      return createErrorResponse(STATUS_500, MESSAGE_FAILED_TO_PROCESS_REQUEST);
    }
  }

  private InputStream getResource(final String resourcePath) {
    if (ROOT.equals(resourcePath)) {
      return this.getClass().getResourceAsStream(INDEX_PAGE);
    }
    return this.getClass().getResourceAsStream(CONTENT_FOLDER + resourcePath);
  }

  private void sendResponse(final OutputStream output, final Response response) {
    try {
      output.write(response.getStatusLine().getBytes());
      output.write((response.getContentTypeLine()).getBytes());
      output.write(CRLF.getBytes());
      output.write(response.getContent());
      output.write((CRLF + CRLF).getBytes());
      output.flush();
      // todo return headers
    } catch (IOException e) {
      LOG.error("Failed to send response", e);
    }
  }

  private Response createErrorResponse(final String status, final String message) {
    LOG.error(message);
    byte[] content = String.format("<h1>%s</h1>", message).getBytes();
    Response response = new Response(status);
    response.setContentType(CONTENT_TYPE_TEXT_HTML);
    response.setContent(content);
    return response;
  }
}
