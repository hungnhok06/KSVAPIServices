package vn.backend.ksv.common.util;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import vn.backend.ksv.common.LogAdapter;
import vn.backend.ksv.common.anotation.CanEmpty;
import vn.backend.ksv.common.anotation.CanNullOrEmpty;
import vn.backend.ksv.common.anotation.NotNull;
import vn.backend.ksv.common.anotation.NotNullAndEmpty;
import vn.backend.ksv.common.exception.CommonException;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 10:53 AM
 */
public class ValidationTool implements IValidationTool{

    private final LogAdapter LOGGER = LogAdapter.newInstance(this.getClass());

    @Inject
    ValidationTool() {

    }

    @Override
    public IValidationTool isValidPhoneNumber(String phone) {
        if (phone == null) {
            throw new CommonException.ValidationError("Phone invalid");
        }
        if (!phone.trim().matches("\\d{10,13}")) {
            throw new CommonException.ValidationError("Phone invalid");
        }
        return this;
    }

    @Override
    public IValidationTool isValidEmailAddress(String email) throws CommonException.ValidationError {
        String patternEmail = "([a-z0-9A-Z!#$%&'*-+/=?^_`{|}~.]{3,})@([a-z0-9A-Z:.\\-]{5,})";
        if (!email.matches(patternEmail)) {
            throw new CommonException.ValidationError("Email invalid");
        }
        return this;
    }

    @Override
    public IValidationTool checkNullAll(Object object) {
        if (object == null) {
            throw new CommonException.ValidationError("Object is null");
        }
        for (Field f : object.getClass().getDeclaredFields()) {
            try {
                f.setAccessible(true);
                if (f.get(object) == null) {
                    throw new CommonException.ValidationError("Properties " + f.getName() + " is null");
                }
            } catch (IllegalAccessException e) {
                LOGGER.error("Error with accessible object cause by {} ", e.getMessage());
            }
        }
        return this;
    }

    /**
     * Check null and empty all field with annotation NotNullAndEmpty
     * Check null all field with annotation NotNull
     *
     * @param object
     * @throws CommonException.ValidationError
     */
    @Override
    public IValidationTool checkNotNullWithAnnotation(Object object) {
        if (object == null) {
            throw new CommonException.ValidationError("Object is null");
        }
        for (Field f : object.getClass().getDeclaredFields()) {
            try {
                f.setAccessible(true);
                if (f.isAnnotationPresent(NotNull.class) || f.isAnnotationPresent(NotNullAndEmpty.class)) {
                    if (null == f.get(object)) {
                        throw new CommonException.ValidationError("Properties " + f.getName() + " is null");
                    }
                }
                if (f.isAnnotationPresent(NotNullAndEmpty.class) && f.get(object) instanceof String) {
                    if (States.isNullOrEmpty((String) f.get(object))) {
                        throw new CommonException.ValidationError("Properties " + f.getName() + " is null or empty");
                    }
                }
            } catch (IllegalAccessException e) {
                LOGGER.error("Error with accessible object cause by {} ", e.getMessage());
            }
        }
        return this;
    }

    /**
     * Check not null and empty all field without annotation CanNullOrEmpty
     * Check not null all field without annotation CanEmpty
     *
     * @param object
     * @throws CommonException.ValidationError
     */
    @Override
    public IValidationTool checkCanNullWithAnnotation(Object object) {
        if (object == null) {
            throw new CommonException.ValidationError("Object is null");
        }
        for (Field f : object.getClass().getDeclaredFields()) {
            try {
                f.setAccessible(true);
                if (f.isAnnotationPresent(CanNullOrEmpty.class)) {
                    continue;
                }
                if (f.isAnnotationPresent(CanEmpty.class)) {
                    if (States.isNull(f.get(object))) {
                        throw new CommonException.ValidationError("Properties " + f.getName() + " is null");
                    }
                    continue;
                }
                if (States.isNull(f.get(object)) || States.isNullOrEmpty(String.valueOf(f.get(object)))) {
                    throw new CommonException.ValidationError("Properties " + f.getName() + " is null or empty");
                }
            } catch (IllegalAccessException e) {
                LOGGER.error("Error with accessible object cause by {} ", e.getMessage());
            }
        }
        return this;
    }

    @Override
    public IValidationTool checkSpecialCharacter(Object object) {
        if (object == null) {
            throw new CommonException.ValidationError("Object is null");
        }
        for (Field f : object.getClass().getDeclaredFields()) {
            try {
                f.setAccessible(true);
                if ((f.get(object) != null)) {
                    if (f.getType().equals(String.class)) {
                        LOGGER.debug("This is String: FIELD:{} VALUE:{}", f.getName(), f.get(object).toString());
                        String data = (String) f.get(object);
                        checkSpecialCharacter(data);
                    } else {
                        LOGGER.debug("{} :this is not string", f.getName());
                    }
                }
            } catch (IllegalAccessException e) {
                LOGGER.error("Error with accessible object cause by {} ", e.getMessage());
            }
        }
        return this;
    }

    @Override
    public IValidationTool checkSpecialCharacterAllowLink(Object object, List<String> allowList) {
        if (object == null) {
            throw new CommonException.ValidationError("Object is null");
        }
        for (Field f : object.getClass().getDeclaredFields()) {
            try {
                f.setAccessible(true);
                if ((f.get(object) != null)) {
                    if (f.getType().equals(String.class)) {
                        String data = (String) f.get(object);
                        if (allowList.contains(f.getName())) {
                            LOGGER.debug("This is link");
                            checkSpecialCharacterForLink(data);
                        } else {
                            LOGGER.debug("This is String: FIELD:{} VALUE:{}", f.getName(), f.get(object).toString());
                            checkSpecialCharacter(data);
                        }
                    } else {
                        LOGGER.debug("{} :this is not string", f.getName());
                    }
                }
            } catch (IllegalAccessException e) {
                LOGGER.error("Error with accessible object cause by {} ", e.getMessage());
            }
        }
        return this;
    }


    @Override
    public IValidationTool checkSpecialCharacter(String string) {
        String pattern = "^([a-zA-Z0-9.@ ]*)$";
        if (!string.matches(pattern))
            throw new CommonException.ValidationError("you have special character like !#$%");
        return this;
    }

    @Override
    public IValidationTool checkSpecialCharacterForLink(String string) {
        String pattern = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        if (string.isEmpty()) return this;
        if (!string.matches(pattern))
            throw new CommonException.ValidationError("you have special character like emoji");
        return this;
    }

    /**
     * Compare byte array input1 and byte array input2 and
     *
     * @param input1
     * @param input2
     * @return
     */
    @Override
    public IValidationTool compareByte(byte[] input1, byte[] input2) throws CommonException.ValidationError {
        try {
            if (!Arrays.equals(input1, input2)) {
                throw new CommonException.ValidationError("Password not match");
            }
            return this;
        } finally {
            Arrays.fill(input1, (byte) 0);
            Arrays.fill(input2, (byte) 0);
        }
    }

    @Override
    public String removeVietnameseKey(String rawStr) {
        if (!States.isNullOrEmpty(rawStr)) {
            rawStr = rawStr.toLowerCase();
            rawStr = rawStr
                    .replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a")
                    .replaceAll("[èéẹẻẽêềếệểễ]", "e")
                    .replaceAll("[ìíịỉĩ]", "i")
                    .replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o")
                    .replaceAll("[ùúụủũưừứựửữ]", "u")
                    .replaceAll("[ỳýỵỷỹ]", "y")
                    .replaceAll("đ", "d");
            return rawStr;
        }
        return "";
    }

    public JsonObject removeHtmlCharacter(JsonObject payload) {
        //REMEMBER, payload is never null
        String input = payload.toString();
        input = input.replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&amp;", "&")
                .replace("&#039;", "")
                .replace("&#034;", "");
        input = input
                .replaceAll("(?i)<script.*?>.*?</script.*?>", "")   // case 1
                .replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", "") // case 2
                .replaceAll("(?i)<.*?\\s+on.*?>.*?</.*?>", "")
                .replaceAll("(?i)<html.*?>.*?</html.*?>", "")
                .replaceAll("(?i)<html.*?>", "")
                .replaceAll("(?i)</html.*?>", "");
        return JsonConverter.toJson(input);
    }

    public void checkDeviceInfoFormat(String deviceInfo) {
        if (Strings.isNullOrEmpty(deviceInfo)) {
            throw new CommonException.ValidationError("Device info is null");
        }

        String regex = "[^:]*+:[^:]*+:[^:]*+:[^:]*+:[^:]*+";
        if (!deviceInfo.matches(regex)) {
            throw new CommonException.ValidationError("Device info missing info");
        }
    }

    @Override
    public void checkAppVersion(String appVersion) {
        if (Strings.isNullOrEmpty(appVersion)) {
            throw new CommonException.ValidationError("Device info is null");
        }
        String regex = "[^:]*+:[^:]*+";
        if (!appVersion.matches(regex)) {
            throw new CommonException.ValidationError("Device info missing info");
        }
    }

    @Override
    public void isMultiple(BigDecimal number, BigDecimal multiple) {
        if (number.compareTo(BigDecimal.ZERO) < 0) {
            throw new CommonException.ValidationError("Number is negative");
        }
        if (number.remainder(multiple).compareTo(BigDecimal.ZERO) < 0) {
            throw new CommonException.ValidationError("Number not is multiple of million");
        }
    }

    @Override
    public void checkMaxPercent(BigDecimal number, double percent, BigDecimal numberToCheck) {
        BigDecimal rate = new BigDecimal(percent);
        if (numberToCheck.compareTo(number.multiply(rate).divide(new BigDecimal(100)).setScale(-3, RoundingMode.HALF_DOWN)) > 0) {
            throw new CommonException.ValidationError("Number to check is larger than " + percent + "% of number " + number);
        }
    }

    @Override
    public void checkMinPercent(BigDecimal number, double percent, BigDecimal numberToCheck) {
        BigDecimal rate = new BigDecimal(percent);
        if (numberToCheck.compareTo(number.multiply(rate).divide(new BigDecimal(100)).setScale(0, RoundingMode.HALF_DOWN)) < 0) {
            throw new CommonException.ValidationError("Number to check is lower than " + percent + "% of number " + number);
        }
    }
}
