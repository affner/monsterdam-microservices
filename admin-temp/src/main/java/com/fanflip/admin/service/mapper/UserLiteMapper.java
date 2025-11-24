package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.UserLite;
import com.monsterdam.admin.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserLite} and its DTO {@link UserLiteDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserLiteMapper extends EntityMapper<UserLiteDTO, UserLite> {}
