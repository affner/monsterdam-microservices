package com.fanflip.catalogs.service.mapper;

import com.fanflip.catalogs.domain.Currency;
import com.fanflip.catalogs.service.dto.CurrencyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Currency} and its DTO {@link CurrencyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CurrencyMapper extends EntityMapper<CurrencyDTO, Currency> {}
