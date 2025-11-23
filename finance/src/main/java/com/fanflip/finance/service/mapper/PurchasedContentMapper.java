package com.fanflip.finance.service.mapper;

import com.fanflip.finance.domain.CreatorEarning;
import com.fanflip.finance.domain.PaymentTransaction;
import com.fanflip.finance.domain.PurchasedContent;
import com.fanflip.finance.domain.WalletTransaction;
import com.fanflip.finance.service.dto.CreatorEarningDTO;
import com.fanflip.finance.service.dto.PaymentTransactionDTO;
import com.fanflip.finance.service.dto.PurchasedContentDTO;
import com.fanflip.finance.service.dto.WalletTransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PurchasedContent} and its DTO {@link PurchasedContentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PurchasedContentMapper extends EntityMapper<PurchasedContentDTO, PurchasedContent> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentTransactionAmount")
    @Mapping(target = "walletTransaction", source = "walletTransaction", qualifiedByName = "walletTransactionAmount")
    @Mapping(target = "creatorEarning", source = "creatorEarning", qualifiedByName = "creatorEarningAmount")
    PurchasedContentDTO toDto(PurchasedContent s);

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
