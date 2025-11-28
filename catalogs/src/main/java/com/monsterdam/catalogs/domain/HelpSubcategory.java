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
 * A HelpSubcategory.
 */
@Entity
@Table(name = "help_subcategory")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HelpSubcategory implements Serializable {

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subcategory")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "questions", "subcategory" }, allowSetters = true)
    private Set<HelpQuestion> questions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "subCategories" }, allowSetters = true)
    private HelpCategory category;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HelpSubcategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public HelpSubcategory name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public HelpSubcategory isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Set<HelpQuestion> getQuestions() {
        return this.questions;
    }

    public void setQuestions(Set<HelpQuestion> helpQuestions) {
        if (this.questions != null) {
            this.questions.forEach(i -> i.setSubcategory(null));
        }
        if (helpQuestions != null) {
            helpQuestions.forEach(i -> i.setSubcategory(this));
        }
        this.questions = helpQuestions;
    }

    public HelpSubcategory questions(Set<HelpQuestion> helpQuestions) {
        this.setQuestions(helpQuestions);
        return this;
    }

    public HelpSubcategory addQuestions(HelpQuestion helpQuestion) {
        this.questions.add(helpQuestion);
        helpQuestion.setSubcategory(this);
        return this;
    }

    public HelpSubcategory removeQuestions(HelpQuestion helpQuestion) {
        this.questions.remove(helpQuestion);
        helpQuestion.setSubcategory(null);
        return this;
    }

    public HelpCategory getCategory() {
        return this.category;
    }

    public void setCategory(HelpCategory helpCategory) {
        this.category = helpCategory;
    }

    public HelpSubcategory category(HelpCategory helpCategory) {
        this.setCategory(helpCategory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HelpSubcategory)) {
            return false;
        }
        return getId() != null && getId().equals(((HelpSubcategory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HelpSubcategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
