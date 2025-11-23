package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.PaymentProvider;
import com.fanflip.admin.service.dto.PaymentProviderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentProvider} and its DTO {@link PaymentProviderDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentProviderMapper extends EntityMapper<PaymentProviderDTO, PaymentProvider> {}
