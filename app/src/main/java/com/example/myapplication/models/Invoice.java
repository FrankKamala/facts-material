
package com.example.myapplication.models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Invoice implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("supplier_id")
    @Expose
    private Integer supplierId;
    @SerializedName("buyer_id")
    @Expose
    private Integer buyerId;
    @SerializedName("creator_id")
    @Expose
    private Integer creatorId;
    @SerializedName("approver_id")
    @Expose
    private Integer approverId;
    @SerializedName("invoice_status")
    @Expose
    private Integer invoiceStatus;
    @SerializedName("invoice_type")
    @Expose
    private Integer invoiceType;
    @SerializedName("invoice_amount")
    @Expose
    private Integer invoiceAmount;
    @SerializedName("due_date")
    @Expose
    private String dueDate;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    private final static long serialVersionUID = 1937195599566218600L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Invoice() {
    }

    /**
     * 
     * @param createdAt
     * @param supplierId
     * @param approverId
     * @param dueDate
     * @param creatorId
     * @param invoiceType
     * @param invoiceAmount
     * @param id
     * @param buyerId
     * @param invoiceStatus
     * @param updatedAt
     */
    public Invoice(Integer id, Integer supplierId, Integer buyerId, Integer creatorId, Integer approverId, Integer invoiceStatus, Integer invoiceType, Integer invoiceAmount, String dueDate, String createdAt, String updatedAt) {
        super();
        this.id = id;
        this.supplierId = supplierId;
        this.buyerId = buyerId;
        this.creatorId = creatorId;
        this.approverId = approverId;
        this.invoiceStatus = invoiceStatus;
        this.invoiceType = invoiceType;
        this.invoiceAmount = invoiceAmount;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getApproverId() {
        return approverId;
    }

    public void setApproverId(Integer approverId) {
        this.approverId = approverId;
    }

    public Integer getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(Integer invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public Integer getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }

    public Integer getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(Integer invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
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
