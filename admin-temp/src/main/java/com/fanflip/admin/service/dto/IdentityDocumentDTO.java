package com.fanflip.admin.service.dto;

import com.fanflip.admin.domain.enumeration.DocumentStatus;
import com.fanflip.admin.domain.enumeration.DocumentType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.fanflip.admin.domain.IdentityDocument} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IdentityDocumentDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String documentName;

    private String documentDescription;

    private DocumentStatus documentStatus;

    private DocumentType documentType;

    @Lob
    private byte[] fileDocument;

    private String fileDocumentContentType;

    @NotNull(message = "must not be null")
    private String fileDocumentS3Key;

    @NotNull(message = "must not be null")
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    private IdentityDocumentReviewDTO review;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentDescription() {
        return documentDescription;
    }

    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
    }

    public DocumentStatus getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(DocumentStatus documentStatus) {
        this.documentStatus = documentStatus;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public byte[] getFileDocument() {
        return fileDocument;
    }

    public void setFileDocument(byte[] fileDocument) {
        this.fileDocument = fileDocument;
    }

    public String getFileDocumentContentType() {
        return fileDocumentContentType;
    }

    public void setFileDocumentContentType(String fileDocumentContentType) {
        this.fileDocumentContentType = fileDocumentContentType;
    }

    public String getFileDocumentS3Key() {
        return fileDocumentS3Key;
    }

    public void setFileDocumentS3Key(String fileDocumentS3Key) {
        this.fileDocumentS3Key = fileDocumentS3Key;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public IdentityDocumentReviewDTO getReview() {
        return review;
    }

    public void setReview(IdentityDocumentReviewDTO review) {
        this.review = review;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IdentityDocumentDTO)) {
            return false;
        }

        IdentityDocumentDTO identityDocumentDTO = (IdentityDocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, identityDocumentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IdentityDocumentDTO{" +
            "id=" + getId() +
            ", documentName='" + getDocumentName() + "'" +
            ", documentDescription='" + getDocumentDescription() + "'" +
            ", documentStatus='" + getDocumentStatus() + "'" +
            ", documentType='" + getDocumentType() + "'" +
            ", fileDocument='" + getFileDocument() + "'" +
            ", fileDocumentS3Key='" + getFileDocumentS3Key() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", review=" + getReview() +
            "}";
    }
}
