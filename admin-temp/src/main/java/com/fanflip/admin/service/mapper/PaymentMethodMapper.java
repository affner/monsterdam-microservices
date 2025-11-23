package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.PaymentMethod;
import com.fanflip.admin.service.dto.PaymentMethodDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentMethod} and its DTO {@link PaymentMethodDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMethodMapper extends EntityMapper<PaymentMethodDTO, PaymentMethod> {}
