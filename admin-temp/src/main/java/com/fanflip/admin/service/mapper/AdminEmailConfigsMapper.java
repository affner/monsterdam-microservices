package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.AdminEmailConfigs;
import com.monsterdam.admin.service.dto.AdminEmailConfigsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AdminEmailConfigs} and its DTO {@link AdminEmailConfigsDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdminEmailConfigsMapper extends EntityMapper<AdminEmailConfigsDTO, AdminEmailConfigs> {}
