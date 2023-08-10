package vn.backend.ksv.common.reponse;

import vn.backend.ksv.common.pojo.user.UserProfileDetails;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/10/23
 * Time: 3:57 PM
 */
public class LoginRes {
    private String token;
    private Long expireAt;
    private Long refreshAt;
    private UserProfileDetails profileDetails;

    public String getToken() {
        return token;
    }

    public LoginRes setToken(String token) {
        this.token = token;
        return this;
    }

    public Long getExpireAt() {
        return expireAt;
    }

    public LoginRes setExpireAt(Long expireAt) {
        this.expireAt = expireAt;
        return this;
    }

    public Long getRefreshAt() {
        return refreshAt;
    }

    public LoginRes setRefreshAt(Long refreshAt) {
        this.refreshAt = refreshAt;
        return this;
    }

    public UserProfileDetails getProfileDetails() {
        return profileDetails;
    }

    public LoginRes setProfileDetails(UserProfileDetails profileDetails) {
        this.profileDetails = profileDetails;
        return this;
    }
}
