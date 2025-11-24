package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.UserSettings;
import com.monsterdam.admin.service.dto.UserSettingsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserSettings} and its DTO {@link UserSettingsDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserSettingsMapper extends EntityMapper<UserSettingsDTO, UserSettings> {}
