package com.monsterdam.catalogs.service.mapper;

import com.monsterdam.catalogs.domain.PayoutMethod;
import com.monsterdam.catalogs.service.dto.PayoutMethodDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PayoutMethod} and its DTO {@link PayoutMethodDTO}.
 */
@Mapper(componentModel = "spring")
public interface PayoutMethodMapper extends EntityMapper<PayoutMethodDTO, PayoutMethod> {}
