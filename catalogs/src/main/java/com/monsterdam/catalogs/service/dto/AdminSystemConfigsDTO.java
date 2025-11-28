package com.monsterdam.catalogs.service.dto;

import com.monsterdam.catalogs.domain.enumeration.ConfigurationCategory;
import com.monsterdam.catalogs.domain.enumeration.ConfigurationValueType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.catalogs.domain.AdminSystemConfigs} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdminSystemConfigsDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String configKey;

    @NotNull
    private String configValue;

    @Size(max = 500)
    private String description;

    private ConfigurationValueType configValueType;

    private ConfigurationCategory configCategory;

    @Lob
    private byte[] configFile;

    private String configFileContentType;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    private Boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ConfigurationValueType getConfigValueType() {
        return configValueType;
    }

    public void setConfigValueType(ConfigurationValueType configValueType) {
        this.configValueType = configValueType;
    }

    public ConfigurationCategory getConfigCategory() {
        return configCategory;
    }

    public void setConfigCategory(ConfigurationCategory configCategory) {
        this.configCategory = configCategory;
    }

    public byte[] getConfigFile() {
        return configFile;
    }

    public void setConfigFile(byte[] configFile) {
        this.configFile = configFile;
    }

    public String getConfigFileContentType() {
        return configFileContentType;
    }

    public void setConfigFileContentType(String configFileContentType) {
        this.configFileContentType = configFileContentType;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdminSystemConfigsDTO)) {
            return false;
        }

        AdminSystemConfigsDTO adminSystemConfigsDTO = (AdminSystemConfigsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, adminSystemConfigsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdminSystemConfigsDTO{" +
            "id=" + getId() +
            ", configKey='" + getConfigKey() + "'" +
            ", configValue='" + getConfigValue() + "'" +
            ", description='" + getDescription() + "'" +
            ", configValueType='" + getConfigValueType() + "'" +
            ", configCategory='" + getConfigCategory() + "'" +
            ", configFile='" + getConfigFile() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
