package vn.backend.ksv.common.pojo.common;

import com.google.gson.annotations.SerializedName;
import vn.backend.ksv.common.constant.staticEnum.StaticEnum;

import java.util.List;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 10:32 AM
 */
public class HeaderInternalRequest {

    private String token;
    private String userId;
    private String phone;
    private String email;
    private String deviceId;
    private Integer accountType;
    @SerializedName("collation-id")
    private String collationId;
    private String language;
    private String customerName;
    private String username;
    private Integer sex;
    private StaticEnum.TokenType tokenType;
    @SerializedName("X-Real-IP")
    private String deviceIPAddress;
    private String appId;
    private String userTitleId;
    private String projectId;
    private String levelId;
    private Long expireAt;
    private List<String> decentralizationList;
    private String companyId;

    public String getToken() {
        return token;
    }

    public HeaderInternalRequest setToken(String token) {
        this.token = token;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public HeaderInternalRequest setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public HeaderInternalRequest setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public HeaderInternalRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public HeaderInternalRequest setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public HeaderInternalRequest setAccountType(Integer accountType) {
        this.accountType = accountType;
        return this;
    }

    public String getCollationId() {
        return collationId;
    }

    public HeaderInternalRequest setCollationId(String collationId) {
        this.collationId = collationId;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public HeaderInternalRequest setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getCustomerName() {
        return customerName;
    }

    public HeaderInternalRequest setCustomerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public HeaderInternalRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public Integer getSex() {
        return sex;
    }

    public HeaderInternalRequest setSex(Integer sex) {
        this.sex = sex;
        return this;
    }

    public StaticEnum.TokenType getTokenType() {
        return tokenType;
    }

    public HeaderInternalRequest setTokenType(StaticEnum.TokenType tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public String getDeviceIPAddress() {
        return deviceIPAddress;
    }

    public HeaderInternalRequest setDeviceIPAddress(String deviceIPAddress) {
        this.deviceIPAddress = deviceIPAddress;
        return this;
    }

    public String getAppId() {
        return appId;
    }

    public HeaderInternalRequest setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getUserTitleId() {
        return userTitleId;
    }

    public HeaderInternalRequest setUserTitleId(String userTitleId) {
        this.userTitleId = userTitleId;
        return this;
    }

    public String getProjectId() {
        return projectId;
    }

    public HeaderInternalRequest setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getLevelId() {
        return levelId;
    }

    public HeaderInternalRequest setLevelId(String levelId) {
        this.levelId = levelId;
        return this;
    }

    public Long getExpireAt() {
        return expireAt;
    }

    public HeaderInternalRequest setExpireAt(Long expireAt) {
        this.expireAt = expireAt;
        return this;
    }

    public List<String> getDecentralizationList() {
        return decentralizationList;
    }

    public HeaderInternalRequest setDecentralizationList(List<String> decentralizationList) {
        this.decentralizationList = decentralizationList;
        return this;
    }

    public String getCompanyId() {
        return companyId;
    }

    public HeaderInternalRequest setCompanyId(String companyId) {
        this.companyId = companyId;
        return this;
    }
}
