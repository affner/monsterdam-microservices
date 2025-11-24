package com.monsterdam.catalogs.service.mapper;

import com.monsterdam.catalogs.domain.PaymentMethod;
import com.monsterdam.catalogs.service.dto.PaymentMethodDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentMethod} and its DTO {@link PaymentMethodDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMethodMapper extends EntityMapper<PaymentMethodDTO, PaymentMethod> {}
