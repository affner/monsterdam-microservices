package com.fanflip.catalogs.service.mapper;

import com.fanflip.catalogs.domain.PaymentMethod;
import com.fanflip.catalogs.service.dto.PaymentMethodDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentMethod} and its DTO {@link PaymentMethodDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMethodMapper extends EntityMapper<PaymentMethodDTO, PaymentMethod> {}
