package com.monsterdam.profile.service.mapper;

import com.monsterdam.profile.domain.UserSettings;
import com.monsterdam.profile.service.dto.UserSettingsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserSettings} and its DTO {@link UserSettingsDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserSettingsMapper extends EntityMapper<UserSettingsDTO, UserSettings> {}
