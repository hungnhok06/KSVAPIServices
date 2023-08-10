package vn.backend.ksv.common.util;

import vn.backend.ksv.common.LogAdapter;
import vn.backend.ksv.common.anotation.Order;
import vn.backend.ksv.common.exception.CommonException;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.KeySpec;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 11:41 AM
 */
public class Generator {
    private static final LogAdapter LOGGER = LogAdapter.newInstance(Generator.class);

    private static final String CHAR_LIST = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    private static final int RANDOM_STRING_LENGTH = 9;

    public static String generateOTP(Integer length) {
        String result = "";
        for (int i = 0; i < length; i++) {
            Integer number = ThreadLocalRandom.current().nextInt(0, 10);
            result += String.valueOf(number);
        }
        if (result.isEmpty()) {
            throw new CommonException.GenerateException("Generate code failed");
        }
        return result;
    }

    public static String generateDumpOTP(Integer length) {
        String result = "";
        for (int i = 0; i < length; i++) {
            Integer number = 9;
            result += String.valueOf(number);
        }
        if (result.isEmpty()) {
            throw new CommonException.GenerateException("Generate code failed");
        }
        return result;
    }

    public static String generate() {
        vn.backend.ksv.common.util.UUID idGenerate = new vn.backend.ksv.common.util.UUID();
        LOGGER.debug(idGenerate.toString().replaceAll("-", ""));
        return idGenerate.toString().replaceAll("-", "");
    }

    public static String generateSHA() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest salt = MessageDigest.getInstance("SHA-256");
        salt.update(new vn.backend.ksv.common.util.UUID().toJavaUUID().randomUUID().toString().getBytes("UTF-8"));
        return bytesToHex(salt.digest());
    }

    public static String bytesToHex(byte[] hashInBytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static String generateDisplayId(Integer length, Integer suffix) {
        String pattern = "%0" + length + "d";
        LOGGER.debug(pattern);
        return String.format(pattern, suffix);
    }

    public static String generateSaltKey() {
        String salt = "nnuahc";
        try {
            SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();
            salt = Base64.getEncoder().encodeToString(secretKey.getEncoded()).substring(0, 4);
        } catch (Exception e) {
            LOGGER.error("Generate salt key fail, use default salt key");
        }
        return salt;
    }

    public static byte[] generateSalt() {
        try {
            final Random r = new SecureRandom();
            byte[] salt = new byte[64];
            r.nextBytes(salt);
            return salt;
        } catch (Exception e) {
            LOGGER.error("Generate salt key fail cause by {}", e.getMessage());
            throw new CommonException.GenerateException("Error on generate salt key");
        }
    }

    public static String generateUserForgotTokenDisplay() {
        String result = "";
        Integer StringLength = 8;
        for (int i = 0; i < StringLength; i++) {
            Integer number = ThreadLocalRandom.current().nextInt(1, 3 + 1);//random 1->3, need +1 to including 3
            switch (number) {
                case 1:
                    Integer characterRandomNum = ThreadLocalRandom.current().nextInt(48, 57 + 1); //number 0->9
                    result += String.valueOf(Character.toChars(characterRandomNum));
                    break;
                case 2:
                    Integer characterRandomLower = ThreadLocalRandom.current().nextInt(97, 122 + 1); //number 0->9
                    result += String.valueOf(Character.toChars(characterRandomLower));
                    break;
                default:
                    Integer characterRandomUpper = ThreadLocalRandom.current().nextInt(65, 90 + 1); //number 0->9
                    result += String.valueOf(Character.toChars(characterRandomUpper));
                    break;
            }
        }
        return result;
    }

    public static String generateLogID(String cif, Integer type, Long currentTime) {
        LOGGER.info("Start generate log id with cif {}, type {}, currentTime {}", cif, type, currentTime);
        try {
            StringBuffer md5 = new StringBuffer();
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update((cif + type + currentTime).getBytes());
                byte byteData[] = md.digest();
                for (int i = 0; i < byteData.length; i++) {
                    md5.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
                }
                return md5.toString();
            } catch (NoSuchAlgorithmException e) {
                LOGGER.error(e.getMessage());
                throw new CommonException.GenerateException("Generate log id failed");
            }
        } catch (Exception e) {
            LOGGER.error("Generate log id fail!");
            throw new CommonException.GenerateException("Generate log id failed");
        }
    }

    public static byte[] decrypt(byte[] buffer, PrivateKey privateKey) throws CommonException.GenerateException {
        try {
            Cipher rsa;
            rsa = Cipher.getInstance("RSA");
            rsa.init(Cipher.DECRYPT_MODE, privateKey);
            return rsa.doFinal(buffer);
        } catch (Exception e) {
            LOGGER.error("Error on decrypt password cause by {}", e.getMessage());
            throw new CommonException.GenerateException("Error on decrypt password.");
        }
    }

    public static byte[] generatePassword(char[] password, byte[] salt) {
        try {
            KeySpec spec = new PBEKeySpec(password, salt, 65536, 512);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            return f.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            LOGGER.error("Error on generate password cause by {}", e.getMessage());
            throw new CommonException.GenerateException("Error on generate password");
        }
    }

    public static char[] convertByteToChar(byte[] bytes) {
        char[] result = new char[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            result[i] = (char) bytes[i];
        }
        return result;
    }

    /**
     * Mask number flow pattern xxxxxxxxx## with # is number not mask. other character will be show.
     *
     * @param number
     * @param format
     * @return
     */
    public static String generateMaskNumberSimple(String number, String format) {
        try {
            if (number.length() != format.length()) {
                throw new CommonException.ValidationError("Number and format length no equals");
            }
            if (number.length() > 255) {
                throw new CommonException.ValidationError("Number too long");
            }
            StringBuilder maskedNumber = new StringBuilder();
            for (int i = 0; i < format.length(); i++) {
                if (format.charAt(i) != '#') {
                    maskedNumber.append(format.charAt(i));
                    continue;
                }
                maskedNumber.append(number.charAt(i));
            }
            return maskedNumber.toString();
        } catch (Exception e) {
            LOGGER.error("Generate mask number simple pattern failed cause by {}", e.getMessage());
            throw new CommonException.GenerateException("Generate mask number simple pattern failed");
        }
    }

    /**
     * Mask suffix number with length
     *
     * @param input
     * @param unMaskLength
     * @return
     */
    public static String generateMaskSuffix(String input, int unMaskLength) {
        try {
            return repeatChar("*", input.length() - unMaskLength).concat(input.substring(input.length() - unMaskLength));
        } catch (Exception e) {
            LOGGER.error("Error on generate mask suffix cause by {}", e.getMessage());
            throw new CommonException.GenerateException("Error on generate mask suffix");
        }
    }

    /**
     * Mask email with rule:
     * First letter not mask.
     * Not mask 1 letter before and 1 letter after @
     * Not mask 1 letter before and letter in domain of mail begin from first dot
     * Ex: chaudeptrai.18@gmail.com.vn
     *
     * @param input
     * @return
     */
    public static String generateMaskEmail(String input) {
        try {
            String name = input.substring(0, input.lastIndexOf("@"));
            //name = chaudeptrai.18
            String domain = input.substring(input.lastIndexOf("@") + 1);
            //domain = gmail.com.vn
            name = name.substring(0, 1).concat(repeatChar("*", name.length() - 2)).concat(name.substring(name.length() - 1));
            //name.substring(0,1) = c, repeatChar("*", 14 - 2) = ************, name.substring(name.length() - 1) = 8
            int indexOfDot = domain.indexOf(".");
            //indexOfDot = 5
            String unMasking = domain.substring(indexOfDot - 1);
            //unMasking = l.com.vn
            domain = domain.substring(0, 1).concat(repeatChar("*", domain.length() - unMasking.length() - 1)).concat(unMasking);
            //domain.substring(0,1) = g, repeatChar = gmail.com.vn(12) - l.com.vn(8) - g(1) = mai(3), unMasking = l.com.vn
            return name.concat("@").concat(domain);
            //result = c************8@g***l.com.vn
        } catch (Exception e) {
            LOGGER.error("Error on generate mask email cause by {}", e.getMessage());
            throw new CommonException.GenerateException("Error on generate mask email");
        }
    }

    private static String repeatChar(String x, int length) {
        //If jdk 11 use this function.
//        return new x.repeat(length);
        return new String(new char[length]).replace("\0", x);
    }

    public static String generateDisplayAccount(String prefix, String suffix, Integer id, Integer number) {
        try {
            Integer numberOfId = number - prefix.length() - suffix.length();
            String pattern = "%0" + numberOfId + "d";
            String strId = String.format(pattern, id);
            return prefix + strId + suffix;
        } catch (Exception e) {
            LOGGER.error("Generate display account number fail!");
            throw new CommonException.GenerateException("Generate display account number fail!");
        }
    }

    public static Boolean generateIsEndRecord(Integer start, Integer end, Integer total) {
        try {
            if (end >= total || end == 0) {
                return true;
            }
            if (start + end >= total) {
                return true;
            }
            return false;
        } catch (Exception e) {
            LOGGER.error("Generate is last record fail cause by {}", e.getMessage());
            throw new CommonException.GenerateException("Generate is last record fail");
        }
    }

    public static String generateSha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String generateSha512(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String generateRandomCharacter(int length) {
        if (length < 0) {
            throw new CommonException.GenerateException("Token length invalid");
        }
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            switch (random.nextInt(3)) {
                case 1:
                    //Random number
                    result.append(String.valueOf(random.nextInt(10)));
                    break;
                case 2:
                    //Random lower case
                    result.append(String.valueOf(Character.toChars(random.nextInt(26) + 97)));
                    break;
                default:
                    //Random upper case
                    result.append(String.valueOf(Character.toChars(random.nextInt(26) + 65)));
                    break;
            }
        }
        return result.toString();
    }

    public static String generateRandomUppercaseCharacter(int length) {
        if (length < 0) {
            throw new CommonException.GenerateException("Token length invalid");
        }
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            switch (random.nextInt(2)) {
                case 1:
                    //Random number
                    result.append(String.valueOf(random.nextInt(10)));
                    break;
                default:
                    //Random upper case
                    result.append(String.valueOf(Character.toChars(random.nextInt(26) + 65)));
                    break;
            }
        }
        return result.toString();
    }

    public static String generateRandomNumber() {

        StringBuffer randStr = new StringBuffer();
        for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
            int number = getRandomNumber();
            char ch = CHAR_LIST.charAt(number);
            randStr.append(ch);
        }
        return randStr.toString();
    }

    private static int getRandomNumber() {
        int randomInt = 0;
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(CHAR_LIST.length());
        if (randomInt - 1 == -1) {
            return randomInt;
        } else {
            return randomInt - 1;
        }
    }

    public static String generateIdLog(LogType type) {
        String prefix = "";
        switch (type) {
            case CORE:
            case END_USER:
            case AUTO:
                return type.getType() + generate();
            default:
                return generate();
        }

    }

    public static Random generateRandom() {
        return new Random();
    }

    public static String hashMD5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            return DatatypeConverter.printHexBinary(digest).toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public enum LogType {
        END_USER("USER_"),
        CORE("CORE_"),
        AUTO("AUTO_");
        private String type;

        LogType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    public static String generateUserId() {
        String result = "";
        for (int i = 0; i < 6; i++) {
            Integer number = ThreadLocalRandom.current().nextInt(0, 10);
            result += String.valueOf(number);
        }
        if (result.isEmpty()) {
            throw new CommonException.GenerateException("Generate code failed");
        }
        return result;
    }

    public static Long generateProcessId() {
        return new UUID().getMostSignificantBits() & Long.MAX_VALUE;
    }

    public static String getContentHMAC(Object object, String separate) {
        if (object == null) {
            throw new CommonException.ValidationError("Object is null");
        }
        try {
            StringBuilder result = new StringBuilder();
            List<Field> fields = Arrays.stream(object.getClass().getDeclaredFields()).filter(item -> {
                if (item.isAnnotationPresent(Order.class)) {
                    return true;
                }
                return false;
            }).collect(Collectors.toList());
            Collections.sort(fields, Comparator.comparingInt(o -> o.getAnnotation(Order.class).value()));
            for (Field f : fields) {
                f.setAccessible(true);
                System.out.println("Name field " + f.getName());
                if (f.getAnnotation(Order.class).isParent()) {
                    result.append(f.getName()).append("=").append(getContentHMAC(f.get(object), separate)).append(separate);
                } else
                    result.append(f.getName()).append("=").append(f.get(object)).append(separate);
            }
            result.deleteCharAt(result.length() - 1);
            System.out.println("Object after HMAC: " + result.toString());
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException.UnknownException("Error get hmac content");
        }
    }

    public static String getContentHMACWithoutFieldName(Object object, String separate) {
        if (object == null) {
            throw new CommonException.ValidationError("Object is null");
        }
        try {
            StringBuilder result = new StringBuilder();
            List<Field> fields = Arrays.stream(object.getClass().getDeclaredFields()).filter(item -> {
                if (item.isAnnotationPresent(Order.class))
                    return true;
                return false;
            }).collect(Collectors.toList());
            Collections.sort(fields, Comparator.comparingInt(o -> o.getAnnotation(Order.class).value()));
            for (Field f : fields) {
                f.setAccessible(true);
                result.append(f.get(object)).append(separate);
            }
            result.deleteCharAt(result.length() - 1);
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException.UnknownException("Error get hmac content");
        }
    }

    public static String getHMACSha512(String keyHMAC, String content) {
        try {
            final byte[] byteKey = keyHMAC.getBytes(StandardCharsets.UTF_8);
            Mac sha512Hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, "HmacSHA512");
            sha512Hmac.init(keySpec);
            byte[] macData = sha512Hmac.doFinal(content.getBytes(StandardCharsets.UTF_8));

            // Can either base64 encode or put it right into hex
            return Base64.getEncoder().encodeToString(macData);
            //result = bytesToHex(macData);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new CommonException.UnknownException("Unknown exception");
        }
    }

    public static String getHMACSha256(String keyHMAC, String content) {
        try {
            final byte[] byteKey = keyHMAC.getBytes(StandardCharsets.UTF_8);
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, "HmacSHA256");
            sha256Hmac.init(keySpec);
            byte[] macData = sha256Hmac.doFinal(content.getBytes(StandardCharsets.UTF_8));

            // Can either base64 encode or put it right into hex
            return Base64.getEncoder().encodeToString(macData);
            //result = bytesToHex(macData);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new CommonException.UnknownException("Unknown exception");
        }
    }

    public static String encrypt(String plainText, PublicKey publicKey) {
        try {
            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(UTF_8));

            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
