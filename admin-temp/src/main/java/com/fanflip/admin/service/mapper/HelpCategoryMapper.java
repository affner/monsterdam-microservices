package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.HelpCategory;
import com.monsterdam.admin.service.dto.HelpCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HelpCategory} and its DTO {@link HelpCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface HelpCategoryMapper extends EntityMapper<HelpCategoryDTO, HelpCategory> {}
