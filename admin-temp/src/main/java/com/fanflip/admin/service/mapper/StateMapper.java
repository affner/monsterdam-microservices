package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.Country;
import com.monsterdam.admin.domain.State;
import com.monsterdam.admin.service.dto.CountryDTO;
import com.monsterdam.admin.service.dto.StateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link State} and its DTO {@link StateDTO}.
 */
@Mapper(componentModel = "spring")
public interface StateMapper extends EntityMapper<StateDTO, State> {
    @Mapping(target = "country", source = "country", qualifiedByName = "countryName")
    StateDTO toDto(State s);

    @Named("countryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CountryDTO toDtoCountryName(Country country);
}
