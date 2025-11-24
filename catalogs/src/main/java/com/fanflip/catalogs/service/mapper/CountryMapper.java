package com.monsterdam.catalogs.service.mapper;

import com.monsterdam.catalogs.domain.Country;
import com.monsterdam.catalogs.service.dto.CountryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Country} and its DTO {@link CountryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CountryMapper extends EntityMapper<CountryDTO, Country> {}
