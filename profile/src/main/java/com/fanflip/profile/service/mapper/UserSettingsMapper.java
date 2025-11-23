package com.fanflip.profile.service.mapper;

import com.fanflip.profile.domain.UserSettings;
import com.fanflip.profile.service.dto.UserSettingsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserSettings} and its DTO {@link UserSettingsDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserSettingsMapper extends EntityMapper<UserSettingsDTO, UserSettings> {}
