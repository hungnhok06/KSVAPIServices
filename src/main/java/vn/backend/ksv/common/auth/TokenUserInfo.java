package vn.backend.ksv.common.auth;

import java.util.List;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 10:42 AM
 */
public class TokenUserInfo {

    private String userId;
    private String username;
    private String email;
    private String phone;
    private String deviceId;
    private Integer accountType;
    private String language;
    private String customerName;
    private Integer sex;
    private List<String> decentralizationList;

    public String getUserId() {
        return userId;
    }

    public TokenUserInfo setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public TokenUserInfo setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public TokenUserInfo setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public TokenUserInfo setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public TokenUserInfo setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public TokenUserInfo setAccountType(Integer accountType) {
        this.accountType = accountType;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public TokenUserInfo setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getCustomerName() {
        return customerName;
    }

    public TokenUserInfo setCustomerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public Integer getSex() {
        return sex;
    }

    public TokenUserInfo setSex(Integer sex) {
        this.sex = sex;
        return this;
    }

    public List<String> getDecentralizationList() {
        return decentralizationList;
    }

    public TokenUserInfo setDecentralizationList(List<String> decentralizationList) {
        this.decentralizationList = decentralizationList;
        return this;
    }
}
