package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.PayoutMethod;
import com.fanflip.admin.service.dto.PayoutMethodDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PayoutMethod} and its DTO {@link PayoutMethodDTO}.
 */
@Mapper(componentModel = "spring")
public interface PayoutMethodMapper extends EntityMapper<PayoutMethodDTO, PayoutMethod> {}
