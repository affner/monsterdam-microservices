package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.HelpCategory;
import com.fanflip.admin.service.dto.HelpCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HelpCategory} and its DTO {@link HelpCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface HelpCategoryMapper extends EntityMapper<HelpCategoryDTO, HelpCategory> {}
