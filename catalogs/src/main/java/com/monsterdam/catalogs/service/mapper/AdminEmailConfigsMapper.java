package com.monsterdam.catalogs.service.mapper;

import com.monsterdam.catalogs.domain.AdminEmailConfigs;
import com.monsterdam.catalogs.service.dto.AdminEmailConfigsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AdminEmailConfigs} and its DTO {@link AdminEmailConfigsDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdminEmailConfigsMapper extends EntityMapper<AdminEmailConfigsDTO, AdminEmailConfigs> {}
