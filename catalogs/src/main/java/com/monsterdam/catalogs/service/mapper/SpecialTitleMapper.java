package com.monsterdam.catalogs.service.mapper;

import com.monsterdam.catalogs.domain.SpecialTitle;
import com.monsterdam.catalogs.service.dto.SpecialTitleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SpecialTitle} and its DTO {@link SpecialTitleDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpecialTitleMapper extends EntityMapper<SpecialTitleDTO, SpecialTitle> {}
