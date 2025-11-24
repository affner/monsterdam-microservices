package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.CreatorEarning;
import com.monsterdam.admin.domain.OfferPromotion;
import com.monsterdam.admin.domain.PaymentTransaction;
import com.monsterdam.admin.domain.PurchasedSubscription;
import com.monsterdam.admin.domain.SubscriptionBundle;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.domain.WalletTransaction;
import com.monsterdam.admin.service.dto.CreatorEarningDTO;
import com.monsterdam.admin.service.dto.OfferPromotionDTO;
import com.monsterdam.admin.service.dto.PaymentTransactionDTO;
import com.monsterdam.admin.service.dto.PurchasedSubscriptionDTO;
import com.monsterdam.admin.service.dto.SubscriptionBundleDTO;
import com.monsterdam.admin.service.dto.UserProfileDTO;
import com.monsterdam.admin.service.dto.WalletTransactionDTO;
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
    @Mapping(target = "viewer", source = "viewer", qualifiedByName = "userProfileId")
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

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
