package com.fanflip.catalogs.service.mapper;

import com.fanflip.catalogs.domain.AdminEmailConfigs;
import com.fanflip.catalogs.service.dto.AdminEmailConfigsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AdminEmailConfigs} and its DTO {@link AdminEmailConfigsDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdminEmailConfigsMapper extends EntityMapper<AdminEmailConfigsDTO, AdminEmailConfigs> {}
