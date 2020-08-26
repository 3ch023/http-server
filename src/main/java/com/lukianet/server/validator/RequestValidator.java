package com.lukianet.server.validator;

import com.lukianet.server.dto.Request;
import com.lukianet.server.dto.RequestValidationResult;

public interface RequestValidator {
  RequestValidationResult validate(Request request);
}
