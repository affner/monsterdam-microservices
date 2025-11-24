package com.monsterdam.catalogs.service.mapper;

import com.monsterdam.catalogs.domain.HelpRelatedArticle;
import com.monsterdam.catalogs.service.dto.HelpRelatedArticleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HelpRelatedArticle} and its DTO {@link HelpRelatedArticleDTO}.
 */
@Mapper(componentModel = "spring")
public interface HelpRelatedArticleMapper extends EntityMapper<HelpRelatedArticleDTO, HelpRelatedArticle> {}
