package com.monsterdam.catalogs.service.mapper;

import com.monsterdam.catalogs.domain.Currency;
import com.monsterdam.catalogs.service.dto.CurrencyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Currency} and its DTO {@link CurrencyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CurrencyMapper extends EntityMapper<CurrencyDTO, Currency> {}
