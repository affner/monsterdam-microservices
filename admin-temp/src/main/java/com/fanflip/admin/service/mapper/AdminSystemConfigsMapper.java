package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.AdminSystemConfigs;
import com.fanflip.admin.service.dto.AdminSystemConfigsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AdminSystemConfigs} and its DTO {@link AdminSystemConfigsDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdminSystemConfigsMapper extends EntityMapper<AdminSystemConfigsDTO, AdminSystemConfigs> {}
