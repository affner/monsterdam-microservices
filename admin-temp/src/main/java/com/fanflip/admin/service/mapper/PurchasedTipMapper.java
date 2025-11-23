package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.CreatorEarning;
import com.fanflip.admin.domain.DirectMessage;
import com.fanflip.admin.domain.PaymentTransaction;
import com.fanflip.admin.domain.PurchasedTip;
import com.fanflip.admin.domain.WalletTransaction;
import com.fanflip.admin.service.dto.CreatorEarningDTO;
import com.fanflip.admin.service.dto.DirectMessageDTO;
import com.fanflip.admin.service.dto.PaymentTransactionDTO;
import com.fanflip.admin.service.dto.PurchasedTipDTO;
import com.fanflip.admin.service.dto.WalletTransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PurchasedTip} and its DTO {@link PurchasedTipDTO}.
 */
@Mapper(componentModel = "spring")
public interface PurchasedTipMapper extends EntityMapper<PurchasedTipDTO, PurchasedTip> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentTransactionAmount")
    @Mapping(target = "walletTransaction", source = "walletTransaction", qualifiedByName = "walletTransactionAmount")
    @Mapping(target = "creatorEarning", source = "creatorEarning", qualifiedByName = "creatorEarningAmount")
    @Mapping(target = "message", source = "message", qualifiedByName = "directMessageMessageContent")
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

    @Named("directMessageMessageContent")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "messageContent", source = "messageContent")
    DirectMessageDTO toDtoDirectMessageMessageContent(DirectMessage directMessage);
}
