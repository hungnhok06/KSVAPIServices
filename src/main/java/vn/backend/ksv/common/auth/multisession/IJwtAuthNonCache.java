package vn.backend.ksv.common.auth.multisession;

import vn.backend.ksv.common.constant.staticEnum.StaticEnum;
import vn.backend.ksv.common.exception.CommonException;
import vn.backend.ksv.common.pojo.common.HeaderInternalRequest;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 10:31 AM
 */
public interface IJwtAuthNonCache {

    HeaderInternalRequest validateToken(String token, String collationId, StaticEnum.TokenType tokenType) throws CommonException.TokenExpiredByLoginException, CommonException.TokenExpiredByTimeoutException, CommonException.ValidationError;

    HeaderInternalRequest validateRefreshToken(String token, StaticEnum.TokenType tokenType) throws CommonException.TokenExpiredByLoginException, CommonException.TokenExpiredByTimeoutException, CommonException.ValidationError;

}
