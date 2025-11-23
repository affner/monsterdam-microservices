package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.AdminEmailConfigs;
import com.fanflip.admin.service.dto.AdminEmailConfigsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AdminEmailConfigs} and its DTO {@link AdminEmailConfigsDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdminEmailConfigsMapper extends EntityMapper<AdminEmailConfigsDTO, AdminEmailConfigs> {}
