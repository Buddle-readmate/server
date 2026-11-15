package com.readmate.buddle.global.security.resolver;

import static com.readmate.buddle.domain.auth.exception.AuthErrorCode.AUTH_TOKEN_REQUIRED;

import com.readmate.buddle.domain.user.entity.UserId;
import com.readmate.buddle.global.exception.BusinessException;
import java.security.Principal;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    boolean hasLoginUser = parameter.hasParameterAnnotation(LoginUser.class);
    boolean isUserId = UserId.class.equals(parameter.getParameterType());
    return hasLoginUser && isUserId;
  }

  @Override
  public UserId resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer modelAndViewContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {
    Principal principal = webRequest.getUserPrincipal();
    validateAuthenticated(principal);
    String subject = principal.getName();
    return new UserId(Long.valueOf(subject));
  }

  private static void validateAuthenticated(Principal principal) {
    if (principal == null) {
      throw new BusinessException(AUTH_TOKEN_REQUIRED);
    }
  }
}
