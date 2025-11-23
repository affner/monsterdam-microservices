package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.AdminUserProfile;
import com.fanflip.admin.service.dto.AdminUserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AdminUserProfile} and its DTO {@link AdminUserProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdminUserProfileMapper extends EntityMapper<AdminUserProfileDTO, AdminUserProfile> {}
