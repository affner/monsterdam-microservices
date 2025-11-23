package com.fanflip.catalogs.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.fanflip.catalogs.domain.HelpSubcategory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HelpSubcategoryDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Boolean isDeleted;

    private HelpCategoryDTO category;

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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public HelpCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(HelpCategoryDTO category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HelpSubcategoryDTO)) {
            return false;
        }

        HelpSubcategoryDTO helpSubcategoryDTO = (HelpSubcategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, helpSubcategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HelpSubcategoryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", category=" + getCategory() +
            "}";
    }
}
