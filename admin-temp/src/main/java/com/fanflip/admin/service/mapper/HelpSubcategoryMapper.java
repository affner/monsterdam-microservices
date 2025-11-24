package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.HelpCategory;
import com.monsterdam.admin.domain.HelpSubcategory;
import com.monsterdam.admin.service.dto.HelpCategoryDTO;
import com.monsterdam.admin.service.dto.HelpSubcategoryDTO;
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
