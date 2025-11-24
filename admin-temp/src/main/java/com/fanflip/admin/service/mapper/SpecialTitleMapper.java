package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.SpecialTitle;
import com.monsterdam.admin.service.dto.SpecialTitleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SpecialTitle} and its DTO {@link SpecialTitleDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpecialTitleMapper extends EntityMapper<SpecialTitleDTO, SpecialTitle> {}
