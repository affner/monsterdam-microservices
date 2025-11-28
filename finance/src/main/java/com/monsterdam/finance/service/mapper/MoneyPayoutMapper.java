package com.monsterdam.finance.service.mapper;

import com.monsterdam.finance.domain.CreatorEarning;
import com.monsterdam.finance.domain.MoneyPayout;
import com.monsterdam.finance.service.dto.CreatorEarningDTO;
import com.monsterdam.finance.service.dto.MoneyPayoutDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MoneyPayout} and its DTO {@link MoneyPayoutDTO}.
 */
@Mapper(componentModel = "spring")
public interface MoneyPayoutMapper extends EntityMapper<MoneyPayoutDTO, MoneyPayout> {
    @Mapping(target = "creatorEarning", source = "creatorEarning", qualifiedByName = "creatorEarningAmount")
    MoneyPayoutDTO toDto(MoneyPayout s);

    @Named("creatorEarningAmount")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "amount", source = "amount")
    CreatorEarningDTO toDtoCreatorEarningAmount(CreatorEarning creatorEarning);
}
