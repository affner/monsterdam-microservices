package com.fanflip.finance.service.mapper;

import com.fanflip.finance.domain.CreatorEarning;
import com.fanflip.finance.domain.MoneyPayout;
import com.fanflip.finance.service.dto.CreatorEarningDTO;
import com.fanflip.finance.service.dto.MoneyPayoutDTO;
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
