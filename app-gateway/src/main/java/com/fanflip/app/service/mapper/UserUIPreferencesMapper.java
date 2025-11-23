package com.fanflip.app.service.mapper;

import com.fanflip.app.domain.UserUIPreferences;
import com.fanflip.app.service.dto.UserUIPreferencesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserUIPreferences} and its DTO {@link UserUIPreferencesDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserUIPreferencesMapper extends EntityMapper<UserUIPreferencesDTO, UserUIPreferences> {}
