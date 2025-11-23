package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.Country;
import com.fanflip.admin.domain.TaxInfo;
import com.fanflip.admin.service.dto.CountryDTO;
import com.fanflip.admin.service.dto.TaxInfoDTO;
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
