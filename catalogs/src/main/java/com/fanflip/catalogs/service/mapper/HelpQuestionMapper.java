package com.monsterdam.catalogs.service.mapper;

import com.monsterdam.catalogs.domain.HelpQuestion;
import com.monsterdam.catalogs.domain.HelpRelatedArticle;
import com.monsterdam.catalogs.domain.HelpSubcategory;
import com.monsterdam.catalogs.service.dto.HelpQuestionDTO;
import com.monsterdam.catalogs.service.dto.HelpRelatedArticleDTO;
import com.monsterdam.catalogs.service.dto.HelpSubcategoryDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HelpQuestion} and its DTO {@link HelpQuestionDTO}.
 */
@Mapper(componentModel = "spring")
public interface HelpQuestionMapper extends EntityMapper<HelpQuestionDTO, HelpQuestion> {
    @Mapping(target = "questions", source = "questions", qualifiedByName = "helpRelatedArticleIdSet")
    @Mapping(target = "subcategory", source = "subcategory", qualifiedByName = "helpSubcategoryId")
    HelpQuestionDTO toDto(HelpQuestion s);

    @Mapping(target = "removeQuestion", ignore = true)
    HelpQuestion toEntity(HelpQuestionDTO helpQuestionDTO);

    @Named("helpRelatedArticleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HelpRelatedArticleDTO toDtoHelpRelatedArticleId(HelpRelatedArticle helpRelatedArticle);

    @Named("helpRelatedArticleIdSet")
    default Set<HelpRelatedArticleDTO> toDtoHelpRelatedArticleIdSet(Set<HelpRelatedArticle> helpRelatedArticle) {
        return helpRelatedArticle.stream().map(this::toDtoHelpRelatedArticleId).collect(Collectors.toSet());
    }

    @Named("helpSubcategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HelpSubcategoryDTO toDtoHelpSubcategoryId(HelpSubcategory helpSubcategory);
}
