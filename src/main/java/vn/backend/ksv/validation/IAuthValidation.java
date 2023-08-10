package vn.backend.ksv.validation;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/10/23
 * Time: 3:51 PM
 */
public interface IAuthValidation {

    void validatePassword(String inputPassword, byte[] salt, byte[] userPassword);

    void validateAdminPassword(String inputPassword, byte[] salt, byte[] userPassword);

    void generatePassword(String userId, String inputPassword);

    void validateStatus(Integer userStatus);
}
