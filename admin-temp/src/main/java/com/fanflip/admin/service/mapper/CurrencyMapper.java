package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.Currency;
import com.fanflip.admin.service.dto.CurrencyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Currency} and its DTO {@link CurrencyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CurrencyMapper extends EntityMapper<CurrencyDTO, Currency> {}
