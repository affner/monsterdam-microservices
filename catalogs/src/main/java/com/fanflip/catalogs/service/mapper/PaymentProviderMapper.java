package com.monsterdam.catalogs.service.mapper;

import com.monsterdam.catalogs.domain.PaymentProvider;
import com.monsterdam.catalogs.service.dto.PaymentProviderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentProvider} and its DTO {@link PaymentProviderDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentProviderMapper extends EntityMapper<PaymentProviderDTO, PaymentProvider> {}
