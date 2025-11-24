package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.Country;
import com.monsterdam.admin.domain.TaxInfo;
import com.monsterdam.admin.service.dto.CountryDTO;
import com.monsterdam.admin.service.dto.TaxInfoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TaxInfo} and its DTO {@link TaxInfoDTO}.
 */
@Mapper(componentModel = "spring")
public interface TaxInfoMapper extends EntityMapper<TaxInfoDTO, TaxInfo> {
    @Mapping(target = "country", source = "country", qualifiedByName = "countryName")
    TaxInfoDTO toDto(TaxInfo s);

    @Named("countryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CountryDTO toDtoCountryName(Country country);
}
