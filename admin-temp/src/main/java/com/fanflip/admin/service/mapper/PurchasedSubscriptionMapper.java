package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.CreatorEarning;
import com.fanflip.admin.domain.OfferPromotion;
import com.fanflip.admin.domain.PaymentTransaction;
import com.fanflip.admin.domain.PurchasedSubscription;
import com.fanflip.admin.domain.SubscriptionBundle;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.domain.WalletTransaction;
import com.fanflip.admin.service.dto.CreatorEarningDTO;
import com.fanflip.admin.service.dto.OfferPromotionDTO;
import com.fanflip.admin.service.dto.PaymentTransactionDTO;
import com.fanflip.admin.service.dto.PurchasedSubscriptionDTO;
import com.fanflip.admin.service.dto.SubscriptionBundleDTO;
import com.fanflip.admin.service.dto.UserProfileDTO;
import com.fanflip.admin.service.dto.WalletTransactionDTO;
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
