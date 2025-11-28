package com.monsterdam.profile.service.mapper;

import com.monsterdam.profile.domain.UserLite;
import com.monsterdam.profile.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserLite} and its DTO {@link UserLiteDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserLiteMapper extends EntityMapper<UserLiteDTO, UserLite> {}
