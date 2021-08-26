package org.zx.common.security;

public interface JWTConst {
    int EXPIRATION_TIME = 900_000; // 15min;
    String SECRET = "SECRET_KEY";
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String SIGN_UP_URL = "/api/login";
}
