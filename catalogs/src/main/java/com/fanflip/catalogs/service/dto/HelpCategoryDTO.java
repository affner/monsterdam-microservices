package com.monsterdam.catalogs.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.catalogs.domain.HelpCategory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HelpCategoryDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Boolean isDeleted;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HelpCategoryDTO)) {
            return false;
        }

        HelpCategoryDTO helpCategoryDTO = (HelpCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, helpCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HelpCategoryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
