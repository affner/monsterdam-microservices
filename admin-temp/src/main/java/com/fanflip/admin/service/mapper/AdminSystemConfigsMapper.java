package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.AdminSystemConfigs;
import com.monsterdam.admin.service.dto.AdminSystemConfigsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AdminSystemConfigs} and its DTO {@link AdminSystemConfigsDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdminSystemConfigsMapper extends EntityMapper<AdminSystemConfigsDTO, AdminSystemConfigs> {}
