package com.fanflip.catalogs.service.mapper;

import com.fanflip.catalogs.domain.HelpRelatedArticle;
import com.fanflip.catalogs.service.dto.HelpRelatedArticleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HelpRelatedArticle} and its DTO {@link HelpRelatedArticleDTO}.
 */
@Mapper(componentModel = "spring")
public interface HelpRelatedArticleMapper extends EntityMapper<HelpRelatedArticleDTO, HelpRelatedArticle> {}
