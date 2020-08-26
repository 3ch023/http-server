package com.lukianet.server.parser;

import com.lukianet.server.dto.Request;
import java.io.IOException;
import java.io.InputStream;

public interface RequestParser {
  Request parse(final InputStream requestInputStream) throws IOException, HttpFormatException;
}
