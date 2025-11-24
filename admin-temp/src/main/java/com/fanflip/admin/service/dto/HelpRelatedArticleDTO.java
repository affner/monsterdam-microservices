package com.monsterdam.admin.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.admin.domain.HelpRelatedArticle} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HelpRelatedArticleDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String title;

    @Lob
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HelpRelatedArticleDTO)) {
            return false;
        }

        HelpRelatedArticleDTO helpRelatedArticleDTO = (HelpRelatedArticleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, helpRelatedArticleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HelpRelatedArticleDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
