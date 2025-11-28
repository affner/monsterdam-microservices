package com.monsterdam.catalogs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A HelpCategory.
 */
@Entity
@Table(name = "help_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HelpCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "questions", "category" }, allowSetters = true)
    private Set<HelpSubcategory> subCategories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HelpCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public HelpCategory name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public HelpCategory isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Set<HelpSubcategory> getSubCategories() {
        return this.subCategories;
    }

    public void setSubCategories(Set<HelpSubcategory> helpSubcategories) {
        if (this.subCategories != null) {
            this.subCategories.forEach(i -> i.setCategory(null));
        }
        if (helpSubcategories != null) {
            helpSubcategories.forEach(i -> i.setCategory(this));
        }
        this.subCategories = helpSubcategories;
    }

    public HelpCategory subCategories(Set<HelpSubcategory> helpSubcategories) {
        this.setSubCategories(helpSubcategories);
        return this;
    }

    public HelpCategory addSubCategories(HelpSubcategory helpSubcategory) {
        this.subCategories.add(helpSubcategory);
        helpSubcategory.setCategory(this);
        return this;
    }

    public HelpCategory removeSubCategories(HelpSubcategory helpSubcategory) {
        this.subCategories.remove(helpSubcategory);
        helpSubcategory.setCategory(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HelpCategory)) {
            return false;
        }
        return getId() != null && getId().equals(((HelpCategory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HelpCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
