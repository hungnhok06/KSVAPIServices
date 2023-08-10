package vn.backend.ksv.common.request;

import vn.backend.ksv.common.anotation.NotNullAndEmpty;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/10/23
 * Time: 3:45 PM
 */
public class LoginRequest {

    @NotNullAndEmpty
    private String username;
    @NotNullAndEmpty
    private String password;

    public String getUsername() {
        return username;
    }

    public LoginRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginRequest setPassword(String password) {
        this.password = password;
        return this;
    }

}
