package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.AccountingRecord;
import com.fanflip.admin.domain.Asset;
import com.fanflip.admin.domain.Budget;
import com.fanflip.admin.domain.Liability;
import com.fanflip.admin.service.dto.AccountingRecordDTO;
import com.fanflip.admin.service.dto.AssetDTO;
import com.fanflip.admin.service.dto.BudgetDTO;
import com.fanflip.admin.service.dto.LiabilityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AccountingRecord} and its DTO {@link AccountingRecordDTO}.
 */
@Mapper(componentModel = "spring")
public interface AccountingRecordMapper extends EntityMapper<AccountingRecordDTO, AccountingRecord> {
    @Mapping(target = "budget", source = "budget", qualifiedByName = "budgetId")
    @Mapping(target = "asset", source = "asset", qualifiedByName = "assetId")
    @Mapping(target = "liability", source = "liability", qualifiedByName = "liabilityId")
    AccountingRecordDTO toDto(AccountingRecord s);

    @Named("budgetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BudgetDTO toDtoBudgetId(Budget budget);

    @Named("assetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AssetDTO toDtoAssetId(Asset asset);

    @Named("liabilityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LiabilityDTO toDtoLiabilityId(Liability liability);
}
