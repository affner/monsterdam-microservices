package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.PayoutMethod;
import com.monsterdam.admin.service.dto.PayoutMethodDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PayoutMethod} and its DTO {@link PayoutMethodDTO}.
 */
@Mapper(componentModel = "spring")
public interface PayoutMethodMapper extends EntityMapper<PayoutMethodDTO, PayoutMethod> {}
