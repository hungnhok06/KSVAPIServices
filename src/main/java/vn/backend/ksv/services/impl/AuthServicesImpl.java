package vn.backend.ksv.services.impl;

import com.google.inject.Inject;
import vn.backend.ksv.common.Configuration;
import vn.backend.ksv.common.LogAdapter;
import vn.backend.ksv.common.constant.key.CodeResponse;
import vn.backend.ksv.common.exception.AuthException;
import vn.backend.ksv.common.exception.CommonException;
import vn.backend.ksv.common.exception.DataException;
import vn.backend.ksv.common.exception.UserException;
import vn.backend.ksv.common.pojo.common.CommonRequest;
import vn.backend.ksv.common.pojo.common.RestfulCommonResponse;
import vn.backend.ksv.common.pojo.common.RestfulFailureResponse;
import vn.backend.ksv.common.pojo.common.RestfulSuccessResponse;
import vn.backend.ksv.common.pojo.user.UserProfileDetails;
import vn.backend.ksv.common.reponse.LoginRes;
import vn.backend.ksv.common.request.LoginRequest;
import vn.backend.ksv.common.util.Generator;
import vn.backend.ksv.common.util.IValidationTool;
import vn.backend.ksv.common.util.JsonConverter;
import vn.backend.ksv.config.Config;
import vn.backend.ksv.handle.ILoginHandle;
import vn.backend.ksv.repository.generator.Tables;
import vn.backend.ksv.repository.generator.tables.records.UserRecord;
import vn.backend.ksv.repository.sources.IUserRepo;
import vn.backend.ksv.services.IAuthServices;
import vn.backend.ksv.validation.IAuthValidation;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 9:49 AM
 */
public class AuthServicesImpl implements IAuthServices {

    private final LogAdapter LOGGER = LogAdapter.newInstance(this.getClass());

    private IValidationTool validationTool;
    private IUserRepo userRepo;
    private Config config;
    private PrivateKey privateKey;
    private IAuthValidation authValidation;
    private ILoginHandle loginHandle;
    @Inject
    public AuthServicesImpl(IValidationTool validationTool,
                            Config config,
                            IUserRepo userRepo,
                            PrivateKey privateKey,
                            ILoginHandle loginHandle,
                            IAuthValidation authValidation
    ){
        this.validationTool = validationTool;
        this.config = config;
        this.userRepo = userRepo;
        this.loginHandle = loginHandle;
        this.privateKey = privateKey;
        this.authValidation = authValidation;

    }
    @Override
    public void initKSVAccountRoot() {
        try {
            LOGGER.info("Start check account account IT");
            if (!userRepo.checkAccountRoot()) {
                LOGGER.info("Account IT not existed");
                LOGGER.info("Create account root");
                String username = "root";
                String password = "Aa@123123";
                String passwordMD5 = Generator.hashMD5(password).toLowerCase();
                X509EncodedKeySpec spec = new X509EncodedKeySpec(Configuration.decodeByteFile(config.getTokenConfig().getRsaPublicFile()));
                KeyFactory kf = KeyFactory.getInstance("RSA");
                PublicKey publicKey = kf.generatePublic(spec);
                String passwordEncrypt = Generator.encrypt(passwordMD5, publicKey);
                LOGGER.info("Username: {}, Password: {}", username, passwordEncrypt);
                createAdmin(username,passwordEncrypt);

                LOGGER.info("Create account IT success");
            } else
                LOGGER.info("Account IT existed");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Init IT error");
        }
    }

    @Override
    public void createAdmin(String username, String password) {
        try {
            LOGGER.info("Start create authorization user {}", username);
            byte[] salt = Generator.generateSalt();
            byte[] rawPassword = Generator.decrypt(Base64.getDecoder().decode(password), privateKey);
            rawPassword = Generator.generatePassword(Generator.convertByteToChar(rawPassword), salt);
            UserRecord userRecord = new UserRecord();
            userRecord.setUserid(Generator.generateUserIdAdmin(userRepo.count(Tables.USER)))
                    .setUsername(username)
                    .setPassword(rawPassword)
                    .setSalt(salt);
            userRepo.save(userRecord);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error on generate password cause by {}", e.getMessage());
            throw new CommonException.GenerateException("Failed to generate password");
        }
    }

    @Override
    public void checkPassword(UserRecord record, String password) {
        try {
            LOGGER.trace("Auth record to login {}", record);
            authValidation.validatePassword(password, record.getSalt(), record.getPassword());
            LOGGER.info("Auth services response success");
        } catch (CommonException.ValidationError e) {
            LOGGER.info("Auth services response wrong password");
            throw new UserException.PasswordNotMatchException("Wrong password");
        } catch (CommonException.GenerateException e) {
            LOGGER.info("GenerateException");
            throw new UserException.PasswordNotMatchException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Unknown error on check password ");
            throw new CommonException.UnknownException("Unknown error on check password");
        }
    }

    @Override
    public RestfulCommonResponse adminLogin(CommonRequest request) {
        try {
            LOGGER.info("Start back office login with data {}", request.getData());
            LoginRequest data = JsonConverter.fromJson(request.getData(), LoginRequest.class);
            validationTool.checkNotNullWithAnnotation(data);
            UserRecord record = userRepo.getAccountUserByUserName(data.getUsername());
//            switch (StaticEnum.AccountBackofficeStatus.safeValueOf(record.getStatus())) {
//                case NOT_ACTIVATED:
//                    return new RestfulFailureResponse().setResponse(CodeResponse.ClientErrorCode.ACCOUNT_NOT_ACTIVATED);
//                case LOCKED:
//                    return new RestfulFailureResponse().setResponse(CodeResponse.ClientErrorCode.ACCOUNT_LOCKED);
//                default:
//                    break;
//            }
            checkPassword(record, data.getPassword());

            LoginRes response = loginHandle.generateBackofficeToken(record, null);
            response.setProfileDetails(new UserProfileDetails()
                    .setCusName(record.getUsername())
                    .setDecentralizationList(null));
            return new RestfulSuccessResponse()
                    .setData(JsonConverter.toJsonElement(response));
        } catch (UserException.BlockedException e) {
            LOGGER.warn(e.getMessage());
            return new RestfulFailureResponse().setResponse(CodeResponse.ClientErrorCode.ACCOUNT_LOCKED);
        } catch (DataException.NotFoundException | UserException.PasswordNotMatchException e) {
            LOGGER.warn(e.getMessage());
            return new RestfulFailureResponse().setResponse(CodeResponse.ClientErrorCode.WRONG_USERNAME_PASSWORD);
        } catch (CommonException.GenerateException e) {
            LOGGER.warn(e.getMessage());
            return new RestfulFailureResponse().setResponse(CodeResponse.ServerErrorCode.INVALID_SSL);
        } catch (CommonException.ValidationError e) {
            LOGGER.warn("Bad request cause by {}", e.getMessage());
            return new RestfulFailureResponse().setResponse(CodeResponse.ClientErrorCode.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error("Error on backoffice login cause by {}", e.getMessage());
            return new RestfulFailureResponse().setResponse(CodeResponse.ServerErrorCode.SERVER_ERROR);
        }
    }
}
