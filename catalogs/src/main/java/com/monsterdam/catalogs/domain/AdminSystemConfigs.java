package com.monsterdam.catalogs.domain;

import com.monsterdam.catalogs.domain.enumeration.ConfigurationCategory;
import com.monsterdam.catalogs.domain.enumeration.ConfigurationValueType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AdminSystemConfigs.
 */
@Entity
@Table(name = "admin_system_configs")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdminSystemConfigs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "config_key", length = 255, nullable = false)
    private String configKey;

    @NotNull
    @Column(name = "config_value", nullable = false)
    private String configValue;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "config_value_type")
    private ConfigurationValueType configValueType;

    @Enumerated(EnumType.STRING)
    @Column(name = "config_category")
    private ConfigurationCategory configCategory;

    @Lob
    @Column(name = "config_file")
    private byte[] configFile;

    @Column(name = "config_file_content_type")
    private String configFileContentType;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "is_active")
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
