package vn.backend.ksv.common.exception;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/10/23
 * Time: 3:56 PM
 */
public class UserException {

    public static class BlockedException extends BaseException {
        public BlockedException(String msg) {
            super(msg);
        }

        public BlockedException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static class PasswordNotMatchException extends BaseException {
        public PasswordNotMatchException(String msg) {
            super(msg);
        }

        public PasswordNotMatchException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

}
