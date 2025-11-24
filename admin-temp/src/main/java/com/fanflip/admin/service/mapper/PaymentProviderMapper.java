package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.PaymentProvider;
import com.monsterdam.admin.service.dto.PaymentProviderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentProvider} and its DTO {@link PaymentProviderDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentProviderMapper extends EntityMapper<PaymentProviderDTO, PaymentProvider> {}
