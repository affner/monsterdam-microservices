package com.fanflip.catalogs.service.mapper;

import com.fanflip.catalogs.domain.PaymentProvider;
import com.fanflip.catalogs.service.dto.PaymentProviderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentProvider} and its DTO {@link PaymentProviderDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentProviderMapper extends EntityMapper<PaymentProviderDTO, PaymentProvider> {}
