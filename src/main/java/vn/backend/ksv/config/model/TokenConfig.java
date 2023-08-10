package vn.backend.ksv.config.model;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/8/23
 * Time: 4:13 PM
 */
public class TokenConfig {
    private String rsaPrivateFile;
    private String rsaPublicFile;
    private Integer loginFailedAttempts;
    private Integer activeTime;
    private Integer timeToLive;

    public String getRsaPrivateFile() {
        return rsaPrivateFile;
    }

    public String getRsaPublicFile() {
        return rsaPublicFile;
    }

    public Integer getLoginFailedAttempts() {
        return loginFailedAttempts;
    }

    public Integer getActiveTime() {
        return activeTime;
    }

    public Integer getTimeToLive() {
        return timeToLive;
    }
}
