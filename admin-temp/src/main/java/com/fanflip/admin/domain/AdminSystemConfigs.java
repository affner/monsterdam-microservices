package com.fanflip.admin.domain;

import com.fanflip.admin.domain.enumeration.ConfigurationCategory;
import com.fanflip.admin.domain.enumeration.ConfigurationValueType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A AdminSystemConfigs.
 */
@Table("admin_system_configs")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "adminsystemconfigs")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdminSystemConfigs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 255)
    @Column("config_key")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String configKey;

    @NotNull(message = "must not be null")
    @Column("config_value")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String configValue;

    @Size(max = 500)
    @Column("description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Column("config_value_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ConfigurationValueType configValueType;

    @Column("config_category")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ConfigurationCategory configCategory;

    @Column("config_file")
    private byte[] configFile;

    @Column("config_file_content_type")
    private String configFileContentType;

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

    @Column("is_active")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AdminSystemConfigs id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigKey() {
        return this.configKey;
    }

    public AdminSystemConfigs configKey(String configKey) {
        this.setConfigKey(configKey);
        return this;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return this.configValue;
    }

    public AdminSystemConfigs configValue(String configValue) {
        this.setConfigValue(configValue);
        return this;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getDescription() {
        return this.description;
    }

    public AdminSystemConfigs description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ConfigurationValueType getConfigValueType() {
        return this.configValueType;
    }

    public AdminSystemConfigs configValueType(ConfigurationValueType configValueType) {
        this.setConfigValueType(configValueType);
        return this;
    }

    public void setConfigValueType(ConfigurationValueType configValueType) {
        this.configValueType = configValueType;
    }

    public ConfigurationCategory getConfigCategory() {
        return this.configCategory;
    }

    public AdminSystemConfigs configCategory(ConfigurationCategory configCategory) {
        this.setConfigCategory(configCategory);
        return this;
    }

    public void setConfigCategory(ConfigurationCategory configCategory) {
        this.configCategory = configCategory;
    }

    public byte[] getConfigFile() {
        return this.configFile;
    }

    public AdminSystemConfigs configFile(byte[] configFile) {
        this.setConfigFile(configFile);
        return this;
    }

    public void setConfigFile(byte[] configFile) {
        this.configFile = configFile;
    }

    public String getConfigFileContentType() {
        return this.configFileContentType;
    }

    public AdminSystemConfigs configFileContentType(String configFileContentType) {
        this.configFileContentType = configFileContentType;
        return this;
    }

    public void setConfigFileContentType(String configFileContentType) {
        this.configFileContentType = configFileContentType;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public AdminSystemConfigs createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public AdminSystemConfigs lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public AdminSystemConfigs createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public AdminSystemConfigs lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public AdminSystemConfigs isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdminSystemConfigs)) {
            return false;
        }
        return getId() != null && getId().equals(((AdminSystemConfigs) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdminSystemConfigs{" +
            "id=" + getId() +
            ", configKey='" + getConfigKey() + "'" +
            ", configValue='" + getConfigValue() + "'" +
            ", description='" + getDescription() + "'" +
            ", configValueType='" + getConfigValueType() + "'" +
            ", configCategory='" + getConfigCategory() + "'" +
            ", configFile='" + getConfigFile() + "'" +
            ", configFileContentType='" + getConfigFileContentType() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
