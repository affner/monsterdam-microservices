package com.fanflip.catalogs.service.mapper;

import com.fanflip.catalogs.domain.AdminSystemConfigs;
import com.fanflip.catalogs.service.dto.AdminSystemConfigsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AdminSystemConfigs} and its DTO {@link AdminSystemConfigsDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdminSystemConfigsMapper extends EntityMapper<AdminSystemConfigsDTO, AdminSystemConfigs> {}
