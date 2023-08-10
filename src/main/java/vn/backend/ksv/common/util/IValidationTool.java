package vn.backend.ksv.common.util;

import com.google.gson.JsonObject;
import vn.backend.ksv.common.exception.CommonException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 10:52 AM
 */
public interface IValidationTool {

    IValidationTool isValidPhoneNumber(String phone);

    IValidationTool isValidEmailAddress(String email);

    IValidationTool checkNullAll(Object object);

    IValidationTool checkNotNullWithAnnotation(Object object);

    IValidationTool checkCanNullWithAnnotation(Object object);

    IValidationTool checkSpecialCharacter(Object object);

    IValidationTool checkSpecialCharacter(String string);

    IValidationTool checkSpecialCharacterAllowLink(Object object, List<String> allowList);

    IValidationTool checkSpecialCharacterForLink(String string);

    IValidationTool compareByte(byte[] input1, byte[] input2) throws CommonException.ValidationError;

    String removeVietnameseKey(String rawStr);

    JsonObject removeHtmlCharacter(JsonObject payload);

    void checkDeviceInfoFormat(String deviceInfo);

    void checkAppVersion(String appVersion);

    void isMultiple(BigDecimal number, BigDecimal multiple);

    void checkMaxPercent(BigDecimal number, double percent, BigDecimal numberToCheck);

    void checkMinPercent(BigDecimal number, double percent, BigDecimal numberToCheck);
}
