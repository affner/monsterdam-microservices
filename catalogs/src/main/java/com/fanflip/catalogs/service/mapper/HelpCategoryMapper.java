package com.monsterdam.catalogs.service.mapper;

import com.monsterdam.catalogs.domain.HelpCategory;
import com.monsterdam.catalogs.service.dto.HelpCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HelpCategory} and its DTO {@link HelpCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface HelpCategoryMapper extends EntityMapper<HelpCategoryDTO, HelpCategory> {}
