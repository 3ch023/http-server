package com.lukianet.server.validator;

import com.lukianet.server.dto.Request;
import com.lukianet.server.dto.RequestValidationResult;

public class RequestValidatorImpl implements RequestValidator {
  private static final String METHOD_HEAD = "HEAD";
  private static final String METHOD_GET = "GET";
  private static final String HTTP_VERSION_1_1 = "HTTP/1.1";
  static final String STATUS_400 = "400 Bad Request";
  static final String STATUS_501 = "501 Not Implemented";
  static final String STATUS_505 = "505 HTTP Version not supported";
  private static final String MESSAGE_METHOD_NOT_IMPLEMENTED =
      "Invalid request. Not implemented method: ";
  private static final String MESSAGE_VERSION_NOT_SUPPORTED =
      "Invalid request. Not supported version: ";
  private static final String MESSAGE_NOT_VALID_REQUEST_URI = "Invalid Request-URI:";

  @Override
  public RequestValidationResult validate(final Request request) {
    if (isNotAllowedMethod(request)) {
      return new RequestValidationResult(
          STATUS_501, MESSAGE_METHOD_NOT_IMPLEMENTED + request.getMethod());
    }
    if (isNotSupportedVersion(request)) {
      return new RequestValidationResult(
          STATUS_505, MESSAGE_VERSION_NOT_SUPPORTED + request.getVersion());
    }
    if (request.getRequestURI() == null) {
      return new RequestValidationResult(
          STATUS_400, MESSAGE_NOT_VALID_REQUEST_URI + request.getRequestURI());
    }
    if (request.getRequestURI().startsWith("http")) {
      return new RequestValidationResult(
          STATUS_400, MESSAGE_NOT_VALID_REQUEST_URI + request.getRequestURI());
    }
    return null;
  }

  private boolean isNotSupportedVersion(Request request) {
    return !HTTP_VERSION_1_1.equals(request.getVersion());
  }

  private boolean isNotAllowedMethod(Request request) {
    return !METHOD_GET.equals(request.getMethod()) && !METHOD_HEAD.equals(request.getMethod());
  }
}
