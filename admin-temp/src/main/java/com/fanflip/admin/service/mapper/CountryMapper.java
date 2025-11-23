package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.Country;
import com.fanflip.admin.service.dto.CountryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Country} and its DTO {@link CountryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CountryMapper extends EntityMapper<CountryDTO, Country> {}
