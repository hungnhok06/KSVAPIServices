package vn.backend.ksv.common.pojo.user;

import java.util.List;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/10/23
 * Time: 4:01 PM
 */
public class UserProfileDetails {
    private String cif;
    private String userId;
    private String cusName;
    private String username;
    private String license;
    private String phone;
    private String email;
    private Long dob;
    private String avatar;
    private Integer gender;
    private Integer position;
    private String address;
    private String cityName;
    private String districtsName;
    private String wardsName;
    private String quarterName;
    private String subQuarterName;
    private Integer role;
    private List<String> decentralizationList;
    private String companyId;

    public String getCif() {
        return cif;
    }

    public UserProfileDetails setCif(String cif) {
        this.cif = cif;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public UserProfileDetails setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getCusName() {
        return cusName;
    }

    public UserProfileDetails setCusName(String cusName) {
        this.cusName = cusName;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserProfileDetails setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getLicense() {
        return license;
    }

    public UserProfileDetails setLicense(String license) {
        this.license = license;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserProfileDetails setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserProfileDetails setEmail(String email) {
        this.email = email;
        return this;
    }

    public Long getDob() {
        return dob;
    }

    public UserProfileDetails setDob(Long dob) {
        this.dob = dob;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public UserProfileDetails setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public Integer getGender() {
        return gender;
    }

    public UserProfileDetails setGender(Integer gender) {
        this.gender = gender;
        return this;
    }

    public Integer getPosition() {
        return position;
    }

    public UserProfileDetails setPosition(Integer position) {
        this.position = position;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public UserProfileDetails setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getCityName() {
        return cityName;
    }

    public UserProfileDetails setCityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public String getDistrictsName() {
        return districtsName;
    }

    public UserProfileDetails setDistrictsName(String districtsName) {
        this.districtsName = districtsName;
        return this;
    }

    public String getWardsName() {
        return wardsName;
    }

    public UserProfileDetails setWardsName(String wardsName) {
        this.wardsName = wardsName;
        return this;
    }

    public String getQuarterName() {
        return quarterName;
    }

    public UserProfileDetails setQuarterName(String quarterName) {
        this.quarterName = quarterName;
        return this;
    }

    public String getSubQuarterName() {
        return subQuarterName;
    }

    public UserProfileDetails setSubQuarterName(String subQuarterName) {
        this.subQuarterName = subQuarterName;
        return this;
    }

    public Integer getRole() {
        return role;
    }

    public UserProfileDetails setRole(Integer role) {
        this.role = role;
        return this;
    }

    public List<String> getDecentralizationList() {
        return decentralizationList;
    }

    public UserProfileDetails setDecentralizationList(List<String> decentralizationList) {
        this.decentralizationList = decentralizationList;
        return this;
    }

    public String getCompanyId() {
        return companyId;
    }

    public UserProfileDetails setCompanyId(String companyId) {
        this.companyId = companyId;
        return this;
    }
}
