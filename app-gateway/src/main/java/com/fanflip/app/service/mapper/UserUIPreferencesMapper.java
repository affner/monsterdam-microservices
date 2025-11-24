package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.UserUIPreferences;
import com.monsterdam.app.service.dto.UserUIPreferencesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserUIPreferences} and its DTO {@link UserUIPreferencesDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserUIPreferencesMapper extends EntityMapper<UserUIPreferencesDTO, UserUIPreferences> {}
