package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.UserLite;
import com.fanflip.admin.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserLite} and its DTO {@link UserLiteDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserLiteMapper extends EntityMapper<UserLiteDTO, UserLite> {}
