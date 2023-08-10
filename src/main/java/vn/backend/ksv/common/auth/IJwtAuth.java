package vn.backend.ksv.common.auth;

import vn.backend.ksv.common.constant.staticEnum.StaticEnum;
import vn.backend.ksv.common.exception.CommonException;
import vn.backend.ksv.common.pojo.common.HeaderInternalRequest;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 10:36 AM
 */
public interface IJwtAuth {

    HeaderInternalRequest validateToken(String token, String collationId, StaticEnum.TokenType tokenType) throws CommonException.TokenExpiredByLoginException, CommonException.TokenExpiredByTimeoutException, CommonException.ValidationError;

    HeaderInternalRequest decodeToken(String token, String collationId, StaticEnum.TokenType tokenType) throws CommonException.TokenExpiredByLoginException, CommonException.TokenExpiredByTimeoutException, CommonException.ValidationError;

    void expireToken(String cif, StaticEnum.TokenType tokenType) throws InterruptedException;

    void expireToken(String token) throws InterruptedException;

    void expireTokenByCif(String cif) throws InterruptedException;

    void checkToken(String cif, String token, Integer tokenType);

    void checkDevice(Integer status);

    void validateRootToken(String token);
}
