package vn.backend.ksv.common.auth;

import vn.backend.ksv.common.constant.staticEnum.StaticEnum;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/10/23
 * Time: 4:07 PM
 */
public interface IJwtProvider {

    String generateToken(TokenAuthInfo tokenAuthInfo, TokenUserInfo tokenUserInfo, StaticEnum.TokenType tokenType, Integer timeToLive);

}
