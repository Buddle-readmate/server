package com.readmate.buddle.domain.auth.controller;

import static com.readmate.buddle.domain.user.entity.SocialProvider.GOOGLE;

import com.readmate.buddle.domain.auth.converter.AuthConverter;
import com.readmate.buddle.domain.auth.dto.request.SocialLoginRequest;
import com.readmate.buddle.domain.auth.dto.response.LoginResponse;
import com.readmate.buddle.domain.auth.dto.response.ReissueResponse;
import com.readmate.buddle.domain.auth.entity.IssuedAccessToken;
import com.readmate.buddle.domain.auth.entity.LoginResult;
import com.readmate.buddle.domain.auth.entity.LoginTokens;
import com.readmate.buddle.domain.auth.entity.RefreshToken;
import com.readmate.buddle.domain.auth.service.AuthService;
import com.readmate.buddle.domain.user.entity.UserId;
import com.readmate.buddle.global.response.ApiResponse;
import com.readmate.buddle.global.security.cookie.TokenCookieManager;
import com.readmate.buddle.global.security.resolver.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
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

  @Operation(summary = "토큰 재발급", description = "refresh token 쿠키로 토큰을 새로 발급합니다.")
  @PostMapping("/reissue")
  public ApiResponse<ReissueResponse> reissue(
      @CookieValue(name = "${jwt.refresh-cookie-name}", required = false) String refreshToken,
      HttpServletResponse response) {
    LoginTokens loginTokens = authService.reissue(refreshToken);
    RefreshToken newRefreshToken = loginTokens.refreshToken();
    tokenCookieManager.addRefreshTokenCookie(response, newRefreshToken);
    ReissueResponse reissueResponse = AuthConverter.toReissueResponse(loginTokens);
    return ApiResponse.success(reissueResponse);
  }

  @Operation(summary = "로그아웃", description = "access token을 막고 refresh token을 삭제합니다.")
  @PostMapping("/logout")
  public ApiResponse<Void> logout(
      @AuthenticationPrincipal Jwt jwt,
      @CookieValue(name = "${jwt.refresh-cookie-name}", required = false) String refreshToken,
      HttpServletResponse response) {
    IssuedAccessToken accessToken = AuthConverter.toIssuedAccessToken(jwt);
    authService.logout(accessToken, refreshToken);
    tokenCookieManager.removeRefreshTokenCookie(response);
    return ApiResponse.success(null);
  }

  @Operation(summary = "회원 탈퇴", description = "계정과 관련 데이터를 삭제하고 토큰을 무효화합니다.")
  @DeleteMapping("/withdraw")
  public ApiResponse<Void> withdraw(
      @LoginUser UserId userId,
      @AuthenticationPrincipal Jwt jwt,
      @CookieValue(name = "${jwt.refresh-cookie-name}", required = false) String refreshToken,
      HttpServletResponse response) {
    IssuedAccessToken accessToken = AuthConverter.toIssuedAccessToken(jwt);
    authService.withdraw(userId, accessToken, refreshToken);
    tokenCookieManager.removeRefreshTokenCookie(response);
    return ApiResponse.success(null);
  }
}
