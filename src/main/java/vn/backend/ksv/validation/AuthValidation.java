package vn.backend.ksv.validation;

import com.google.inject.Inject;
import vn.backend.ksv.common.LogAdapter;
import vn.backend.ksv.common.exception.AuthException;
import vn.backend.ksv.common.exception.CommonException;
import vn.backend.ksv.common.exception.UserException;
import vn.backend.ksv.common.util.Generator;
import vn.backend.ksv.common.util.IValidationTool;

import java.security.PrivateKey;
import java.util.Base64;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/10/23
 * Time: 3:52 PM
 */
public class AuthValidation implements IAuthValidation{
    private final LogAdapter LOGGER = LogAdapter.newInstance(this.getClass());

    private PrivateKey privateKey;
    private IValidationTool validationTool;

    @Inject
    public AuthValidation(PrivateKey privateKey,
                          IValidationTool validationTool) {
        this.privateKey = privateKey;
        this.validationTool = validationTool;
    }

    @Override
    public void validatePassword(String inputPassword, byte[] salt, byte[] userPassword) {
        try {
            byte[] rawPassword = Generator.decrypt(Base64.getDecoder().decode(inputPassword), privateKey);
            rawPassword = Generator.generatePassword(Generator.convertByteToChar(rawPassword), salt);
            validationTool.compareByte(rawPassword, userPassword);
        } catch (CommonException.GenerateException e) {
            throw new CommonException.GenerateException(e.getMessage());
        } catch (CommonException.ValidationError e) {
            throw new CommonException.ValidationError("Password not match");
        }
    }

    @Override
    public void validateAdminPassword(String inputPassword, byte[] salt, byte[] userPassword) {

    }

    @Override
    public void generatePassword(String userId, String inputPassword) {

    }

    @Override
    public void validateStatus(Integer userStatus) {
//        if(!StaticEnum.UserStatus.ACTIVE.getCode().equals(userStatus)){
//            throw new CommonException.ForbiddenException("User has been ban!");
//        }
    }
}
