package com.fanflip.profile.service.mapper;

import com.fanflip.profile.domain.UserLite;
import com.fanflip.profile.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserLite} and its DTO {@link UserLiteDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserLiteMapper extends EntityMapper<UserLiteDTO, UserLite> {}
