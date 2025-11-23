package com.fanflip.catalogs.service.mapper;

import com.fanflip.catalogs.domain.Country;
import com.fanflip.catalogs.service.dto.CountryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Country} and its DTO {@link CountryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CountryMapper extends EntityMapper<CountryDTO, Country> {}
