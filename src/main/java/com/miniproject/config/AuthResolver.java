package com.miniproject.config;

import com.miniproject.config.data.UserSession;
import com.miniproject.controller.AuthController;
import com.miniproject.domain.Session;
import com.miniproject.exception.UnAuthorized;
import com.miniproject.repositiry.SessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Slf4j
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;

    private final static String KEY = "GNestrg5aea6Wyt+k31NyjOrpszmlVsnVOjwQ3wZNjs=";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
//        if( servletRequest == null) {
//            log.error( "servletRequest null");
//            throw new UnAuthorized();
//        }
//        Cookie[] cookies = servletRequest.getCookies();
//        if( cookies.length == 0){
//            log.error( "쿠키 없음");
//            throw new UnAuthorized();
//        }
//
//        String accessToken = cookies[0].getValue();
//
//        Session session = sessionRepository.findByAccessToken( accessToken)
//                .orElseThrow(UnAuthorized::new);
        String jws = webRequest.getHeader( "Authorization");
        if( jws == null || jws.equals( "")) {
            throw new UnAuthorized();
        }

        byte[] decodedKey = Base64.decodeBase64( KEY);
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey( decodedKey)
                    .build()
                    .parseClaimsJws( jws);
            log.info(">>>>>{}", claims);
            String userId = claims.getBody().getSubject();
            return new UserSession( Long.parseLong(userId));
            //OK, we can trust this JWT

        } catch (JwtException e) {
            throw new UnAuthorized();
            //don't trust the JWT!
        }
//        return new UserSession( session.getLoginUser().getId());
    }
}
