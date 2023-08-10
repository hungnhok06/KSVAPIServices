package vn.backend.ksv.common.auth;

import vn.backend.ksv.common.anotation.NotNull;
import vn.backend.ksv.common.anotation.NotNullAndEmpty;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 10:39 AM
 */
public class TokenAuthInfo {

    @NotNullAndEmpty
    private String userId;
    private String deviceId;
    private String appId;
    private String projectId;
    private String userTitleId;
    private String roleId;
    private Integer tokenType;
    private Integer deviceStatus;
    private String companyId;
    private String levelId;
    @NotNull
    private Long expireAt;
    @NotNull
    private Long activeTime;
    private Long createdAt;

    public String getUserId() {
        return userId;
    }

    public TokenAuthInfo setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public TokenAuthInfo setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getAppId() {
        return appId;
    }

    public TokenAuthInfo setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getProjectId() {
        return projectId;
    }

    public TokenAuthInfo setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getUserTitleId() {
        return userTitleId;
    }

    public TokenAuthInfo setUserTitleId(String userTitleId) {
        this.userTitleId = userTitleId;
        return this;
    }

    public String getRoleId() {
        return roleId;
    }

    public TokenAuthInfo setRoleId(String roleId) {
        this.roleId = roleId;
        return this;
    }

    public Integer getTokenType() {
        return tokenType;
    }

    public TokenAuthInfo setTokenType(Integer tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public Integer getDeviceStatus() {
        return deviceStatus;
    }

    public TokenAuthInfo setDeviceStatus(Integer deviceStatus) {
        this.deviceStatus = deviceStatus;
        return this;
    }

    public String getCompanyId() {
        return companyId;
    }

    public TokenAuthInfo setCompanyId(String companyId) {
        this.companyId = companyId;
        return this;
    }

    public String getLevelId() {
        return levelId;
    }

    public TokenAuthInfo setLevelId(String levelId) {
        this.levelId = levelId;
        return this;
    }

    public Long getExpireAt() {
        return expireAt;
    }

    public TokenAuthInfo setExpireAt(Long expireAt) {
        this.expireAt = expireAt;
        return this;
    }

    public Long getActiveTime() {
        return activeTime;
    }

    public TokenAuthInfo setActiveTime(Long activeTime) {
        this.activeTime = activeTime;
        return this;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public TokenAuthInfo setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
