package com.monsterdam.catalogs.service.mapper;

import com.monsterdam.catalogs.domain.HelpCategory;
import com.monsterdam.catalogs.domain.HelpSubcategory;
import com.monsterdam.catalogs.service.dto.HelpCategoryDTO;
import com.monsterdam.catalogs.service.dto.HelpSubcategoryDTO;
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
