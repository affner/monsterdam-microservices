package com.fanflip.finance.service.mapper;

import com.fanflip.finance.domain.SubscriptionBundle;
import com.fanflip.finance.service.dto.SubscriptionBundleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscriptionBundle} and its DTO {@link SubscriptionBundleDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubscriptionBundleMapper extends EntityMapper<SubscriptionBundleDTO, SubscriptionBundle> {}
