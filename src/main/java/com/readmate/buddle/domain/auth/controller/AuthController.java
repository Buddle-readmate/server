package com.readmate.buddle.domain.auth.controller;

import static com.readmate.buddle.domain.user.entity.SocialProvider.GOOGLE;

import com.readmate.buddle.domain.auth.converter.AuthConverter;
import com.readmate.buddle.domain.auth.dto.request.SocialLoginRequest;
import com.readmate.buddle.domain.auth.dto.response.LoginResponse;
import com.readmate.buddle.domain.auth.entity.LoginResult;
import com.readmate.buddle.domain.auth.entity.RefreshToken;
import com.readmate.buddle.domain.auth.service.AuthService;
import com.readmate.buddle.global.response.ApiResponse;
import com.readmate.buddle.global.security.cookie.TokenCookieManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "로그인, 토큰 관련 API")
public class AuthController {

  private final AuthService authService;
  private final TokenCookieManager tokenCookieManager;

  @Operation(summary = "구글 로그인", description = "프론트가 구글에서 받은 인가 코드로 로그인합니다.")
  @PostMapping("/login/google")
  public ApiResponse<LoginResponse> loginWithGoogle(
      @RequestBody @Valid SocialLoginRequest request, HttpServletResponse response) {
    String authorizationCode = request.code();
    LoginResult loginResult = authService.login(GOOGLE, authorizationCode);
    RefreshToken refreshToken = loginResult.refreshToken();
    tokenCookieManager.addRefreshTokenCookie(response, refreshToken);
    LoginResponse loginResponse = AuthConverter.toLoginResponse(loginResult);
    return ApiResponse.success(loginResponse);
  }
}
