package com.monsterdam.finance.service.mapper;

import com.monsterdam.finance.domain.CreatorEarning;
import com.monsterdam.finance.domain.PaymentTransaction;
import com.monsterdam.finance.domain.PurchasedTip;
import com.monsterdam.finance.domain.WalletTransaction;
import com.monsterdam.finance.service.dto.CreatorEarningDTO;
import com.monsterdam.finance.service.dto.PaymentTransactionDTO;
import com.monsterdam.finance.service.dto.PurchasedTipDTO;
import com.monsterdam.finance.service.dto.WalletTransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PurchasedTip} and its DTO {@link PurchasedTipDTO}.
 */
@Mapper(componentModel = "spring")
public interface PurchasedTipMapper extends EntityMapper<PurchasedTipDTO, PurchasedTip> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentTransactionAmount")
    @Mapping(target = "walletTransaction", source = "walletTransaction", qualifiedByName = "walletTransactionAmount")
    @Mapping(target = "creatorEarning", source = "creatorEarning", qualifiedByName = "creatorEarningAmount")
    PurchasedTipDTO toDto(PurchasedTip s);

    @Named("paymentTransactionAmount")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "amount", source = "amount")
    PaymentTransactionDTO toDtoPaymentTransactionAmount(PaymentTransaction paymentTransaction);

    @Named("walletTransactionAmount")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "amount", source = "amount")
    WalletTransactionDTO toDtoWalletTransactionAmount(WalletTransaction walletTransaction);

    @Named("creatorEarningAmount")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "amount", source = "amount")
    CreatorEarningDTO toDtoCreatorEarningAmount(CreatorEarning creatorEarning);
}
