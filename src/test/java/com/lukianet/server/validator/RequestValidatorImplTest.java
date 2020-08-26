package com.lukianet.server.validator;

import static org.junit.jupiter.api.Assertions.*;

import com.google.common.collect.Lists;
import com.lukianet.server.dto.Request;
import com.lukianet.server.dto.RequestValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RequestValidatorImplTest {
  private RequestValidatorImpl validator;

  @BeforeEach
  void setUp() {
    validator = new RequestValidatorImpl();
  }

  @Test
  void shouldValidateInvalidMethod() {
    Request request =
        new Request("PUT", "/", "HTTP/1.1", Lists.newArrayList("Connection: keep-alive"));
    RequestValidationResult result = validator.validate(request);
    assertEquals(result.getStatus(), RequestValidatorImpl.STATUS_501);
  }

  @Test
  void shouldValidateNotSupportedVersion() {
    Request request =
        new Request("GET", "/", "HTTP/2.0", Lists.newArrayList("Connection: keep-alive"));
    RequestValidationResult result = validator.validate(request);
    assertEquals(result.getStatus(), RequestValidatorImpl.STATUS_505);
  }

  @Test
  void shouldEmptyRequestURI() {
    Request request =
        new Request("GET", null, "HTTP/1.1", Lists.newArrayList("Connection: keep-alive"));
    RequestValidationResult result = validator.validate(request);
    assertEquals(result.getStatus(), RequestValidatorImpl.STATUS_400);
  }

  @Test
  void shouldInvalidRequestURI() {
    Request request =
        new Request(
            "GET",
            "http://www.w3.org/pub/WWW/TheProject.html",
            "HTTP/1.1",
            Lists.newArrayList("Connection: keep-alive"));
    RequestValidationResult result = validator.validate(request);
    assertEquals(result.getStatus(), RequestValidatorImpl.STATUS_400);
  }
}
