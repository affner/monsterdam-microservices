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
 * A HelpRelatedArticle.
 */
@Table("help_related_article")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "helprelatedarticle")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HelpRelatedArticle implements Serializable {

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

    @Transient
    @JsonIgnoreProperties(value = { "questions", "subcategory" }, allowSetters = true)
    private Set<HelpQuestion> relatedArticles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HelpRelatedArticle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public HelpRelatedArticle title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public HelpRelatedArticle content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<HelpQuestion> getRelatedArticles() {
        return this.relatedArticles;
    }

    public void setRelatedArticles(Set<HelpQuestion> helpQuestions) {
        if (this.relatedArticles != null) {
            this.relatedArticles.forEach(i -> i.removeQuestion(this));
        }
        if (helpQuestions != null) {
            helpQuestions.forEach(i -> i.addQuestion(this));
        }
        this.relatedArticles = helpQuestions;
    }

    public HelpRelatedArticle relatedArticles(Set<HelpQuestion> helpQuestions) {
        this.setRelatedArticles(helpQuestions);
        return this;
    }

    public HelpRelatedArticle addRelatedArticle(HelpQuestion helpQuestion) {
        this.relatedArticles.add(helpQuestion);
        helpQuestion.getQuestions().add(this);
        return this;
    }

    public HelpRelatedArticle removeRelatedArticle(HelpQuestion helpQuestion) {
        this.relatedArticles.remove(helpQuestion);
        helpQuestion.getQuestions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HelpRelatedArticle)) {
            return false;
        }
        return getId() != null && getId().equals(((HelpRelatedArticle) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HelpRelatedArticle{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
