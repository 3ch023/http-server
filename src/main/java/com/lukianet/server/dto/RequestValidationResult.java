package com.lukianet.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RequestValidationResult {
  private String status;
  private String message;
}
