package vn.backend.ksv.common.auth.response;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 11:59 AM
 */
public enum AuthResponseCode {

    UNKNOWN("-999", "unknown code"),
    OK("200", "check success"),
    BAD_REQUEST("400", "wrong input"),
    NOT_MATCH("401", "check not match"),
    NOT_FOUND("402", "not found id"),
    EXPIRED("403", "auth is expired by timeout or not found"),
    KEY_OTP_EXPIRED("404", "key otp expired"),
    CODE_OTP_EXPIRED("405", "code otp expired"),
    WRONG_OTP_MANY_TIME("406", "wrong otp many time"),
    REQUEST_OTP_LIMIT("407", "request otp limited");

    private String code;
    private String message;

    AuthResponseCode(String code,
                     String message) {
        this.code = code;
        this.message = message;
    }

    public static AuthResponseCode safeValuesOf(String code) {
        for (AuthResponseCode item: values()) {
            if (code.equalsIgnoreCase(item.getCode())) {
                return item;
            }
        }
        return UNKNOWN;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
