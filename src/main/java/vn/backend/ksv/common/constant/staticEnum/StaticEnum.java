package vn.backend.ksv.common.constant.staticEnum;

import vn.backend.ksv.common.exception.CommonException;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 10:33 AM
 */
public class StaticEnum {

    public enum TokenType {
        ADMIN_LOGIN(1);
        private Integer code;

        TokenType(Integer code) {
            this.code = code;
        }

        public static TokenType safeValuesOf(Integer code) {
            for (TokenType item : values()) {
                if (item.getCode().equals(code)) {
                    return item;
                }
            }
            throw new CommonException.UnknownValuesException("Unknown token with values " + code);
        }

        public Integer getCode() {
            return code;
        }
    }

}
