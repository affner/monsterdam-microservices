package com.fanflip.admin.service.dto;

import com.fanflip.admin.domain.enumeration.AssetType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.fanflip.admin.domain.Asset} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssetDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String name;

    @NotNull(message = "must not be null")
    private BigDecimal value;

    private LocalDate acquisitionDate;

    @NotNull(message = "must not be null")
    private AssetType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public AssetType getType() {
        return type;
    }

    public void setType(AssetType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetDTO)) {
            return false;
        }

        AssetDTO assetDTO = (AssetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", value=" + getValue() +
            ", acquisitionDate='" + getAcquisitionDate() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
