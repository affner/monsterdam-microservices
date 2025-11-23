package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.HelpCategory;
import com.fanflip.admin.domain.HelpSubcategory;
import com.fanflip.admin.service.dto.HelpCategoryDTO;
import com.fanflip.admin.service.dto.HelpSubcategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HelpSubcategory} and its DTO {@link HelpSubcategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface HelpSubcategoryMapper extends EntityMapper<HelpSubcategoryDTO, HelpSubcategory> {
    @Mapping(target = "category", source = "category", qualifiedByName = "helpCategoryId")
    HelpSubcategoryDTO toDto(HelpSubcategory s);

    @Named("helpCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HelpCategoryDTO toDtoHelpCategoryId(HelpCategory helpCategory);
}
