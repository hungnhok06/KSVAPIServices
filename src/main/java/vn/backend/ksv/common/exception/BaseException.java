package vn.backend.ksv.common.exception;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/8/23
 * Time: 4:09 PM
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -128216908108589678L;

    public BaseException(String msg) {
        super(msg);
    }

    public BaseException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
