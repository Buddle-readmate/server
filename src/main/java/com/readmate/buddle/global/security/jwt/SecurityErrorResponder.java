package com.readmate.buddle.global.security.jwt;

import com.readmate.buddle.global.exception.ErrorCode;
import com.readmate.buddle.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
@RequiredArgsConstructor
public class SecurityErrorResponder {

  private static final String JSON_UTF_8 = "application/json;charset=UTF-8";

  private final JsonMapper jsonMapper;

  public void respond(HttpServletResponse response, ErrorCode errorCode) throws IOException {
    HttpStatus status = errorCode.status();
    response.setStatus(status.value());
    response.setContentType(JSON_UTF_8);
    ApiResponse<Void> apiResponse = ApiResponse.failure(errorCode);
    PrintWriter writer = response.getWriter();
    writer.write(jsonMapper.writeValueAsString(apiResponse));
  }
}
