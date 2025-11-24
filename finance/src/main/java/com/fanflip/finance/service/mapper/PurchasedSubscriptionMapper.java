package com.monsterdam.finance.service.mapper;

import com.monsterdam.finance.domain.CreatorEarning;
import com.monsterdam.finance.domain.OfferPromotion;
import com.monsterdam.finance.domain.PaymentTransaction;
import com.monsterdam.finance.domain.PurchasedSubscription;
import com.monsterdam.finance.domain.SubscriptionBundle;
import com.monsterdam.finance.domain.WalletTransaction;
import com.monsterdam.finance.service.dto.CreatorEarningDTO;
import com.monsterdam.finance.service.dto.OfferPromotionDTO;
import com.monsterdam.finance.service.dto.PaymentTransactionDTO;
import com.monsterdam.finance.service.dto.PurchasedSubscriptionDTO;
import com.monsterdam.finance.service.dto.SubscriptionBundleDTO;
import com.monsterdam.finance.service.dto.WalletTransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PurchasedSubscription} and its DTO {@link PurchasedSubscriptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PurchasedSubscriptionMapper extends EntityMapper<PurchasedSubscriptionDTO, PurchasedSubscription> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentTransactionAmount")
    @Mapping(target = "walletTransaction", source = "walletTransaction", qualifiedByName = "walletTransactionAmount")
    @Mapping(target = "creatorEarning", source = "creatorEarning", qualifiedByName = "creatorEarningAmount")
    @Mapping(target = "subscriptionBundle", source = "subscriptionBundle", qualifiedByName = "subscriptionBundleId")
    @Mapping(target = "appliedPromotion", source = "appliedPromotion", qualifiedByName = "offerPromotionPromotionType")
    PurchasedSubscriptionDTO toDto(PurchasedSubscription s);

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

    @Named("subscriptionBundleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubscriptionBundleDTO toDtoSubscriptionBundleId(SubscriptionBundle subscriptionBundle);

    @Named("offerPromotionPromotionType")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "promotionType", source = "promotionType")
    OfferPromotionDTO toDtoOfferPromotionPromotionType(OfferPromotion offerPromotion);
}
