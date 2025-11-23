package com.fanflip.catalogs.service.mapper;

import com.fanflip.catalogs.domain.PayoutMethod;
import com.fanflip.catalogs.service.dto.PayoutMethodDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PayoutMethod} and its DTO {@link PayoutMethodDTO}.
 */
@Mapper(componentModel = "spring")
public interface PayoutMethodMapper extends EntityMapper<PayoutMethodDTO, PayoutMethod> {}
