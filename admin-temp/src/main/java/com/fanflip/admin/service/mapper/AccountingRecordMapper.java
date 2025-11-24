package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.AccountingRecord;
import com.monsterdam.admin.domain.Asset;
import com.monsterdam.admin.domain.Budget;
import com.monsterdam.admin.domain.Liability;
import com.monsterdam.admin.domain.PaymentTransaction;
import com.monsterdam.admin.service.dto.AccountingRecordDTO;
import com.monsterdam.admin.service.dto.AssetDTO;
import com.monsterdam.admin.service.dto.BudgetDTO;
import com.monsterdam.admin.service.dto.LiabilityDTO;
import com.monsterdam.admin.service.dto.PaymentTransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AccountingRecord} and its DTO {@link AccountingRecordDTO}.
 */
@Mapper(componentModel = "spring")
public interface AccountingRecordMapper extends EntityMapper<AccountingRecordDTO, AccountingRecord> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentTransactionId")
    @Mapping(target = "budget", source = "budget", qualifiedByName = "budgetId")
    @Mapping(target = "asset", source = "asset", qualifiedByName = "assetId")
    @Mapping(target = "liability", source = "liability", qualifiedByName = "liabilityId")
    AccountingRecordDTO toDto(AccountingRecord s);

    @Named("paymentTransactionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentTransactionDTO toDtoPaymentTransactionId(PaymentTransaction paymentTransaction);

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
