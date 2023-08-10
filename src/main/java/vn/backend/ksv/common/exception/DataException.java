package vn.backend.ksv.common.exception;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 10:44 AM
 */
public class DataException {

    public static class QueryException extends BaseException {
        public QueryException(String msg) {
            super(msg);
        }

        public QueryException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static class ExecuteException extends BaseException {

        public ExecuteException(String msg) {
            super(msg);
        }

        public ExecuteException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static class NotFoundException extends BaseException {

        public NotFoundException(String msg) {
            super(msg);
        }

        public NotFoundException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static class MultiValuesException extends BaseException {

        public MultiValuesException(String msg) {
            super(msg);
        }

        public MultiValuesException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static class DuplicateException extends BaseException {
        public DuplicateException(String msg) {
            super(msg);
        }

        public DuplicateException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }
}
