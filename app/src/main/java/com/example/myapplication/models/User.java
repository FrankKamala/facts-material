
package com.example.myapplication.models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("role")
    @Expose
    private Integer role;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("company_id")
    @Expose
    private Integer companyId;
    @SerializedName("company_role")
    @Expose
    private Integer companyRole;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("email_verified_at")
    @Expose
    private Object emailVerifiedAt;
    @SerializedName("api_token")
    @Expose
    private String apiToken;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    private final static long serialVersionUID = 610521815286926217L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public User() {
    }

    /**
     * 
     * @param createdAt
     * @param companyId
     * @param companyRole
     * @param role
     * @param apiToken
     * @param phone
     * @param emailVerifiedAt
     * @param imageUrl
     * @param name
     * @param id
     * @param email
     * @param updatedAt
     */
    public User(Integer id, Integer role, String phone, String imageUrl, Integer companyId, Integer companyRole, String name, String email, Object emailVerifiedAt, String apiToken, String createdAt, String updatedAt) {
        super();
        this.id = id;
        this.role = role;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.companyId = companyId;
        this.companyRole = companyRole;
        this.name = name;
        this.email = email;
        this.emailVerifiedAt = emailVerifiedAt;
        this.apiToken = apiToken;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCompanyRole() {
        return companyRole;
    }

    public void setCompanyRole(Integer companyRole) {
        this.companyRole = companyRole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(Object emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
