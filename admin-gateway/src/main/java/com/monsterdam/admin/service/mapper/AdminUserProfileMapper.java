package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.AdminUserProfile;
import com.monsterdam.admin.service.dto.AdminUserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AdminUserProfile} and its DTO {@link AdminUserProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdminUserProfileMapper extends EntityMapper<AdminUserProfileDTO, AdminUserProfile> {}
