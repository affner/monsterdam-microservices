package com.fanflip.catalogs.service.mapper;

import com.fanflip.catalogs.domain.HelpCategory;
import com.fanflip.catalogs.service.dto.HelpCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HelpCategory} and its DTO {@link HelpCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface HelpCategoryMapper extends EntityMapper<HelpCategoryDTO, HelpCategory> {}
