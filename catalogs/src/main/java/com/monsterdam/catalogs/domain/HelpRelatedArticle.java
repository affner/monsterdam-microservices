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
 * A HelpRelatedArticle.
 */
@Entity
@Table(name = "help_related_article")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HelpRelatedArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "questions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
