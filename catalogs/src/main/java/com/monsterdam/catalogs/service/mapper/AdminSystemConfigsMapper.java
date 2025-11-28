package com.monsterdam.catalogs.service.mapper;

import com.monsterdam.catalogs.domain.AdminSystemConfigs;
import com.monsterdam.catalogs.service.dto.AdminSystemConfigsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AdminSystemConfigs} and its DTO {@link AdminSystemConfigsDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdminSystemConfigsMapper extends EntityMapper<AdminSystemConfigsDTO, AdminSystemConfigs> {}
