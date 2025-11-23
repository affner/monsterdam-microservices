package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.UserSettings;
import com.fanflip.admin.service.dto.UserSettingsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserSettings} and its DTO {@link UserSettingsDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserSettingsMapper extends EntityMapper<UserSettingsDTO, UserSettings> {}
