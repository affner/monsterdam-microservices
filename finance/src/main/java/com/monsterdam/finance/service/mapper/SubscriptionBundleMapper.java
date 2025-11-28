package com.monsterdam.finance.service.mapper;

import com.monsterdam.finance.domain.SubscriptionBundle;
import com.monsterdam.finance.service.dto.SubscriptionBundleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscriptionBundle} and its DTO {@link SubscriptionBundleDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubscriptionBundleMapper extends EntityMapper<SubscriptionBundleDTO, SubscriptionBundle> {}
