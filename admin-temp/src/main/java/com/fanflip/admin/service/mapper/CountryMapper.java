package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.Country;
import com.monsterdam.admin.service.dto.CountryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Country} and its DTO {@link CountryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CountryMapper extends EntityMapper<CountryDTO, Country> {}
