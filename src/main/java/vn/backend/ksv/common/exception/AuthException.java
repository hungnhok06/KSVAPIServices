package vn.backend.ksv.common.exception;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/10/23
 * Time: 3:54 PM
 */
public class AuthException extends CommonException{

    public static class PasswordNotMatchException extends BaseException {
        public PasswordNotMatchException(String msg) {
            super(msg);
        }

        public PasswordNotMatchException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }
}
