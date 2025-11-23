package com.fanflip.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A HelpQuestion.
 */
@Table("help_question")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "helpquestion")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HelpQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("title")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String title;

    @Column("content")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String content;

    @NotNull(message = "must not be null")
    @Column("is_deleted")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isDeleted;

    @Transient
    @JsonIgnoreProperties(value = { "relatedArticles" }, allowSetters = true)
    private Set<HelpRelatedArticle> questions = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "questions", "category" }, allowSetters = true)
    private HelpSubcategory subcategory;

    @Column("subcategory_id")
    private Long subcategoryId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HelpQuestion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public HelpQuestion title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public HelpQuestion content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public HelpQuestion isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Set<HelpRelatedArticle> getQuestions() {
        return this.questions;
    }

    public void setQuestions(Set<HelpRelatedArticle> helpRelatedArticles) {
        this.questions = helpRelatedArticles;
    }

    public HelpQuestion questions(Set<HelpRelatedArticle> helpRelatedArticles) {
        this.setQuestions(helpRelatedArticles);
        return this;
    }

    public HelpQuestion addQuestion(HelpRelatedArticle helpRelatedArticle) {
        this.questions.add(helpRelatedArticle);
        return this;
    }

    public HelpQuestion removeQuestion(HelpRelatedArticle helpRelatedArticle) {
        this.questions.remove(helpRelatedArticle);
        return this;
    }

    public HelpSubcategory getSubcategory() {
        return this.subcategory;
    }

    public void setSubcategory(HelpSubcategory helpSubcategory) {
        this.subcategory = helpSubcategory;
        this.subcategoryId = helpSubcategory != null ? helpSubcategory.getId() : null;
    }

    public HelpQuestion subcategory(HelpSubcategory helpSubcategory) {
        this.setSubcategory(helpSubcategory);
        return this;
    }

    public Long getSubcategoryId() {
        return this.subcategoryId;
    }

    public void setSubcategoryId(Long helpSubcategory) {
        this.subcategoryId = helpSubcategory;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HelpQuestion)) {
            return false;
        }
        return getId() != null && getId().equals(((HelpQuestion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HelpQuestion{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
