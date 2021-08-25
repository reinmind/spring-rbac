package org.zx.common.security.code;

public interface JWTConst {
    int EXPIRATION_TIME = 3600;
    String SECRET = "SECRET_KEY";
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String SIGN_UP_URL = "/api/user/create";
}
