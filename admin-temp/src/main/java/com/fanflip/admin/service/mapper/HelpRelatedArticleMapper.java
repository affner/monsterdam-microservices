package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.HelpRelatedArticle;
import com.monsterdam.admin.service.dto.HelpRelatedArticleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HelpRelatedArticle} and its DTO {@link HelpRelatedArticleDTO}.
 */
@Mapper(componentModel = "spring")
public interface HelpRelatedArticleMapper extends EntityMapper<HelpRelatedArticleDTO, HelpRelatedArticle> {}
