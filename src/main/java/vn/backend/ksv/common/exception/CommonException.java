package vn.backend.ksv.common.exception;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/8/23
 * Time: 4:09 PM
 */
public class CommonException {
    public static class GenerateException extends BaseException {
        public GenerateException(String msg) {
            super(msg);
        }

        public GenerateException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static class ValidationError extends BaseException {
        public ValidationError(String msg) {
            super(msg);
        }

        public ValidationError(String msg, Throwable cause) {
            super(msg, cause);
        }
    }


    public static class AuthException extends BaseException {
        public AuthException(String msg) {
            super(msg);
        }

        public AuthException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static class ForbiddenException extends BaseException {
        public ForbiddenException(String msg) {
            super(msg);
        }

        public ForbiddenException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static class TokenException extends BaseException {
        public TokenException(String msg) {
            super(msg);
        }

        public TokenException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static class TimeoutException extends BaseException {
        public TimeoutException(String msg) {
            super(msg);
        }

        public TimeoutException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static class UnsupportException extends BaseException {
        public UnsupportException(String msg) {
            super(msg);
        }

        public UnsupportException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static class UnknownException extends BaseException {
        public UnknownException(String msg) {
            super(msg);
        }

        public UnknownException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static class UnknownValuesException extends BaseException {
        public UnknownValuesException(String msg) {
            super(msg);
        }

        public UnknownValuesException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static class ConfigException extends BaseException {
        public ConfigException(String msg) {
            super(msg);
        }

        public ConfigException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static class InvalidConfig extends BaseException {
        public InvalidConfig(String msg) {
            super(msg);
        }

        public InvalidConfig(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static class FileException extends BaseException {
        public FileException(String msg) {
            super(msg);
        }

        public FileException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static class ConnectionError extends BaseException{
        public ConnectionError(String msg) {
            super(msg);
        }

        public ConnectionError(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static class RestAdapterError extends BaseException{
        public RestAdapterError(String msg) {
            super(msg);
        }

        public RestAdapterError(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static class HMACException extends BaseException {
        public HMACException(String msg) {
            super(msg);
        }

        public HMACException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static class TokenExpiredByLoginException extends BaseException {
        public TokenExpiredByLoginException(String msg) {
            super(msg);
        }

        public TokenExpiredByLoginException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static class TokenExpiredByTimeoutException extends BaseException {
        public TokenExpiredByTimeoutException(String msg) {
            super(msg);
        }

        public TokenExpiredByTimeoutException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static class RequireRefreshTokenException extends BaseException {
        public RequireRefreshTokenException(String msg) {
            super(msg);
        }

        public RequireRefreshTokenException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }
}
