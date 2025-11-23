package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.HelpRelatedArticle;
import com.fanflip.admin.service.dto.HelpRelatedArticleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HelpRelatedArticle} and its DTO {@link HelpRelatedArticleDTO}.
 */
@Mapper(componentModel = "spring")
public interface HelpRelatedArticleMapper extends EntityMapper<HelpRelatedArticleDTO, HelpRelatedArticle> {}
