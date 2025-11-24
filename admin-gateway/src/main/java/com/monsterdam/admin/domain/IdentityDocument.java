package com.monsterdam.admin.domain;

import com.monsterdam.admin.domain.enumeration.DocumentStatus;
import com.monsterdam.admin.domain.enumeration.DocumentType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A IdentityDocument.
 */
@Table("identity_document")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "identitydocument")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IdentityDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("document_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String documentName;

    @Column("document_description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String documentDescription;

    @Column("document_status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private DocumentStatus documentStatus;

    @Column("document_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private DocumentType documentType;

    @Column("file_document")
    private byte[] fileDocument;

    @NotNull
    @Column("file_document_content_type")
    private String fileDocumentContentType;

    @NotNull(message = "must not be null")
    @Column("file_document_s_3_key")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String fileDocumentS3Key;

    @NotNull(message = "must not be null")
    @Column("created_date")
    private Instant createdDate;

    @Column("last_modified_date")
    private Instant lastModifiedDate;

    @Column("created_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String createdBy;

    @Column("last_modified_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String lastModifiedBy;

    @Transient
    @JsonIgnoreProperties(value = { "ticket", "documents", "observations" }, allowSetters = true)
    private IdentityDocumentReview review;

    @Column("review_id")
    private Long reviewId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IdentityDocument id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentName() {
        return this.documentName;
    }

    public IdentityDocument documentName(String documentName) {
        this.setDocumentName(documentName);
        return this;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentDescription() {
        return this.documentDescription;
    }

    public IdentityDocument documentDescription(String documentDescription) {
        this.setDocumentDescription(documentDescription);
        return this;
    }

    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
    }

    public DocumentStatus getDocumentStatus() {
        return this.documentStatus;
    }

    public IdentityDocument documentStatus(DocumentStatus documentStatus) {
        this.setDocumentStatus(documentStatus);
        return this;
    }

    public void setDocumentStatus(DocumentStatus documentStatus) {
        this.documentStatus = documentStatus;
    }

    public DocumentType getDocumentType() {
        return this.documentType;
    }

    public IdentityDocument documentType(DocumentType documentType) {
        this.setDocumentType(documentType);
        return this;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public byte[] getFileDocument() {
        return this.fileDocument;
    }

    public IdentityDocument fileDocument(byte[] fileDocument) {
        this.setFileDocument(fileDocument);
        return this;
    }

    public void setFileDocument(byte[] fileDocument) {
        this.fileDocument = fileDocument;
    }

    public String getFileDocumentContentType() {
        return this.fileDocumentContentType;
    }

    public IdentityDocument fileDocumentContentType(String fileDocumentContentType) {
        this.fileDocumentContentType = fileDocumentContentType;
        return this;
    }

    public void setFileDocumentContentType(String fileDocumentContentType) {
        this.fileDocumentContentType = fileDocumentContentType;
    }

    public String getFileDocumentS3Key() {
        return this.fileDocumentS3Key;
    }

    public IdentityDocument fileDocumentS3Key(String fileDocumentS3Key) {
        this.setFileDocumentS3Key(fileDocumentS3Key);
        return this;
    }

    public void setFileDocumentS3Key(String fileDocumentS3Key) {
        this.fileDocumentS3Key = fileDocumentS3Key;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public IdentityDocument createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public IdentityDocument lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public IdentityDocument createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public IdentityDocument lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public IdentityDocumentReview getReview() {
        return this.review;
    }

    public void setReview(IdentityDocumentReview identityDocumentReview) {
        this.review = identityDocumentReview;
        this.reviewId = identityDocumentReview != null ? identityDocumentReview.getId() : null;
    }

    public IdentityDocument review(IdentityDocumentReview identityDocumentReview) {
        this.setReview(identityDocumentReview);
        return this;
    }

    public Long getReviewId() {
        return this.reviewId;
    }

    public void setReviewId(Long identityDocumentReview) {
        this.reviewId = identityDocumentReview;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IdentityDocument)) {
            return false;
        }
        return getId() != null && getId().equals(((IdentityDocument) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IdentityDocument{" +
            "id=" + getId() +
            ", documentName='" + getDocumentName() + "'" +
            ", documentDescription='" + getDocumentDescription() + "'" +
            ", documentStatus='" + getDocumentStatus() + "'" +
            ", documentType='" + getDocumentType() + "'" +
            ", fileDocument='" + getFileDocument() + "'" +
            ", fileDocumentContentType='" + getFileDocumentContentType() + "'" +
            ", fileDocumentS3Key='" + getFileDocumentS3Key() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            "}";
    }
}
